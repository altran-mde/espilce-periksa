package org.espilce.periksa.validation;

import java.util.Map;
import java.util.Set;

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
	 * Adds the {@link EValidator} element to this set if it is not already present.
	 * More formally, adds the specified {@link EValidator} <tt>e</tt> to this
	 * composite if the composite contains no {@link EValidator} <tt>e2</tt> such
	 * that <tt>(e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2))</tt>. If
	 * this composite already contains the {@link EValidator}, the call leaves the
	 * composite unchanged and returns <tt>false</tt>. In combination with the
	 * restriction on constructors, this ensures that composites never contain
	 * duplicate {@link EValidator}s.
	 *
	 * @param validator validator to be added to this composite
	 * @return <tt>true</tt> if this composite did not already contain the specified
	 *         validator
	 * @see Set#add(Object)
	 */
	boolean addEValidator(EValidator validator);

	/**
	 * Removes the specified {@link EValidator} from this composite if it is present
	 * . More formally, removes an {@link EValidator} <tt>e</tt> such that
	 * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, if this
	 * composite contains such an {@link EValidator}. Returns <tt>true</tt> if this
	 * composite contained the {@link EValidator} (or equivalently, if this
	 * composite changed as a result of the call). (This composite will not contain
	 * the {@link EValidator} once the call returns.)
	 *
	 * @param validator validator to be removed from this composite, if present
	 * @return <tt>true</tt> if this composite contained the specified validator
	 * @see Set#remove(Object)
	 */
	boolean removeValidator(EValidator validator);

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
