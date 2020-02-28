package org.espilce.periksa.validation;

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;

/**
 * An {@link EValidator} that delegates validation to all its
 * {@link #getEValidators()}.
 */
public interface ECompositeValidator extends EValidator {
	/**
	 * Returns the {@link EValidator}s that are used for validation.
	 * 
	 * @return the {@link EValidator}s that are used for validation.
	 */
	Iterable<EValidator> getEValidators();

	/**
	 * Ensures that this composite contains the specified {@link EValidator}.
	 * Returns <tt>true</tt> if this composite changed as a result of the call.
	 * (Returns <tt>false</tt> if this composite does not permit duplicates and
	 * already contains the specified {@link EValidator}.)
	 * 
	 * @param validator {@link EValidator} whose presence in this composite is to be
	 *                  ensured
	 * @return <tt>true</tt> if this composite changed as a result of the call
	 * @see Collection#add(Object)
	 */
	boolean addEValidator(EValidator validator);

	@Override
	default boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean valid = true;
		for (EValidator validator : getEValidators()) {
			valid &= validator.validate(eObject, diagnostics, context);
		}
		return valid;
	}

	@Override
	default boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean valid = true;
		for (EValidator validator : getEValidators()) {
			valid &= validator.validate(eClass, eObject, diagnostics, context);
		}
		return valid;
	}

	@Override
	default boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean valid = true;
		for (EValidator validator : getEValidators()) {
			valid &= validator.validate(eDataType, value, diagnostics, context);
		}
		return valid;
	}
}
