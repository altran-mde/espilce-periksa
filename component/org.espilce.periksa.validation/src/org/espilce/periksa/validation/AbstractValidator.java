/* Copied from https://github.com/eclipse/xtext-core/blob/v2.18.0/org.eclipse.xtext/src/
 * org/eclipse/xtext/validation/AbstractInjectableValidator.java
 */
/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.espilce.periksa.validation;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EValidator;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 * @author Jan Koehnlein 
 */
public abstract class AbstractValidator implements EValidator {
	
	public static final String ISSUE_SEVERITIES = AbstractValidator.class.getCanonicalName() + ".issueSeverities";

	public void register() {
		EValidatorRegistrar registrar = new EValidatorRegistrar();
		Collection<EPackage> packages = new LinkedHashSet<EPackage>(getEPackages());
		if (packages.size()==0) {
			throw new IllegalStateException("No EPackages were registered for the validator "+getClass().getName()+" please override and implement getEPackages().");
		}
		for (EPackage ePackage : packages) {
			registrar.register(ePackage, this);
		}
	}

	protected abstract List<EPackage> getEPackages();

	@Override
	public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return true;
	}

	@Override
	final public boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate(eObject.eClass(), eObject, diagnostics, context);
	}

	@Override
	final public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return internalValidate(eObject.eClass(), eObject, diagnostics, context);
	}

	protected abstract boolean internalValidate(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context);
				
}
