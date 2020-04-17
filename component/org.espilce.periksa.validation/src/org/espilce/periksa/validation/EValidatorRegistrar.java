/* Copied from https://github.com/eclipse/xtext-core/blob/v2.18.0/org.eclipse.xtext/src/
 * org/eclipse/xtext/validation/EValidatorRegistrar.java
 */
/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.espilce.periksa.validation;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EValidator;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class EValidatorRegistrar {

	private final EValidator.Registry registry;
	
	public EValidatorRegistrar() {
		 this.registry = EValidator.Registry.INSTANCE;
	}
	
	public EValidatorRegistrar(EValidator.Registry registry) {
		this.registry = registry;
	}

	public void register(EPackage ePackage, EValidator registerMe) {
		EValidator validator = registry.getEValidator(ePackage);
		if (validator == null) {
			validator = new CompositeEValidator();
		}
		else if (!(validator instanceof CompositeEValidator)) {
			CompositeEValidator newValidator = new CompositeEValidator();
			newValidator.addValidator(validator);
			validator = newValidator;
		}
		((CompositeEValidator) validator).addValidator(registerMe);
		registry.put(ePackage, validator);
	}

	public void unregister(EPackage ePackage, EValidator unregisterMe) {
		EValidator validator = registry.getEValidator(ePackage);
		if (validator == unregisterMe) {
			registry.remove(ePackage);
		}
		else if (validator instanceof CompositeEValidator) {
			((CompositeEValidator) validator).removeValidator(unregisterMe);
		}
	}

	public void unregister(EPackage ePackage) {
		registry.remove(ePackage);
	}
}
