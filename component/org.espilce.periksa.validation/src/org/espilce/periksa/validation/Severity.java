/* Copied from https://github.com/eclipse/xtext-core/blob/v2.18.0/org.eclipse.xtext/src/
 * org/eclipse/xtext/diagnostics/Severity.java
 */
/*******************************************************************************
 * Copyright (c) 2010 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.espilce.periksa.validation;

/**
 * @author Sebastian Zarnekow - Initial contribution and API
 */
public enum Severity {

	ERROR,
	WARNING,
	
	/**
	 * Severity INFO is not supported for resource diagnostics.
	 */
	INFO
	
}
