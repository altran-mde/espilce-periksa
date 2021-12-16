/**
 */
package org.espilce.periksa.test.testModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Entity Container</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.espilce.periksa.test.testModel.EntityContainer#getEntities <em>Entities</em>}</li>
 * </ul>
 *
 * @see org.espilce.periksa.test.testModel.TestModelPackage#getEntityContainer()
 * @model
 * @generated
 */
public interface EntityContainer extends EObject {
	/**
	 * Returns the value of the '<em><b>Entities</b></em>' containment reference list.
	 * The list contents are of type {@link org.espilce.periksa.test.testModel.Entity}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Entities</em>' containment reference list.
	 * @see org.espilce.periksa.test.testModel.TestModelPackage#getEntityContainer_Entities()
	 * @model containment="true"
	 * @generated
	 */
	EList<Entity> getEntities();

} // EntityContainer
