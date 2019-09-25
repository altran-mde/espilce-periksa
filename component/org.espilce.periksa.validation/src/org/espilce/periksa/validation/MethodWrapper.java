package org.espilce.periksa.validation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.espilce.periksa.util.Exceptions;
import org.espilce.periksa.validation.AbstractDeclarativeValidator.State;

/**
 * @since 2.6
 */
class MethodWrapper {
	private static final Logger log = Logger.getLogger(AbstractDeclarativeValidator.class);

	private final Method method;
	private final String s;
	private final AbstractDeclarativeValidator instance;

	protected MethodWrapper(AbstractDeclarativeValidator instance, Method m) {
		this.instance = instance;
		this.method = m;
		this.s = m.getName() + ":" + m.getParameterTypes()[0].getName();
	}

	@Override
	public int hashCode() {
		return s.hashCode() ^ instance.hashCode();
	}

	public boolean isMatching(Class<?> param) {
		return method.getParameterTypes()[0].isAssignableFrom(param);
	}

	public void invoke(State state) {
		if (instance.state.get() != null && instance.state.get() != state)
			throw new IllegalStateException("State is already assigned.");
		boolean wasNull = instance.state.get() == null;
		if (wasNull)
			instance.state.set(state);
		try {
			try {
				state.currentMethod = method;
				method.setAccessible(true);
				method.invoke(instance, state.currentObject);
			} catch (IllegalArgumentException e) {
				log.error(e.getMessage(), e);
			} catch (IllegalAccessException e) {
				log.error(e.getMessage(), e);
			} catch (InvocationTargetException e) {
				Throwable targetException = e.getTargetException();
				handleInvocationTargetException(targetException, state);
			}
		} finally {
			if (wasNull)
				instance.state.set(null);
		}
	}
	
	protected void handleInvocationTargetException(Throwable targetException, State state) {
		// ignore GuardException, check is just not evaluated if guard is false
		if (!(targetException instanceof GuardException))
			Exceptions.throwUncheckedException(targetException);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof MethodWrapper))
			return false;
		MethodWrapper mw = (MethodWrapper) obj;
		return s.equals(mw.s) && instance == mw.instance;
	}
	
	public AbstractDeclarativeValidator getInstance() {
		return instance;
	}
	
	public Method getMethod() {
		return method;
	}
}