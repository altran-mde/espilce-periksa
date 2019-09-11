/*******************************************************************************
 * Copyright (c) 2008 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.espilce.periksa.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.util.EObjectValidator;

/**
 * @author Sven Efftinge - Initial contribution and API
 */
public class CompositeEValidator implements EValidator {

	public static final String USE_EOBJECT_VALIDATOR = "org.eclipse.xtext.validation.CompositeEValidator.USE_EOBJECT_VALIDATOR";
	
	private List<EValidatorEqualitySupport> contents;
	
	private static final Logger logger = Logger.getLogger(CompositeEValidator.class);
	
	private boolean useEObjectValidator = true;
	
	public static class EValidatorEqualitySupport {
		private EValidator delegate;

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof EValidatorEqualitySupport))
				return false;
			EValidator otherDelegate = ((EValidatorEqualitySupport) obj).getDelegate();
			if (otherDelegate.getClass().equals(getDelegate().getClass())) {
				if (delegate instanceof AbstractValidator) {
					AbstractValidator casted = (AbstractValidator) getDelegate();
					AbstractValidator otherCasted = (AbstractValidator) otherDelegate;
					if (casted.isLanguageSpecific() == otherCasted.isLanguageSpecific()) {
						if (casted.isLanguageSpecific()) {
							return StringUtils.equals(casted.getLanguageName(), otherCasted.getLanguageName());
						}
						return true;
					}
					return false;
				}
				return true;
			}
			return false;
		}

		@Override
		public int hashCode() {
			return getDelegate().getClass().getName().hashCode();
		}

		public void setDelegate(EValidator delegate) {
			this.delegate = delegate;
		}

		public EValidator getDelegate() {
			return delegate;
		}
	}

	protected void initDefaults() {
		if (isUseEObjectValidator()) {
	 		this.addValidator(new EObjectValidator(){
				@Override
				public boolean validate_EveryProxyResolves(EObject eObject, DiagnosticChain diagnostics,
						Map<Object, Object> context) {
					// don't check, we have our own implementation, which creates nicer messages
					return true;
				}
				
				@Override
				public boolean validate_NoCircularContainment(EObject eObject, DiagnosticChain diagnostics,
						Map<Object, Object> context) {
					// don't check
					return true;
				}
			});
		}
	}

	public void addValidator(EValidator validator) {
		if (this == validator)
			return;
		if (validator instanceof CompositeEValidator) {
			CompositeEValidator other = (CompositeEValidator) validator;
			for(int i = 0; i < other.getContents().size(); i++)
				addValidator(other.getContents().get(i).delegate);
		} else {
			EValidatorEqualitySupport equalitySupport = new EValidatorEqualitySupport();
			equalitySupport.setDelegate(validator);
			if (!getContents().contains(equalitySupport))
				this.getContents().add(equalitySupport);
		}
	}

	@Override
	public boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = true;
		for (int i = 0; i < getContents().size(); i++) {
			EValidatorEqualitySupport val = getContents().get(i);
			try {
				result &= val.getDelegate().validate(eObject, diagnostics, context);
			}
			catch (Throwable e) {
				logger.error("Error executing EValidator", e);
				diagnostics.add(createExceptionDiagnostic("Error executing EValidator", eObject, e));
				throw e;
			}
		}
		return result;
	}

	@Override
	public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = true;
		for (int i = 0; i < getContents().size(); i++) {
			EValidatorEqualitySupport val = getContents().get(i);
			try {
				result &= val.getDelegate().validate(eClass, eObject, diagnostics, context);
			}
			catch (Throwable e) {
				logger.error("Error executing EValidator", e);
				diagnostics.add(createExceptionDiagnostic("Error executing EValidator", eClass, e));
				throw e;
			}
		}
		return result;
	}

	@Override
	public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics, Map<Object, Object> context) {
		boolean result = true;
		for (int i = 0; i < getContents().size(); i++) {
			EValidatorEqualitySupport val = getContents().get(i);
			try {
				result &= val.getDelegate().validate(eDataType, value, diagnostics, context);
			}
			catch (Throwable e) {
				logger.error("Error executing EValidator", e);
				diagnostics.add(createExceptionDiagnostic("Error executing EValidator", eDataType, e));
				throw e;
			}
		}
		return result;
	}

	private Diagnostic createExceptionDiagnostic(String message, Object source, Throwable t) {
		return new BasicDiagnostic(Diagnostic.ERROR, source.toString(), 0, message, new Object[] { t });

	}

	public boolean isUseEObjectValidator() {
		return useEObjectValidator;
	}
	
	public void setUseEObjectValidator(boolean useEObjectValidator) {
		this.useEObjectValidator = useEObjectValidator;
	}

	public List<EValidatorEqualitySupport> getContents() {
		if (contents == null) {
			contents = new ArrayList<EValidatorEqualitySupport>(4);
			initDefaults();
		}
		return contents;
	}

	/**
	 * For testing purpose.
	 * @noreference This method is not intended to be referenced by clients.
	 * @nooverride This method is not intended to be re-implemented or extended by clients.
	 * @since 2.4
	 */
	public CompositeEValidator getCopyAndClearContents() {
		CompositeEValidator result = new CompositeEValidator();
		result.useEObjectValidator = this.useEObjectValidator;
		if (this.contents != null) {
			result.contents = new ArrayList<>(this.contents);
			this.contents = null;
		}
		return result;
	}
}
