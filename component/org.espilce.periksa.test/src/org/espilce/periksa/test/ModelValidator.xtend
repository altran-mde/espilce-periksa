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
