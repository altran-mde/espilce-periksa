/**
 * Copyright (C) 2020 Altran Netherlands B.V.
 * 
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 * 
 * SPDX-License-Identifier: EPL-2.0
 */
package org.espilce.periksa.test;

import com.google.common.base.Objects;
import org.eclipse.xtext.xbase.lib.Extension;
import org.espilce.periksa.test.ModelValidations;
import org.espilce.periksa.test.ModelValidator;
import org.espilce.periksa.test.testModel.Entity;
import org.espilce.periksa.test.testModel.TestModelPackage;
import org.espilce.periksa.validation.Check;
import org.espilce.periksa.validation.CheckContext;
import org.espilce.periksa.validation.ComposedChecks;
import org.espilce.periksa.validation.DeclarativeValidator;

@ComposedChecks({ ModelValidator.class, ModelValidations.class })
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
