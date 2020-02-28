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
