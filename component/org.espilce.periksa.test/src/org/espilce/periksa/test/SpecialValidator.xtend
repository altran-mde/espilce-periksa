package org.espilce.periksa.test

import org.espilce.periksa.test.testModel.Entity
import org.espilce.periksa.test.testModel.TestModelPackage
import org.espilce.periksa.test.testModel.special.SpecialPackage
import org.espilce.periksa.validation.Check
import org.espilce.periksa.validation.CheckContext
import org.espilce.periksa.validation.ComposedChecks
import org.espilce.periksa.validation.DeclarativeValidator
import org.espilce.periksa.validation.ERegistrableValidator

@ComposedChecks(ModelValidator, EntityStartsWithCapital)
class SpecialValidator extends DeclarativeValidator implements ERegistrableValidator {

    override getEPackages() {
        #{SpecialPackage.eINSTANCE}
    }
    
    @Check
    def void checkNameNotSpecial(Entity entity, extension CheckContext context) {
        // Note that this check will be performed for all subclasses of Entity in the SpecialPackage (i.e. SpecialEntity)
        // It will NOT be performed for an Entity instance (in the same model instance) as the Entity EClass does not belong to the SpecialPackage
        if (entity.name == 'SpecialName') {
            report.warning("'SpecialName' should not be used as name", TestModelPackage.Literals.ENTITY__NAME)
        }
    }
}
