package org.espilce.periksa.validation;

import java.lang.reflect.Method;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

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
