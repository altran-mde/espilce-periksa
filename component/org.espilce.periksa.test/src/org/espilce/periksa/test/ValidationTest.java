package org.espilce.periksa.test;

import org.espilce.periksa.test.testModel.Entity;
import org.espilce.periksa.test.testModel.TestModelFactory;
import org.espilce.periksa.test.testModel.TestModelPackage;
import org.espilce.periksa.test.testModel.special.SpecialEntity;
import org.espilce.periksa.test.testModel.special.SpecialFactory;
import org.espilce.periksa.testsupport.ATestValidator;
import org.junit.BeforeClass;
import org.junit.Test;

public class ValidationTest extends ATestValidator {

	@BeforeClass
	public static void registerValidator() {
		new ModelValidator().register();
		new SpecialValidator().register();
	}

	@Test
	public void testOK() {
		Entity entity = TestModelFactory.eINSTANCE.createEntity();
		entity.setName("ValidName");
		validateModel(entity);
		assertNoErrorsOrWarnings(entity);
	}

	@Test
	public void testError() {
		doTestError(TestModelFactory.eINSTANCE.createEntity());
	}

	@Test
	public void testSpecialError() {
		doTestError(SpecialFactory.eINSTANCE.createSpecialEntity());
	}

	private void doTestError(Entity entity) {
		entity.setName("E");
		validateModel(createResource(entity));
		assertErrorPresent("Name should contain at least 3 characters", entity, TestModelPackage.Literals.ENTITY__NAME,
				"code");
		assertErrorPresent("Name should contain at least 3 characters", entity, TestModelPackage.Literals.ENTITY__NAME);
		assertErrorPresent("Name should contain at least 3 characters", entity, "code");
		assertErrorPresent("Name should contain at least 3 characters", entity);
		assertErrorPresent(entity, "code");
		assertErrorPresent(entity, TestModelPackage.Literals.ENTITY__NAME, "code");
	}

	@Test
	public void testWarning() {
		Entity entity = TestModelFactory.eINSTANCE.createEntity();
		entity.setName("noCapital");
		validateModel(createResource(entity));
		assertWarningPresent("Name should start with a capital", entity, TestModelPackage.Literals.ENTITY__NAME);
		assertWarningPresent("Name should start with a capital", entity);
		assertWarningPresent(entity, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testException() {
		Entity entity = TestModelFactory.eINSTANCE.createEntity();
		entity.setName("ThrowException");
		validateModel(createResource(entity));
	}

	@Test
	public void testSpecialWarning() {
		Entity entity = TestModelFactory.eINSTANCE.createEntity();
		entity.setName("SpecialName");
		SpecialEntity specialEntity = SpecialFactory.eINSTANCE.createSpecialEntity();
		specialEntity.setName("SpecialName");
		validateModel(createResource(entity, specialEntity));
		assertNoErrorsOrWarnings(entity);
		assertWarningPresent("'SpecialName' should not be used as name", specialEntity);
	}
}
