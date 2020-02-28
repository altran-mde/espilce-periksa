package org.espilce.periksa.validation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ContextAwareCheckMethod extends CheckMethod {
	protected ContextAwareCheckMethod(Method method, ContextAwareDeclarativeValidator instance) {
		super(method, instance);
	}
	
	@Override
	public ContextAwareDeclarativeValidator getInstance() {
		return (ContextAwareDeclarativeValidator) super.getInstance();
	}
	
	@Override
	protected void invoke(CheckContext context) throws Exception, InvocationTargetException {
		if (getInstance().getContext() != null) {
			throw new IllegalStateException("Context already assigned.");
		}
		getInstance().setContext(context);
		try {
			if (getMethod().getParameterCount() == 1) {
				getMethod().setAccessible(true);
				getMethod().invoke(getInstance(), context.getEObject());
			} else {
				super.invoke(context);
			}
		} finally {
			getInstance().setContext(null);
		}
	}
}
