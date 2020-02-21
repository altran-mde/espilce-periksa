package org.espilce.periksa.test;

import com.google.common.base.Objects;
import java.util.List;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.espilce.periksa.test.ModelValidatorBase;
import org.espilce.periksa.test.testModel.Entity;
import org.espilce.periksa.test.testModel.TestModelPackage;
import org.espilce.periksa.validation.Check;

@SuppressWarnings("all")
public class ModelValidator extends ModelValidatorBase {
  @Override
  protected List<EPackage> getEPackages() {
    return CollectionLiterals.<EPackage>newArrayList(TestModelPackage.eINSTANCE);
  }
  
  @Check
  public void checkNameStartsWithCapital(final Entity entity) {
    boolean _isUpperCase = Character.isUpperCase(entity.getName().charAt(0));
    boolean _not = (!_isUpperCase);
    if (_not) {
      this.warning("Name should start with a capital", 
        TestModelPackage.Literals.ENTITY__NAME);
    }
  }
  
  @Check
  public void throwException(final Entity entity) {
    String _name = entity.getName();
    boolean _equals = Objects.equal("ThrowException", _name);
    if (_equals) {
      throw new IllegalArgumentException();
    }
  }
}
