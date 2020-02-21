package org.espilce.periksa.test

import org.espilce.periksa.test.testModel.Entity
import org.espilce.periksa.test.testModel.TestModelPackage
import org.espilce.periksa.validation.Check

class ModelValidator extends ModelValidatorBase {
    
    override protected getEPackages() {
        newArrayList(TestModelPackage.eINSTANCE)
    }
    
    @Check
    def void checkNameStartsWithCapital(Entity entity) {
        if (!Character.isUpperCase(entity.name.charAt(0))) {
            warning("Name should start with a capital", 
                TestModelPackage.Literals.ENTITY__NAME)
        }
    }

    @Check
    def void throwException(Entity entity) {
        if ("ThrowException" == entity.name) {
            throw new IllegalArgumentException();
        }
    }
}
