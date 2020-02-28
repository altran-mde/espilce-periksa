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

import java.util.function.Supplier;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EValidator;
import org.espilce.periksa.validation.internal.CompositeValidator;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class EValidatorRegistrar {

	private final EValidator.Registry registry;

	private Supplier<ECompositeValidator> compositeValidatorSupplier = CompositeValidator::new;
	
	/**
	 * Registers {@link EValidator}s in the {@link EValidator.Registry#INSTANCE}.
	 */
	public EValidatorRegistrar() {
		this.registry = EValidator.Registry.INSTANCE;
	}

	/**
	 * Registers {@link EValidator}s in the <tt>registry</tt>.
	 */
	public EValidatorRegistrar(EValidator.Registry registry) {
		this.registry = registry;
	}

	public void setCompositeValidatorSupplier(Supplier<ECompositeValidator> compositeValidatorSupplier) {
		this.compositeValidatorSupplier = compositeValidatorSupplier;
	}
	
	protected Supplier<ECompositeValidator> getCompositeValidatorSupplier() {
		return compositeValidatorSupplier;
	}
	
	/**
	 * Registers an {@link EValidator} for an {@link EPackage}.
	 * 
	 * @param ePackage   the package for which <tt>registerMe</tt> should be
	 *                   registered.
	 * @param registerMe the validator to be registered
	 */
	public void register(EPackage ePackage, EValidator registerMe) {
		final EValidator validator = registry.getEValidator(ePackage);
		if (validator == null) {
			registry.put(ePackage, registerMe);
		} else if (validator instanceof ECompositeValidator) {
			((ECompositeValidator) validator).addEValidator(registerMe);
		} else {
			ECompositeValidator compositeValidator = getCompositeValidatorSupplier().get();
			compositeValidator.addEValidator(validator);
			compositeValidator.addEValidator(registerMe);
			registry.put(ePackage, compositeValidator);
		}
	}
}
