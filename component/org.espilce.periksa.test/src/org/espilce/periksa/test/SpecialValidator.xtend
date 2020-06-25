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
import org.espilce.periksa.validation.CheckContext
import org.espilce.periksa.validation.ComposedChecks
import org.espilce.periksa.validation.DeclarativeValidator

@ComposedChecks(ModelValidator, EntityStartsWithCapital)
class SpecialValidator extends DeclarativeValidator {
    
    @Check
    def void checkNameNotSpecial(Entity entity, extension CheckContext context) {
        // Note that this check will be performed for all subclasses of Entity in the SpecialPackage (i.e. SpecialEntity)
        // It will NOT be performed for an Entity instance (in the same model instance) as the Entity EClass does not belong to the SpecialPackage
        if (entity.name == 'SpecialName') {
            report.warning("'SpecialName' should not be used as name", TestModelPackage.Literals.ENTITY__NAME)
        }
    }
}
