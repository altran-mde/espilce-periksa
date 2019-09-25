package org.espilce.periksa.test

import java.util.List
import org.espilce.periksa.test.testModel.Entity
import org.espilce.periksa.test.testModel.TestModelPackage
import org.espilce.periksa.validation.Check
import org.eclipse.emf.ecore.EPackage
import org.espilce.periksa.validation.AbstractPeriksaValidator

class ModelValidator extends AbstractPeriksaValidator {
    
    override List<EPackage> getEPackages() {
        newArrayList(TestModelPackage.eINSTANCE);
    }
    
    @Check
    def void checkNameContainsAtLeast3Chars(Entity entity) {
        if (entity.name.length < 3) {
            error("Name should contain at least 3 characters", 
                TestModelPackage.Literals.ENTITY__NAME, "code", "data")
        }
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