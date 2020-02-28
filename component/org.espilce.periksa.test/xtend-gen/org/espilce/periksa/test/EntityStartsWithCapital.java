package org.espilce.periksa.test;

import org.eclipse.xtext.xbase.lib.Extension;
import org.espilce.periksa.test.testModel.Entity;
import org.espilce.periksa.test.testModel.TestModelPackage;
import org.espilce.periksa.validation.Check;
import org.espilce.periksa.validation.CheckContext;

@SuppressWarnings("all")
public final class EntityStartsWithCapital {
  private EntityStartsWithCapital() {
  }
  
  @Check
  public static void checkNameStartsWithCapital(final Entity entity, @Extension final CheckContext context) {
    boolean _isUpperCase = Character.isUpperCase(entity.getName().charAt(0));
    boolean _not = (!_isUpperCase);
    if (_not) {
      context.getReport().warning("Name should start with a capital", TestModelPackage.Literals.ENTITY__NAME);
    }
  }
}
