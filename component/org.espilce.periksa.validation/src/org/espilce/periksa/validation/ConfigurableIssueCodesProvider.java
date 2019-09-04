/*******************************************************************************
 * Copyright (c) 2012, 2017 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.espilce.periksa.validation;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.espilce.periksa.preferences.PreferenceKey;
import org.espilce.periksa.util.IAcceptor;

/**
 * @author Sven Efftinge - Initial contribution and API
 * @since 2.4
 */
public class ConfigurableIssueCodesProvider {
	
	/**
	 * IssueCode to control DerivedResourceMarkerCopier
	 */
	// @see org.eclipse.xtext.builder.smap.DerivedResourceMarkerCopier
	public static final String COPY_JAVA_PROBLEMS = "org.eclipse.xtext.builder.copyJavaProblems";
	
	private Map<String, PreferenceKey> issueCodes;

	public ConfigurableIssueCodesProvider() {
		final Map<String, PreferenceKey> map = new LinkedHashMap<>();
		initialize(new IAcceptor<PreferenceKey>() {

			@Override
			public void accept(PreferenceKey prefKey) {
				map.put(prefKey.getId(), prefKey);
			}
		});
		this.issueCodes = Collections.unmodifiableMap(map);
	}
	
	/**
	 * @since 2.14
	 */
	protected void initialize(IAcceptor<PreferenceKey> iAcceptor) {
		iAcceptor.accept(create(COPY_JAVA_PROBLEMS, SeverityConverter.SEVERITY_IGNORE));
	}
	
	/**
	 * @since 2.14
	 */
	protected final PreferenceKey create(String id, String defaultValue) {
		return new PreferenceKey(id, defaultValue);
	}
	
	/**
	 * @return all configurable issue codes.
	 */
	public Map<String, PreferenceKey> getConfigurableIssueCodes() {
		return issueCodes;
	}
	
}
