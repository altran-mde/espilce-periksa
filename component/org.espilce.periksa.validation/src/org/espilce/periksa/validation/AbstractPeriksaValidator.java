/* Copied from https://github.com/eclipse/xtext-core/blob/v2.18.0/org.eclipse.xtext/src/
 * org/eclipse/xtext/validation/AbstractDeclarativeValidator.java
 */
/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.espilce.periksa.validation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

public abstract class AbstractPeriksaValidator extends AbstractDeclarativeValidator {

	protected void info(String message, EStructuralFeature feature) {
		info(message, feature, ValidationMessageAcceptor.INSIGNIFICANT_INDEX, null);
	}

	protected void info(String message, EStructuralFeature feature, int index) {
		info(message, feature, index, null);
	}

	protected void info(String message, EStructuralFeature feature, int index, String code, String... issueData) {
		info(message, state.get().currentObject, feature, index, code, issueData);
	}

	protected void info(String message, EStructuralFeature feature, String code, String... issueData) {
		info(message, state.get().currentObject, feature, ValidationMessageAcceptor.INSIGNIFICANT_INDEX, code,
				issueData);
	}
	
	protected void info(String message, EObject source, EStructuralFeature feature) {
		info(message, source, feature, ValidationMessageAcceptor.INSIGNIFICANT_INDEX, null);
	}

	protected void info(String message, EObject source, EStructuralFeature feature, int index) {
		info(message, source, feature, index, null);
	}
	
	protected void info(String message, EObject source, EStructuralFeature feature, int index, String code,
			String... issueData) {
		getMessageAcceptor().acceptInfo(message, source, feature, index, code, issueData);
	}

	protected void info(String message, EObject source, EStructuralFeature feature, String code,
			String... issueData) {
		getMessageAcceptor().acceptInfo(message, source, feature, ValidationMessageAcceptor.INSIGNIFICANT_INDEX, code, issueData);
	}

	protected void warning(String message, EStructuralFeature feature) {
		warning(message, feature, ValidationMessageAcceptor.INSIGNIFICANT_INDEX, null);
	}

	protected void warning(String message, EStructuralFeature feature, int index) {
		warning(message, feature, index, null);
	}

	protected void warning(String message, EStructuralFeature feature, String code, String... issueData) {
		warning(message, state.get().currentObject, feature, ValidationMessageAcceptor.INSIGNIFICANT_INDEX, code,
				issueData);
	}

	protected void warning(String message, EStructuralFeature feature, int index, String code, String... issueData) {
		warning(message, state.get().currentObject, feature, index, code, issueData);
	}

	protected void warning(String message, EObject source, EStructuralFeature feature) {
		warning(message, source, feature, ValidationMessageAcceptor.INSIGNIFICANT_INDEX);
	}

	protected void warning(String message, EObject source, EStructuralFeature feature, int index) {
		warning(message, source, feature, index, null);
	}

	protected void warning(String message, EObject source, EStructuralFeature feature, int index, String code,
			String... issueData) {
		getMessageAcceptor().acceptWarning(message, source, feature, index, code, issueData);
	}

	protected void warning(String message, EObject source, EStructuralFeature feature, String code, String... issueData) {
		getMessageAcceptor().acceptWarning(message, source, feature, ValidationMessageAcceptor.INSIGNIFICANT_INDEX,
				code, issueData);
	}

	protected void error(String message, EStructuralFeature feature) {
		error(message, feature, ValidationMessageAcceptor.INSIGNIFICANT_INDEX, null);
	}

	protected void error(String message, EStructuralFeature feature, int index) {
		error(message, feature, index, null);
	}

	protected void error(String message, EStructuralFeature feature, String code, String... issueData) {
		error(message, feature, ValidationMessageAcceptor.INSIGNIFICANT_INDEX, code, issueData);
	}

	protected void error(String message, EStructuralFeature feature, int index, String code, String... issueData) {
		error(message, state.get().currentObject, feature, index, code, issueData);
	}

	protected void error(String message, EObject source, EStructuralFeature feature) {
		error(message, source, feature, ValidationMessageAcceptor.INSIGNIFICANT_INDEX);
	}

	protected void error(String message, EObject source, EStructuralFeature feature, int index) {
		error(message, source, feature, index, null);
	}

	protected void error(String message, EObject source, EStructuralFeature feature, String code, String... issueData) {
		getMessageAcceptor().acceptError(message, source, feature, ValidationMessageAcceptor.INSIGNIFICANT_INDEX, code,
				issueData);
	}

	protected void error(String message, EObject source, EStructuralFeature feature, int index, String code,
			String... issueData) {
		getMessageAcceptor().acceptError(message, source, feature, index, code, issueData);
	}

}
