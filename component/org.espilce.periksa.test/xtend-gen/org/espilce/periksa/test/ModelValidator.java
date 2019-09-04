package org.espilce.periksa.test;

import java.util.List;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.espilce.periksa.test.testModel.Entity;
import org.espilce.periksa.test.testModel.TestModelPackage;
import org.espilce.periksa.validation.AbstractDeclarativeValidator;
import org.espilce.periksa.validation.Check;

@SuppressWarnings("all")
public class ModelValidator extends AbstractDeclarativeValidator {
  @Override
  public List<EPackage> getEPackages() {
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
}
