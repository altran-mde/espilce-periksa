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

import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * @author itemis AG - Initial contribution and API
 * @author Altran Netherlands B.V. - Refactoring including API updates
 */
public class CheckContext {
	private final DiagnosticChain diagnostics;
	private final Map<Object, Object> validationContext;
	private final EObject eObject;

	private boolean valid = true;

	private final ContextAwareValidationReporter report = new ContextAwareValidationReporter() {
		@Override
		public void error(String message, EStructuralFeature feature, int index, String code, String... issueData) {
			error(message, getEObject(), feature, index, code, issueData);
		}

		@Override
		public void error(String message, EObject object, EStructuralFeature feature, int index, String code,
				String... issueData) {
			checkIsFromCurrentlyCheckedResource(object);
			reportDiagnostic(createDiagnostic(Diagnostic.ERROR, message, object, feature, index, code, issueData));
		}

		@Override
		public void warning(String message, EStructuralFeature feature, int index, String code, String... issueData) {
			warning(message, getEObject(), feature, index, code, issueData);
		}

		@Override
		public void warning(String message, EObject object, EStructuralFeature feature, int index, String code,
				String... issueData) {
			checkIsFromCurrentlyCheckedResource(object);
			reportDiagnostic(createDiagnostic(Diagnostic.WARNING, message, object, feature, index, code, issueData));
		}

		@Override
		public void info(String message, EStructuralFeature feature, int index, String code, String... issueData) {
			info(message, getEObject(), feature, index, code, issueData);
		}

		@Override
		public void info(String message, EObject object, EStructuralFeature feature, int index, String code,
				String... issueData) {
			checkIsFromCurrentlyCheckedResource(object);
			reportDiagnostic(createDiagnostic(Diagnostic.INFO, message, object, feature, index, code, issueData));
		}

		private void checkIsFromCurrentlyCheckedResource(EObject object) {
			final EObject currentObject = getEObject();
			if (object == null || currentObject == null || object == currentObject) {
				return;
			}
			final Resource resource = object.eResource();
			final Resource currentResource = currentObject.eResource();
			if (resource == currentResource) {
				return;
			}
			URI uri = resource == null ? null : resource.getURI();
			URI currentUri = currentResource == null ? null : currentResource.getURI();
			throw new IllegalArgumentException(
					"You can only add issues for EObjects contained in the currently validated resource '" + currentUri
							+ "'. But the given EObject was contained in '" + uri + "'");
		}
	};

	protected CheckContext(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> validationContext) {
		this.eObject = eObject;
		this.diagnostics = diagnostics;
		this.validationContext = validationContext;
	}

	public ContextAwareValidationReporter getReport() {
		return report;
	}

	public Map<Object, Object> getValidationContext() {
		return validationContext;
	}

	protected EObject getEObject() {
		return eObject;
	}

	protected boolean isValid() {
		return valid;
	}

	protected Diagnostic createDiagnostic(int severity, String message, EObject object, EStructuralFeature feature,
			int index, String code, String... issueData) {
		return new FeatureBasedDiagnostic(severity, message, object, feature, index, code, issueData);
	}
	
	protected void reportDiagnostic(Diagnostic diagnostic) {
		diagnostics.add(diagnostic);
		if (diagnostic.getSeverity() == Diagnostic.ERROR) {
			valid = false;
		}
	}
}
