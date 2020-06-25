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
import org.espilce.periksa.test.testModel.TestModelPackage
import org.espilce.periksa.validation.Check
import org.espilce.periksa.validation.ContextAwareDeclarativeValidator

class ModelValidatorBase extends ContextAwareDeclarativeValidator {
    
    @Check
    def void checkNameContainsAtLeast3Chars(Entity entity) {
        if (entity.name.length < 3) {
            error("Name should contain at least 3 characters", 
                TestModelPackage.Literals.ENTITY__NAME, "code", "data")
        }
    }
}
