package org.espilce.periksa.test;

import java.util.logging.Logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.espilce.periksa.test.testModel.Entity;
import org.espilce.periksa.test.testModel.TestModelFactory;
import org.espilce.periksa.test.testModel.TestModelPackage;
import org.espilce.periksa.test.testModel.special.SpecialEntity;
import org.espilce.periksa.test.testModel.special.SpecialFactory;
import org.espilce.periksa.test.testModel.special.SpecialPackage;
import org.espilce.periksa.testsupport.ATestValidator;
import org.espilce.periksa.validation.DeclarativeValidator;
import org.espilce.periksa.validation.EValidatorRegistrar;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class ValidationTest extends ATestValidator {

	@BeforeClass
	public static void registerValidators() {
		// Check if run as JUnit Test (requires bootstrap) or JUnit Plug-in Test
		final Bundle bundle = FrameworkUtil.getBundle(ValidationTest.class);
		if (null == bundle) {
			Logger.getLogger(ValidationTest.class.getName()).info("ModelValidator Standalone configuration");
			final EValidatorRegistrar registrar = new EValidatorRegistrar();
			registrar.register(TestModelPackage.eINSTANCE, DeclarativeValidator.of(EntityStartsWithCapital.class));
			registrar.register(TestModelPackage.eINSTANCE, new ModelValidator());
			registrar.register(SpecialPackage.eINSTANCE, new SpecialValidator());
		} else {
			Platform.getLog(bundle).log(new Status(IStatus.INFO, bundle.getSymbolicName(), "ModelValidator Eclipse configuration"));
			// Registration handled by extension point org.espilce.periksa.validation.registrar
		}
	}

	@Test
	public void testOK() {
		Entity entity = TestModelFactory.eINSTANCE.createEntity();
		entity.setName("ValidName");
		SpecialEntity specialEntity = SpecialFactory.eINSTANCE.createSpecialEntity();
		specialEntity.setName("ValidName");
		validateModel(createResource(entity, specialEntity));
		
		assertNoErrorsOrWarnings(entity);
		assertNoErrorsOrWarnings(specialEntity);
	}

	@Test
	public void testErrorOnBaseValidator() {
		doTestNameLengthMinimal3(TestModelFactory.eINSTANCE.createEntity());
	}

	@Test
	public void testErrorViaComposedChecksOnBaseValidator() {
		doTestNameLengthMinimal3(SpecialFactory.eINSTANCE.createSpecialEntity());
	}

	@Test
	public void testWarningViaReflectiveValidatorExtension() {
		doTestNameStartsWithCapital(TestModelFactory.eINSTANCE.createEntity());
	}

	@Test
	public void testWarningViaComposedChecks() {
		doTestNameStartsWithCapital(SpecialFactory.eINSTANCE.createSpecialEntity());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testException() {
		Entity entity = TestModelFactory.eINSTANCE.createEntity();
		entity.setName("ThrowException");
		validateModel(entity);
	}

	@Test
	public void testPackageIsolation() {
		Entity entity = TestModelFactory.eINSTANCE.createEntity();
		entity.setName("SpecialName");
		SpecialEntity specialEntity = SpecialFactory.eINSTANCE.createSpecialEntity();
		specialEntity.setName("SpecialName");
		validateModel(createResource(entity, specialEntity));
		
		assertNoErrorsOrWarnings(entity);
		assertWarningPresent("'SpecialName' should not be used as name", specialEntity);
	}

	private void doTestNameLengthMinimal3(Entity entity) {
		entity.setName("E");
		validateModel(entity);
		assertErrorPresent("Name should contain at least 3 characters", entity, TestModelPackage.Literals.ENTITY__NAME,
				"code");
		assertErrorPresent("Name should contain at least 3 characters", entity, TestModelPackage.Literals.ENTITY__NAME);
		assertErrorPresent("Name should contain at least 3 characters", entity, "code");
		assertErrorPresent("Name should contain at least 3 characters", entity);
		assertErrorPresent(entity, "code");
		assertErrorPresent(entity, TestModelPackage.Literals.ENTITY__NAME, "code");
	}

	private void doTestNameStartsWithCapital(Entity entity) {
		entity.setName("noCapital");
		validateModel(entity);
		assertWarningPresent("Name should start with a capital", entity, TestModelPackage.Literals.ENTITY__NAME);
		assertWarningPresent("Name should start with a capital", entity);
		assertWarningPresent(entity, null);
	}
}
