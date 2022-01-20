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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;

/**
 * Base class for {@link EValidator} that defines validations by means of
 * implementing methods that are annotated with the {@link Check} annotation.
 * While invoking an annotated validation method, the model element that is
 * currently validated is known as the {@link #getContext() context} and
 * therefore may be omitted as method parameter. This makes writing validations
 * even more convenient.
 * 
 * @author itemis AG - Initial contribution and API
 * @author Altran Netherlands B.V. - Refactoring including API updates
 */
public abstract class ContextAwareDeclarativeValidator extends DeclarativeValidator
		implements ContextAwareValidationReporter {
	// Using a thread local variable to support that this validator instance can be
	// invoked simultaneously from multiple threads (i.e. when multiple models are
	// validated at the same time)
	private final ThreadLocal<CheckContext> context = new ThreadLocal<>();

	public ContextAwareDeclarativeValidator() {
		super();
	}

	void setContext(CheckContext context) {
		this.context.set(context);
	}

	/**
	 * The {@link CheckContext} will only be available within a {@link Check}
	 * methods.
	 * 
	 * @return the context when within a {@link Check} method, <tt>null</tt>
	 *         otherwise.
	 */
	protected final CheckContext getContext() {
		return context.get();
	}

	@Override
	protected CheckMethod createCheckMethod(Method method, Object validatorInstance) throws IllegalArgumentException {
		if (validatorInstance instanceof ContextAwareDeclarativeValidator) {
			return new ContextAwareCheckMethod(method, (ContextAwareDeclarativeValidator) validatorInstance);
		} else {
			return super.createCheckMethod(method, validatorInstance);
		}
	}

	@Override
	public void error(String message, EStructuralFeature feature, int index, String code, String... issueData) {
		getContext().getReport().error(message, feature, index, code, issueData);
	}

	@Override
	public void error(String message, EObject object, EStructuralFeature feature, int index, String code,
			String... issueData) {
		getContext().getReport().error(message, object, feature, index, code, issueData);
	}

	@Override
	public void warning(String message, EStructuralFeature feature, int index, String code, String... issueData) {
		getContext().getReport().warning(message, feature, index, code, issueData);
	}

	@Override
	public void warning(String message, EObject object, EStructuralFeature feature, int index, String code,
			String... issueData) {
		getContext().getReport().warning(message, object, feature, index, code, issueData);
	}

	@Override
	public void info(String message, EStructuralFeature feature, int index, String code, String... issueData) {
		getContext().getReport().info(message, feature, index, code, issueData);
	}

	@Override
	public void info(String message, EObject object, EStructuralFeature feature, int index, String code,
			String... issueData) {
		getContext().getReport().info(message, object, feature, index, code, issueData);
	}
}
