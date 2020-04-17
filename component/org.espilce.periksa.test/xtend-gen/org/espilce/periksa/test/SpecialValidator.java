package org.espilce.periksa.test;

import com.google.common.base.Objects;
import org.eclipse.xtext.xbase.lib.Extension;
import org.espilce.periksa.test.EntityStartsWithCapital;
import org.espilce.periksa.test.ModelValidator;
import org.espilce.periksa.test.testModel.Entity;
import org.espilce.periksa.test.testModel.TestModelPackage;
import org.espilce.periksa.validation.Check;
import org.espilce.periksa.validation.CheckContext;
import org.espilce.periksa.validation.ComposedChecks;
import org.espilce.periksa.validation.DeclarativeValidator;

@ComposedChecks({ ModelValidator.class, EntityStartsWithCapital.class })
@SuppressWarnings("all")
public class SpecialValidator extends DeclarativeValidator {
  @Check
  public void checkNameNotSpecial(final Entity entity, @Extension final CheckContext context) {
    String _name = entity.getName();
    boolean _equals = Objects.equal(_name, "SpecialName");
    if (_equals) {
      context.getReport().warning("\'SpecialName\' should not be used as name", TestModelPackage.Literals.ENTITY__NAME);
    }
  }
}
