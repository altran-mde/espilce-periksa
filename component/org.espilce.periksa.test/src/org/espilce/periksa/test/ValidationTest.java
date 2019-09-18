package org.espilce.periksa.test;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.espilce.commons.emf.testsupport.ATestValidator;
import org.espilce.periksa.test.testModel.Entity;
import org.espilce.periksa.test.testModel.TestModelFactory;
import org.junit.BeforeClass;
import org.junit.Test;

public class ValidationTest extends ATestValidator {
	
	@BeforeClass
	public static void registerValidator() {
		new ModelValidator().register();
	}
	
	@Test
	public void testError() {	
		Entity entity = TestModelFactory.eINSTANCE.createEntity();
		entity.setName("E");
		validateModel(createResource(entity));
		assertErrorPresent(entity, null);
	}

	@Test
	public void testWarning() {	
		Entity entity = TestModelFactory.eINSTANCE.createEntity();
		entity.setName("noCapital");
		validateModel(createResource(entity));
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
				EList<EObject> result = new BasicEList<EObject>();
				result.add(eObject);
				return result;
			}
			@Override
			public ResourceSet getResourceSet() {
				Resource resource = this;
				ResourceSet resourceSet = new ResourceSetImpl() {
					public EList<Resource> getResources() {
						EList<Resource> result = new BasicEList<Resource>();
						result.add(resource);
						return result;
					}
				};
				return resourceSet;
			}
		};
	}

}
