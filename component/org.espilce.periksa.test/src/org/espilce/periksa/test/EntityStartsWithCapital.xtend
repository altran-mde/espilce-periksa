package org.espilce.periksa.test

import org.espilce.periksa.test.testModel.Entity
import org.espilce.periksa.test.testModel.TestModelPackage
import org.espilce.periksa.validation.Check
import org.espilce.periksa.validation.CheckContext

final class EntityStartsWithCapital {
	private new() {
		// Empty for utility classes
	}
	
	@Check
	static def void checkNameStartsWithCapital(Entity entity, extension CheckContext context) {
		if (!Character.isUpperCase(entity.name.charAt(0))) {
			report.warning("Name should start with a capital", TestModelPackage.Literals.ENTITY__NAME)
		}
	}
}