/**
 */
package org.espilce.periksa.test.testModel.special.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.espilce.periksa.test.testModel.TestModelPackage;

import org.espilce.periksa.test.testModel.impl.TestModelPackageImpl;

import org.espilce.periksa.test.testModel.special.SpecialEntity;
import org.espilce.periksa.test.testModel.special.SpecialFactory;
import org.espilce.periksa.test.testModel.special.SpecialPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class SpecialPackageImpl extends EPackageImpl implements SpecialPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass specialEntityEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.espilce.periksa.test.testModel.special.SpecialPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private SpecialPackageImpl() {
		super(eNS_URI, SpecialFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link SpecialPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static SpecialPackage init() {
		if (isInited)
			return (SpecialPackage) EPackage.Registry.INSTANCE.getEPackage(SpecialPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredSpecialPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		SpecialPackageImpl theSpecialPackage = registeredSpecialPackage instanceof SpecialPackageImpl
				? (SpecialPackageImpl) registeredSpecialPackage
				: new SpecialPackageImpl();

		isInited = true;

		// Obtain or create and register interdependencies
		Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TestModelPackage.eNS_URI);
		TestModelPackageImpl theTestModelPackage = (TestModelPackageImpl) (registeredPackage instanceof TestModelPackageImpl
				? registeredPackage
				: TestModelPackage.eINSTANCE);

		// Create package meta-data objects
		theSpecialPackage.createPackageContents();
		theTestModelPackage.createPackageContents();

		// Initialize created meta-data
		theSpecialPackage.initializePackageContents();
		theTestModelPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theSpecialPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(SpecialPackage.eNS_URI, theSpecialPackage);
		return theSpecialPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSpecialEntity() {
		return specialEntityEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSpecialEntity_Description() {
		return (EAttribute) specialEntityEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SpecialFactory getSpecialFactory() {
		return (SpecialFactory) getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated)
			return;
		isCreated = true;

		// Create classes and their features
		specialEntityEClass = createEClass(SPECIAL_ENTITY);
		createEAttribute(specialEntityEClass, SPECIAL_ENTITY__DESCRIPTION);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized)
			return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		TestModelPackage theTestModelPackage = (TestModelPackage) EPackage.Registry.INSTANCE
				.getEPackage(TestModelPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		specialEntityEClass.getESuperTypes().add(theTestModelPackage.getEntity());

		// Initialize classes, features, and operations; add parameters
		initEClass(specialEntityEClass, SpecialEntity.class, "SpecialEntity", !IS_ABSTRACT, !IS_INTERFACE,
				IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSpecialEntity_Description(), ecorePackage.getEString(), "description", null, 0, 1,
				SpecialEntity.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_UNSETTABLE, !IS_ID, IS_UNIQUE,
				!IS_DERIVED, IS_ORDERED);
	}

} //SpecialPackageImpl
