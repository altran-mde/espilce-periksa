package org.espilce.periksa.test

import java.util.List
import org.espilce.periksa.test.testModel.Entity
import org.espilce.periksa.test.testModel.TestModelPackage
import org.espilce.periksa.validation.AbstractDeclarativeValidator
import org.espilce.periksa.validation.Check
import org.eclipse.emf.ecore.EPackage

class ModelValidator extends AbstractDeclarativeValidator {
    
    override List<EPackage> getEPackages() {
        newArrayList(TestModelPackage.eINSTANCE);
    }
    
    @Check
    def void checkNameStartsWithCapital(Entity entity) {
        if (!Character.isUpperCase(entity.name.charAt(0))) {
            warning("Name should start with a capital", 
                TestModelPackage.Literals.ENTITY__NAME)
        }
    }
}