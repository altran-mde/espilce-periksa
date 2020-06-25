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
import java.util.Arrays;

import org.eclipse.emf.ecore.EClass;

/**
 * @author itemis AG - Initial contribution and API
 * @author Altran Netherlands B.V. - Refactoring including API updates
 */
public class CheckMethod {
	private final Method method;
	private final Object instance;
	
	protected CheckMethod(Method method, Object instance) {
		this.method = method;
		this.instance = instance;
	}
	
	public Method getMethod() {
		return method;
	}
	
	public Object getInstance() {
		return instance;
	}
	
	/**
	 * Returns the class of the instance on which this method is invoked, or the
	 * class which declares this method if it is static.
	 */
	protected Class<?> getInvokingClass() {
		return instance == null ? method.getDeclaringClass() : instance.getClass();
	}
	
	protected boolean isValidating(EClass eClazz) {
		return method.getParameterTypes()[0].isAssignableFrom(eClazz.getInstanceClass());
	}

	protected void invoke(CheckContext context) throws Exception, InvocationTargetException {
		method.setAccessible(true);
		method.invoke(instance, context.getEObject(), context);
	}

	/**
	 * Custom hashCode to avoid duplicate checks in case of overridden check methods
	 * 
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((instance == null) ? 0 : instance.hashCode());
		result = prime * result + ((method == null) ? 0 : method.getName().hashCode());
		result = prime * result + ((method == null) ? 0 : Arrays.hashCode(method.getParameters()));
		return result;
	}

	/**
	 * Custom equals to avoid duplicate checks in case of overridden check methods
	 * 
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CheckMethod other = (CheckMethod) obj;
		if (instance == null) {
			if (other.instance != null)
				return false;
		} else if (!instance.equals(other.instance))
			return false;
		if (method == null) {
			if (other.method != null)
				return false;
		} else if (!method.equals(other.method)) {
			return false;
		} else if (!Arrays.equals(method.getParameters(), other.method.getParameters())) {
			return false;
		}
		return true;
	}
}
