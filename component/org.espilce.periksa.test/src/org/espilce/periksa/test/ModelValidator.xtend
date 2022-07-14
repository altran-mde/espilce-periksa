/*******************************************************************************
 * Copyright (C) 2020 Altran Netherlands B.V.
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.espilce.periksa.test

import java.io.IOException
import org.espilce.periksa.test.testModel.Entity
import org.espilce.periksa.validation.Check
import org.espilce.periksa.validation.CheckContext
import org.espilce.periksa.validation.CheckMethod

class ModelValidator extends ModelValidatorBase {
    
	@Check
	def void throwException(Entity entity) {
		if ("ThrowException" == entity.name) {
			throw new IOException();
		}
	}

	override protected handleCheckMethodInvocationException(Throwable throwable, CheckMethod method,
		CheckContext context) {
		if (throwable instanceof IOException) {
			throw new IllegalArgumentException(throwable);
		}
		super.handleCheckMethodInvocationException(throwable, method, context)
	}
}
