package org.espilce.periksa.validation;

import org.eclipse.emf.ecore.EStructuralFeature;

public interface ContextAwareValidationReporter extends ValidationReporter {
	/**
	 * Annotate an instance, a feature value, or all feature values with an error.
	 * 
	 * @param message   the error message. May not be <code>null</code>.
	 * @param feature   the feature or <code>null</code> if the complete instance
	 *                  should be annotated.
	 * @param index     the index of the erroneous value or <code>-1</code> if all
	 *                  values are considered to be invalid. The index is ignored if
	 *                  the feature is null or the feature is a single value
	 *                  feature.
	 * @param code      the optional issue code.
	 * @param issueData the optional issue data.
	 */
	void error(String message, EStructuralFeature feature, int index, String code, String... issueData);

	default void error(String message, EStructuralFeature feature, String code, String... issueData) {
		error(message, feature, INSIGNIFICANT_INDEX, code, issueData);
	}

	default void error(String message, EStructuralFeature feature) {
		error(message, feature, INSIGNIFICANT_INDEX);
	}

	default void error(String message, EStructuralFeature feature, int index) {
		error(message, feature, index, null);
	}

	/**
	 * Annotate an instance, a feature value, or all feature values with a warning.
	 * 
	 * @param message   the warning message. May not be <code>null</code>.
	 * @param feature   the feature or <code>null</code> if the complete instance
	 *                  should be annotated.
	 * @param index     the index of the relevant value or <code>-1</code> if all
	 *                  values are considered to be affected. The index is ignored
	 *                  if the feature is null or the feature is a single value
	 *                  feature.
	 * @param code      the optional issue code.
	 * @param issueData the optional issue data.
	 */
	void warning(String message, EStructuralFeature feature, int index, String code, String... issueData);

	default void warning(String message, EStructuralFeature feature, String code, String... issueData) {
		warning(message, feature, INSIGNIFICANT_INDEX, code, issueData);
	}

	default void warning(String message, EStructuralFeature feature) {
		warning(message, feature, INSIGNIFICANT_INDEX);
	}

	default void warning(String message, EStructuralFeature feature, int index) {
		warning(message, feature, index, null);
	}

	/**
	 * Annotate an instance, a feature value, or all feature values with an info
	 * message.
	 * 
	 * @param message   the info message. May not be <code>null</code>.
	 * @param feature   the feature or <code>null</code> if the complete instance
	 *                  should be annotated.
	 * @param index     the index of the interesting value or <code>-1</code> if all
	 *                  values are considered to be interesting. The index is
	 *                  ignored if the feature is null or the feature is a single
	 *                  value feature.
	 * @param code      the optional issue code.
	 * @param issueData the optional issue data.
	 */
	void info(String message, EStructuralFeature feature, int index, String code, String... issueData);

	default void info(String message, EStructuralFeature feature, String code, String... issueData) {
		info(message, feature, INSIGNIFICANT_INDEX, code, issueData);
	}

	default void info(String message, EStructuralFeature feature) {
		info(message, feature, INSIGNIFICANT_INDEX, null);
	}

	default void info(String message, EStructuralFeature feature, int index) {
		info(message, feature, index, null);
	}
}
