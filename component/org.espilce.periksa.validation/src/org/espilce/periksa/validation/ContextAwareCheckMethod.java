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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author itemis AG - Initial contribution and API
 * @author Altran Netherlands B.V. - Refactoring including API updates
 */
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
