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

import org.espilce.periksa.test.testModel.Entity
import org.espilce.periksa.test.testModel.EntityContainer
import org.espilce.periksa.test.testModel.TestModelPackage
import org.espilce.periksa.test.testModel.special.SpecialPackage
import org.espilce.periksa.validation.Check
import org.espilce.periksa.validation.CheckContext
import org.espilce.periksa.validation.ValidationLibrary

final class ModelValidations {
	private new() {
		// Empty for utility classes
	}
	
	@Check
	static def void checkNameStartsWithCapital(Entity entity, extension CheckContext context) {
		if (!Character.isUpperCase(entity.name.charAt(0))) {
			report.warning("Name should start with a capital", TestModelPackage.Literals.ENTITY__NAME)
		}
	}

    
    @Check
    static def void checkDuplicateDescription(EntityContainer entityContainer, CheckContext context) {
    	ValidationLibrary::checkDuplicateValue(entityContainer.entities, SpecialPackage.Literals.SPECIAL_ENTITY__DESCRIPTION, context)
    }
}