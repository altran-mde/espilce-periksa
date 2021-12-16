package org.espilce.periksa.validation;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * This library contains typical validations to add to a meta-model.
 */
public class ValidationLibrary {
	/**
	 * Checks if the {@link EObject#eGet(EStructuralFeature) value} of
	 * <code>feature</code> for <code>object</code>
	 * ({@link EObject#eIsSet(EStructuralFeature) if set}) is a duplicate within the
	 * scope of the {@link EObject#eContainingFeature() containing feature} of the
	 * <code>object</code>. If true, reports an error on <code>object</code> about
	 * the duplicate <code>feature</code> value.
	 * 
	 * @param object  the object to inspect
	 * @param feature the feature to inspect
	 * @param context the check context used to report errors
	 * @see #checkDuplicateValue(Iterable, EStructuralFeature, CheckContext)
	 */
	public static void checkDuplicateValue(EObject object, EStructuralFeature feature, CheckContext context) {
		if (!object.eIsSet(feature)) {
			// We do not validate the default value
			return;
		}
		EStructuralFeature objectContainingFeature = object.eContainingFeature();
		EObject objectContainer = object.eContainer();
		if (objectContainer == null || objectContainingFeature == null || !objectContainingFeature.isMany()) {
			// Object is not contained by a many feature, hence has no siblings, thus no duplicates
			return;
		}
		EClass featureContainingClass = feature.getEContainingClass();
		Object objectValue = object.eGet(feature);
		Object siblings = objectContainer.eGet(objectContainingFeature);
		// siblings should be an Iterable as objectContainingFeature.isMany() == true
		for (Object sibling : (Iterable<?>) siblings) {
			if (sibling != object && featureContainingClass.isInstance(sibling) && ((EObject) sibling).eIsSet(feature)) {
				Object siblingValue = ((EObject) sibling).eGet(feature);
				if (Objects.equals(objectValue, siblingValue)) {
					context.getReport().error(String.format("Duplicate %s", feature.getName()), object, feature);
					return;
				}
			}
		}
	}

	/**
	 * Checks for duplicate <code>feature</code> values
	 * ({@link EObject#eIsSet(EStructuralFeature) if set}) within
	 * <code>objects</code> and reports an error for each element with a duplicate
	 * <code>feature</code> value.<br>
	 * <b>NOTE:</b> This method is faster than
	 * {@link #checkDuplicateValue(EObject, EStructuralFeature, CheckContext)} as
	 * <code>objects</code> is iterated only once.
	 * 
	 * @param objects the objects to inspect
	 * @param feature the feature to inspect
	 * @param context the check context used to report errors
	 */
	public static void checkDuplicateValue(Iterable<? extends EObject> objects, EStructuralFeature feature,
			CheckContext context) {
		EClass featureContainingClass = feature.getEContainingClass();
		Map<Object, List<EObject>> featureValueGroups = StreamSupport.stream(objects.spliterator(), false)
				.filter(featureContainingClass::isInstance)
				.filter(object -> object.eIsSet(feature))
				.collect(Collectors.groupingBy(object -> object.eGet(feature)));
		Stream<EObject> duplicates = featureValueGroups.values().stream().filter(group -> group.size() > 1)
				.flatMap(List::stream);
		duplicates.forEach(duplicate -> {
			context.getReport().error(String.format("Duplicate %s", feature.getName()), duplicate, feature);
		});
	}
}
