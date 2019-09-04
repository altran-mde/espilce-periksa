/*******************************************************************************
 * Copyright (c) 2015 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package org.espilce.periksa.preferences

import java.util.Map
import java.util.HashMap
import org.espilce.periksa.util.EmfAdaptable

@EmfAdaptable class PreferenceValuesByLanguage {
	val Map<String, IPreferenceValues> preferencesByLanguage = new HashMap<String, IPreferenceValues>()

	def get(String languageId) {
		preferencesByLanguage.get(languageId)
	}

	def put(String languageId, IPreferenceValues values) {
		preferencesByLanguage.put(languageId, values)
	}
}