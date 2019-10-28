/* Copied from https://github.com/eclipse/xtext-core/blob/v2.18.0/org.eclipse.xtext/src/
 * org/eclipse/xtext/validation/AbstractDeclarativeValidator.java
 */
/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.espilce.periksa.validation;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.jdt.annotation.NonNull;
import org.espilce.periksa.validation.State.StateAccess;

/**
 * Allows subclasses to specify invariants in a declarative manner using {@link Check} annotation.
 * 
 * Example:
 * 
 * <pre>
 * &#064;Check
 * void checkName(ParserRule rule) {
 * 	if (!toFirstUpper(rule.getName()).equals(rule.getName())) {
 * 		warning(&quot;Name should start with a capital.&quot;, XtextPackage.Literals.ABSTRACT_RULE__NAME);
 * 	}
 * }
 * </pre>
 * 
 * @author Sven Efftinge - Initial contribution and API
 * @author Michael Clay
 */
public abstract class AbstractDeclarativeValidator extends AbstractValidator implements
		ValidationMessageAcceptor {

	private static final GuardException guardException = new GuardException();

	private volatile Set<MethodWrapper> checkMethods = null;

	private ValidationMessageAcceptor messageAcceptor;

	public AbstractDeclarativeValidator() {
		this.state = new ThreadLocal<State>();
		this.messageAcceptor = this;
	}

	private List<MethodWrapper> collectMethods(Class<? extends AbstractDeclarativeValidator> clazz) {
		List<MethodWrapper> checkMethods = new ArrayList<MethodWrapper>();
		Set<Class<?>> visitedClasses = new HashSet<Class<?>>(4);
		collectMethods(this, clazz, visitedClasses, checkMethods);
		return checkMethods;
	}

	private void collectMethods(AbstractDeclarativeValidator instance,
			Class<? extends AbstractDeclarativeValidator> clazz, Collection<Class<?>> visitedClasses,
			Collection<MethodWrapper> result) {
		if (visitedClasses.contains(clazz))
			return;
		collectMethodsImpl(instance, clazz, visitedClasses, result);
		Class<? extends AbstractDeclarativeValidator> k = clazz;
		while (k != null) {
			ComposedChecks checks = k.getAnnotation(ComposedChecks.class);
			if (checks != null) {
				for (Class<? extends AbstractDeclarativeValidator> external : checks.validators())
					collectMethods(null, external, visitedClasses, result);
			}
			k = getSuperClass(k);
		}
	}

	private Class<? extends AbstractDeclarativeValidator> getSuperClass(
			Class<? extends AbstractDeclarativeValidator> clazz) {
		try {
			Class<? extends AbstractDeclarativeValidator> superClass = clazz.getSuperclass().asSubclass(
					AbstractDeclarativeValidator.class);
			if (AbstractDeclarativeValidator.class.equals(superClass))
				return null;
			return superClass;
		} catch (ClassCastException e) {
			return null;
		}
	}

	private void collectMethodsImpl(@NonNull AbstractDeclarativeValidator instance,
			Class<? extends AbstractDeclarativeValidator> clazz, Collection<Class<?>> visitedClasses,
			Collection<MethodWrapper> result) {
		if (!visitedClasses.add(clazz))
			return;
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getAnnotation(Check.class) != null && method.getParameterTypes().length == 1) {
				result.add(createMethodWrapper(instance, method));
			}
		}
		Class<? extends AbstractDeclarativeValidator> superClass = getSuperClass(clazz);
		if (superClass != null)
			collectMethodsImpl(instance, superClass, visitedClasses, result);
	}

	/**
	 * @since 2.6
	 */
	protected MethodWrapper createMethodWrapper(AbstractDeclarativeValidator instanceToUse, Method method) {
		return new MethodWrapper(instanceToUse, method);
	}

	private final SimpleCache<Class<?>, List<MethodWrapper>> methodsForType = new SimpleCache<Class<?>, List<MethodWrapper>>(
			new Function<Class<?>, List<MethodWrapper>>() {
				@Override
				public List<MethodWrapper> apply(Class<?> param) {
					List<MethodWrapper> result = new ArrayList<MethodWrapper>();
					for (MethodWrapper mw : checkMethods) {
						if (mw.isMatching(param))
							result.add(mw);
					}
					return result;
				}
			});

	protected final ThreadLocal<State> state;

	protected EObject getCurrentObject() {
		return state.get().currentObject;
	}

	protected Method getCurrentMethod() {
		return state.get().currentMethod;
	}

	protected DiagnosticChain getChain() {
		return state.get().chain;
	}

	protected Map<Object, Object> getContext() {
		return state.get().context;
	}

	@Override
	protected final boolean internalValidate(EClass class1, EObject object, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		if (checkMethods == null) {
			synchronized (this) {
				if (checkMethods == null) {
					Set<MethodWrapper> checkMethods = new LinkedHashSet<>();
					checkMethods.addAll(collectMethods(getClass()));
					this.checkMethods = checkMethods;
				}
			}
		}

		State state = new State();
		state.chain = diagnostics;
		state.currentObject = object;
		state.context = context;

		for (MethodWrapper method : methodsForType.get(object.getClass())) {
			method.invoke(state);
		}

		return !state.hasErrors;
	}

	protected void guard(boolean guardExpression) {
		if (!guardExpression) {
			throw guardException;
		}
	}

	protected void checkDone() {
		throw guardException;
	}

	//////////////////////////////////////////////////////////
	// Implementation of the Validation message acceptor below
	//////////////////////////////////////////////////////////

	@Override
	public void acceptError(String message, EObject object, EStructuralFeature feature, int index, String code,
			String... issueData) {
		checkIsFromCurrentlyCheckedResource(object);
		this.state.get().hasErrors = true;
		state.get().chain.add(createDiagnostic(Severity.ERROR, message, object, feature, index, code, issueData));
	}

	/**
	 * @since 2.4
	 */
	protected void checkIsFromCurrentlyCheckedResource(EObject object) {
		if (object != null && this.state.get() != null && this.state.get().currentObject != null
				&& object.eResource() != this.state.get().currentObject.eResource()) {
			URI uriGiven = null;
			if (object.eResource() != null)
				uriGiven = object.eResource().getURI();
			URI uri = null;
			if (this.state.get().currentObject.eResource() != null)
				uri = this.state.get().currentObject.eResource().getURI();
			throw new IllegalArgumentException(
					"You can only add issues for EObjects contained in the currently validated resource '" + uri
							+ "'. But the given EObject was contained in '" + uriGiven + "'");
		}
	}

	@Override
	public void acceptWarning(String message, EObject object, EStructuralFeature feature, int index, String code,
			String... issueData) {
		checkIsFromCurrentlyCheckedResource(object);
		state.get().chain.add(createDiagnostic(Severity.WARNING, message, object, feature, index, code, issueData));
	}

	@Override
	public void acceptInfo(String message, EObject object, EStructuralFeature feature, int index, String code,
			String... issueData) {
		checkIsFromCurrentlyCheckedResource(object);
		state.get().chain.add(createDiagnostic(Severity.INFO, message, object, feature, index, code, issueData));
	}

	protected Diagnostic createDiagnostic(Severity severity, String message, EObject object,
			EStructuralFeature feature, int index, String code, String... issueData) {
		int diagnosticSeverity = toDiagnosticSeverity(severity);
		Diagnostic result = new FeatureBasedDiagnostic(diagnosticSeverity, message, object, feature, index,
				code, issueData);
		return result;
	}

	protected int toDiagnosticSeverity(Severity severity) {
		int diagnosticSeverity = -1;
		switch (severity) {
			case ERROR:
				diagnosticSeverity = Diagnostic.ERROR;
				break;
			case WARNING:
				diagnosticSeverity = Diagnostic.WARNING;
				break;
			case INFO:
				diagnosticSeverity = Diagnostic.INFO;
				break;
			default:
				throw new IllegalArgumentException("Unknow severity " + severity);
		}
		return diagnosticSeverity;
	}

	public StateAccess setMessageAcceptor(ValidationMessageAcceptor messageAcceptor) {
		this.messageAcceptor = messageAcceptor;
		return new StateAccess(this);
	}

	public ValidationMessageAcceptor getMessageAcceptor() {
		return messageAcceptor;
	}

}