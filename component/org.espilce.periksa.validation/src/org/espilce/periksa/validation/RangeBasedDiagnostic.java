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

import org.eclipse.emf.ecore.EObject;

/**
 * A validation diagnostic that is associated with a range.
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public class RangeBasedDiagnostic extends AbstractValidationDiagnostic {

	private final int offset;
	private final int length;

	protected RangeBasedDiagnostic(int severity, String message, EObject source, int offset, int length, String issueCode,
			String[] issueData) {
		super(severity, message, source, issueCode, issueData);
		this.offset = offset;
		this.length = length;
	}

	@Override
	public List<?> getData() {
		if (getIssueData() == null)
	    	return Collections.unmodifiableList(Arrays.asList(getSourceEObject(), offset, length));
		else
	    	return Collections.unmodifiableList(Arrays.asList(getSourceEObject(), offset, length, getIssueData()));
	}
	
	public int getOffset() {
		return offset;
	}
	
	public int getLength() {
		return length;
	}

	@Override
	public String getSource() {
		return super.getSource() + "@[" + offset + ":" + length + "]";
	}
	
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder(super.toString());
		result.append("@[").append(offset).append(":").append(length).append("]");
		return result.toString();
	}
}
