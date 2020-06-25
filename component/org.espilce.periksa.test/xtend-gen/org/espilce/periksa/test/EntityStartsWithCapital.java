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
