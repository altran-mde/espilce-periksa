/**
 */
package org.espilce.periksa.test.testModel.special;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.espilce.periksa.test.testModel.TestModelPackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see org.espilce.periksa.test.testModel.special.SpecialFactory
 * @model kind="package"
 * @generated
 */
public interface SpecialPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "special";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.example.org/special";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "special";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SpecialPackage eINSTANCE = org.espilce.periksa.test.testModel.special.impl.SpecialPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.espilce.periksa.test.testModel.special.impl.SpecialEntityImpl <em>Entity</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.espilce.periksa.test.testModel.special.impl.SpecialEntityImpl
	 * @see org.espilce.periksa.test.testModel.special.impl.SpecialPackageImpl#getSpecialEntity()
	 * @generated
	 */
	int SPECIAL_ENTITY = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPECIAL_ENTITY__NAME = TestModelPackage.ENTITY__NAME;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPECIAL_ENTITY__DESCRIPTION = TestModelPackage.ENTITY_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPECIAL_ENTITY_FEATURE_COUNT = TestModelPackage.ENTITY_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Entity</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SPECIAL_ENTITY_OPERATION_COUNT = TestModelPackage.ENTITY_OPERATION_COUNT + 0;

	/**
	 * Returns the meta object for class '{@link org.espilce.periksa.test.testModel.special.SpecialEntity <em>Entity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Entity</em>'.
	 * @see org.espilce.periksa.test.testModel.special.SpecialEntity
	 * @generated
	 */
	EClass getSpecialEntity();

	/**
	 * Returns the meta object for the attribute '{@link org.espilce.periksa.test.testModel.special.SpecialEntity#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.espilce.periksa.test.testModel.special.SpecialEntity#getDescription()
	 * @see #getSpecialEntity()
	 * @generated
	 */
	EAttribute getSpecialEntity_Description();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SpecialFactory getSpecialFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.espilce.periksa.test.testModel.special.impl.SpecialEntityImpl <em>Entity</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.espilce.periksa.test.testModel.special.impl.SpecialEntityImpl
		 * @see org.espilce.periksa.test.testModel.special.impl.SpecialPackageImpl#getSpecialEntity()
		 * @generated
		 */
		EClass SPECIAL_ENTITY = eINSTANCE.getSpecialEntity();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SPECIAL_ENTITY__DESCRIPTION = eINSTANCE.getSpecialEntity_Description();

	}

} //SpecialPackage
