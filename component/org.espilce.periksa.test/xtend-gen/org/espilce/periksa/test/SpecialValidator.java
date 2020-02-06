package org.espilce.periksa.test;

import com.google.common.base.Objects;
import java.util.Collections;
import java.util.List;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.espilce.periksa.test.ModelValidator;
import org.espilce.periksa.test.testModel.Entity;
import org.espilce.periksa.test.testModel.TestModelPackage;
import org.espilce.periksa.test.testModel.special.SpecialPackage;
import org.espilce.periksa.validation.Check;

@SuppressWarnings("all")
public class SpecialValidator extends ModelValidator {
  @Override
  protected List<EPackage> getEPackages() {
    return Collections.<EPackage>unmodifiableList(CollectionLiterals.<EPackage>newArrayList(SpecialPackage.eINSTANCE));
  }
  
  @Check
  public void checkNameNotSpecial(final Entity entity) {
    String _name = entity.getName();
    boolean _equals = Objects.equal(_name, "SpecialName");
    if (_equals) {
      this.warning("\'SpecialName\' should not be used as name", TestModelPackage.Literals.ENTITY__NAME);
    }
  }
}
