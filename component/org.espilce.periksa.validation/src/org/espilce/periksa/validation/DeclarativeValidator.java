package org.espilce.periksa.validation;

import static org.eclipse.emf.common.util.Diagnostic.ERROR;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;

public class DeclarativeValidator implements EValidator {
	private final Class<?> validatorClazz;
	private final Supplier<?> validatorInstanceSupplier;

	private volatile Set<CheckMethod> checkMethods;
	private volatile Map<EClass, List<CheckMethod>> checkMethodsForType;

	// Using a protected constructor has the same effect as making the class abstract 
	protected DeclarativeValidator() {
		this.validatorClazz = getClass();
		this.validatorInstanceSupplier = () -> this;
	}

	public static DeclarativeValidator of(Class<?> validatorClazz) throws IllegalArgumentException {
		if (EValidator.class.isAssignableFrom(validatorClazz)) {
			throw new IllegalArgumentException(
					String.format("Validator class should not extend/implement %s", EValidator.class.getName()));
		}
		return new DeclarativeValidator(validatorClazz);
	}
	
	/**
	 * Using a public static method that calls a private constructor prevents subclasses to use this constructor.
	 */
	private DeclarativeValidator(Class<?> validatorClazz) {
		this.validatorClazz = validatorClazz;
		this.validatorInstanceSupplier = lazyInstance(validatorClazz);
	}

	public static DeclarativeValidator of(Object validatorInstance) throws IllegalArgumentException {
		if (validatorInstance instanceof EValidator) {
			throw new IllegalArgumentException(
					String.format("Validator instance should not implement interface %s", EValidator.class.getName()));
		}
		return new DeclarativeValidator(validatorInstance);
	}
	
	/**
	 * Using a public static method that calls a private constructor prevents subclasses to use this constructor.
	 */
	private DeclarativeValidator(Object validatorInstance) {
		this.validatorClazz = validatorInstance.getClass();
		this.validatorInstanceSupplier = () -> validatorInstance;
	}

