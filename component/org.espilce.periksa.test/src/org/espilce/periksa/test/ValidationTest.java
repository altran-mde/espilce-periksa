package org.espilce.periksa.test;

import java.util.logging.Logger;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.espilce.periksa.test.testModel.Entity;
import org.espilce.periksa.test.testModel.TestModelFactory;
import org.espilce.periksa.test.testModel.TestModelPackage;
import org.espilce.periksa.testsupport.ATestValidator;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

public class ValidationTest extends ATestValidator {

	@BeforeClass
	public static void registerValidator() {
		// Check if run as JUnit Test (requires bootstrap) or JUnit Plug-in Test
		final Bundle bundle = FrameworkUtil.getBundle(ValidationTest.class);
		if (null == bundle) {
			Logger.getLogger(ValidationTest.class.getName()).info("ModelValidator Standalone configuration");
			new ModelValidator().register();
		} else {
			Platform.getLog(bundle).log(new Status(IStatus.INFO, bundle.getSymbolicName(), "ModelValidator Eclipse configuration"));
			// Registration handled by extension point org.espilce.periksa.validation.registrar
		}
	}

	@Test
	public void testError() {
		Entity entity = TestModelFactory.eINSTANCE.createEntity();
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

	private Resource createResource(EObject eObject) {
		return new ResourceImpl() {
			@Override
			public EList<EObject> getContents() {
				EList<EObject> result = new BasicEList<>();
				result.add(eObject);
				return result;
			}

			@Override
			public ResourceSet getResourceSet() {
				Resource resource = this;
				ResourceSet resourceSet = new ResourceSetImpl() {
					public EList<Resource> getResources() {
						EList<Resource> result = new BasicEList<>();
						result.add(resource);
						return result;
					}
				};
				return resourceSet;
			}
		};
	}
}
