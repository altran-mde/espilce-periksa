package org.espilce.periksa.test;

import org.espilce.periksa.test.testModel.Entity;
import org.espilce.periksa.test.testModel.TestModelPackage;
import org.espilce.periksa.validation.Check;
import org.espilce.periksa.validation.ContextAwareDeclarativeValidator;

@SuppressWarnings("all")
public class ModelValidatorBase extends ContextAwareDeclarativeValidator {
  @Check
  public void checkNameContainsAtLeast3Chars(final Entity entity) {
    int _length = entity.getName().length();
    boolean _lessThan = (_length < 3);
    if (_lessThan) {
      this.error("Name should contain at least 3 characters", 
        TestModelPackage.Literals.ENTITY__NAME, "code", "data");
    }
  }
}
