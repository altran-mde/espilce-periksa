/* Copied from https://github.com/eclipse/xtext-core/blob/v2.18.0/org.eclipse.xtext/src/
 * org/eclipse/xtext/validation/AbstractValidationDiagnostic.java
 */
/*******************************************************************************
 * Copyright (c) 2011 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.espilce.periksa.validation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.espilce.commons.emf.textrenderer.EmfStringRenderer;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public abstract class AbstractValidationDiagnostic implements Diagnostic {

	private final String message;
	private final EObject source;
	private final int severity;
	private final String issueCode;
	private final String[] issueData;
	
	/**
	 * @param issueData optional user data. May not contain <code>null</code> entries.
	 */
	protected AbstractValidationDiagnostic(int severity, String message, EObject source, String issueCode, String... issueData) {
		if (Arrays.asList(issueData).contains(null)) {
			throw new NullPointerException("issueData may not contain null");
		}
		this.source = source;
		this.severity = severity;
		this.message = message;
		this.issueCode = issueCode;
		this.issueData = issueData;
	}
	
	@Override
	public List<Diagnostic> getChildren() {
		return Collections.emptyList();
	}

	// it turns out, the EMF Diagnostic works well with 0,
	// look at getIssueCode() to retrieve the string-based
	// code
	@Override
	public int getCode() {
		return 0;
	}
	
	@Override
	public String getSource() {
		return source == null ? "" : source.toString();
	}
	
	public EObject getSourceEObject() {
		return source;
	}
	
	@Override
	public Throwable getException() {
		return null;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public int getSeverity() {
		return severity;
	}
	
	public String getIssueCode() {
		return issueCode;
	}
	
	public String[] getIssueData() {
		return issueData;
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append("Diagnostic ");
		result.append(severityToStr(severity));
		if (issueCode != null) {
			result.append(" code=");
			result.append(issueCode);
		}
		result.append(" \"");
		result.append(message);
		result.append("\"");
		if (getSourceEObject() != null) {
			result.append(" at ");
			result.append(new EmfStringRenderer(getSourceEObject()).render());
		}
		return result.toString();
	}
	
	public static String severityToStr(int severity) {
		switch (severity) {
			case OK:
				return "OK";
			case INFO:
				return "INFO";
			case WARNING:
				return "WARNING";
			case ERROR:
				return "ERROR";
			case CANCEL:
				return "CANCEL";
			default:
				return Integer.toHexString(severity);
		}
	}
}