	@Override
	public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return true;
	}

	@Override
	public final boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate(eObject == null ? null : eObject.eClass(), eObject, diagnostics, context);
	}

	@Override
	public final boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		if (null == checkMethods) {
			synchronized (validatorClazz) {
				if (null == checkMethods) {
					checkMethodsForType = new ConcurrentHashMap<>();

					Set<Class<?>> visitedClasses = new HashSet<>();
					checkMethods = new LinkedHashSet<>();
					try {
						collectCheckMethods(validatorInstanceSupplier, validatorClazz, visitedClasses, checkMethods);
						if (checkMethods.isEmpty()) {
							diagnostics.add(new BasicDiagnostic(ERROR, getClass().getName(), 0,
									validatorClazz.getName() + " does not declare any @Check methods.", new Object[0]));
							return false;
						}
					} catch (IllegalArgumentException e) {
						diagnostics.add(new BasicDiagnostic(ERROR, getClass().getName(), 0,
								"Failed to initialize declarative validator: " + e.getMessage(), new Object[] { e }));
						return false;
					}
				}
			}
		}

		List<CheckMethod> typeMethods = checkMethodsForType.computeIfAbsent(eClass,
				k -> checkMethods.stream().filter(m -> m.isValidating(k)).collect(Collectors.toList()));

		if (typeMethods.isEmpty()) {
			return true;
		}

		CheckContext checkContext = createCheckContext(eObject, diagnostics, context);
		for (CheckMethod checkMethod : typeMethods) {
			try {
				checkMethod.invoke(checkContext);
			} catch (InvocationTargetException e) {
				handleCheckMethodInvocationException(e.getTargetException(), checkMethod, checkContext);
			} catch (Exception e) {
				checkContext.reportDiagnostic(new BasicDiagnostic(ERROR, checkMethod.getInvokingClass().getName(), 0,
						"Failed to invoke check " + checkMethod.getMethod().toString() + ": " + e.getMessage(),
						new Object[] { e }));
			}
		}
		return checkContext.isValid();
	}
	
	protected void handleCheckMethodInvocationException(Throwable throwable, CheckMethod method, CheckContext context) {
		final Object validatorInstance = method.getInstance();
		if (validatorInstance != this && validatorInstance instanceof DeclarativeValidator) {
			// By default, delegate to the validator that declares the Check method 
			((DeclarativeValidator) validatorInstance).handleCheckMethodInvocationException(throwable, method, context);
			return;
		}
		
		if (throwable instanceof RuntimeException) {
			throw (RuntimeException) throwable;
		} else if (throwable instanceof Error) {
			throw (Error) throwable;
		}
		
		context.reportDiagnostic(new BasicDiagnostic(ERROR, method.getInvokingClass().getName(), 0,
				"Check " + method.getMethod().toString() + " threw an exception: " + throwable.getMessage(),
				new Object[] { throwable }));
	}
	
	protected CheckContext createCheckContext(EObject eObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		return new CheckContext(eObject, diagnostics, context);
	}

	protected <T> T newInstance(Class<T> clazz) throws IllegalArgumentException {
		try {
			return clazz.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			throw new IllegalArgumentException(clazz.getName() + " should implement a default public constructor.", e);
		}
	}

	private <T> Supplier<T> lazyInstance(Class<T> clazz) {
		return new Supplier<T>() {
			private volatile T instance = null;

			@Override
			public T get() {
		        if (instance == null) {
		            synchronized(this) {
		                if (instance == null) {
		                	instance = newInstance(clazz);
		                }
		            }
		        }
		        return instance;
			}
		};
	}

	/**
	 * Using a {@link Supplier} is a nice trick to delay the instantiation of a
	 * class until it is really needed. If the validatorClazz only contains static
	 * {@link Check} methods, no instance is required.
	 * 
	 * @param validatorInstanceSupplier
	 * @param validatorClazz
	 * @param visitedClasses
	 * @param result
	 * @throws IllegalArgumentException
	 * @see {@link #lazyInstance(Class)}
	 */
	private void collectCheckMethods(Supplier<?> validatorInstanceSupplier, Class<?> validatorClazz,
			Set<Class<?>> visitedClasses, Set<CheckMethod> result) throws IllegalArgumentException {
		if (!visitedClasses.add(validatorClazz)) {
			// Class already processed, skip to avoid duplicate validations
			return;
		}
		for (Method method : validatorClazz.getDeclaredMethods()) {
			if (method.isAnnotationPresent(Check.class)) {
				if (Modifier.isStatic(method.getModifiers())) {
					result.add(createCheckMethod(method, null));
				} else {
					result.add(createCheckMethod(method, validatorInstanceSupplier.get()));
				}
			}
		}

		Class<?> superclass = validatorClazz.getSuperclass();
		if (null != superclass) {
			collectCheckMethods(validatorInstanceSupplier, superclass, visitedClasses, result);
		}
		for (Class<?> implementedInterface : validatorClazz.getInterfaces()) {
			// Interfaces may implement default methods with @Check annotations
			collectCheckMethods(validatorInstanceSupplier, implementedInterface, visitedClasses, result);
		}
		final ComposedChecks composedChecks = validatorClazz.getAnnotation(ComposedChecks.class);
		if (composedChecks != null) {
			for (Class<?> composedCheckClazz : composedChecks.value()) {
				if (DeclarativeValidator.class.isAssignableFrom(composedCheckClazz)) {
					// DeclarativeValidators might override methods (e.g. createCheckMethod) and
					// therefore collecting their check methods should be delegated to their instance.
					DeclarativeValidator validatorInstance = (DeclarativeValidator) newInstance(composedCheckClazz);
					validatorInstance.collectCheckMethods(() -> validatorInstance, composedCheckClazz, visitedClasses, result);
				} else {
					collectCheckMethods(lazyInstance(composedCheckClazz), composedCheckClazz, visitedClasses, result);
				}
			}
		}
	}

	protected CheckMethod createCheckMethod(Method method, Object validatorInstance) throws IllegalArgumentException {
		if (method.getParameterCount() != 2) {
			throw new IllegalArgumentException("Check method should have exactly 2 parameters: " + method);
		} else if (!method.getParameterTypes()[1].equals(CheckContext.class)) {
			throw new IllegalArgumentException(
					"Check methods' 2nd parameter should be of type" + CheckContext.class.getName() + ": " + method);
		}
		return new CheckMethod(method, validatorInstance);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((validatorClazz == null) ? 0 : validatorClazz.getName().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeclarativeValidator other = (DeclarativeValidator) obj;
		if (validatorClazz == null) {
			if (other.validatorClazz != null)
				return false;
		} else if (!validatorClazz.equals(other.validatorClazz))
			return false;
		return true;
	}
}
