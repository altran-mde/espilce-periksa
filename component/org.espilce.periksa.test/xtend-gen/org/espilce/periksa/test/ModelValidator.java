package org.espilce.periksa.test;

import com.google.common.base.Objects;
import java.util.List;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.espilce.periksa.test.testModel.Entity;
import org.espilce.periksa.test.testModel.TestModelPackage;
import org.espilce.periksa.validation.AbstractPeriksaValidator;
import org.espilce.periksa.validation.Check;

@SuppressWarnings("all")
public class ModelValidator extends AbstractPeriksaValidator {
  @Override
  public List<EPackage> getEPackages() {
    return CollectionLiterals.<EPackage>newArrayList(TestModelPackage.eINSTANCE);
  }
  
  @Check
  public void checkNameContainsAtLeast3Chars(final Entity entity) {
    int _length = entity.getName().length();
    boolean _lessThan = (_length < 3);
    if (_lessThan) {
      this.error("Name should contain at least 3 characters", 
        TestModelPackage.Literals.ENTITY__NAME, "code", "data");
    }
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
