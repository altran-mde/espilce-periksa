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
import java.io.IOException;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.espilce.periksa.test.ModelValidatorBase;
import org.espilce.periksa.test.testModel.Entity;
import org.espilce.periksa.validation.Check;
import org.espilce.periksa.validation.CheckContext;
import org.espilce.periksa.validation.CheckMethod;

@SuppressWarnings("all")
public class ModelValidator extends ModelValidatorBase {
  @Check
  public void throwException(final Entity entity) {
    try {
      String _name = entity.getName();
      boolean _equals = Objects.equal("ThrowException", _name);
      if (_equals) {
        throw new IOException();
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  @Override
  protected void handleCheckMethodInvocationException(final Throwable throwable, final CheckMethod method, final CheckContext context) {
    if ((throwable instanceof IOException)) {
      throw new IllegalArgumentException(throwable);
    }
    super.handleCheckMethodInvocationException(throwable, method, context);
  }
}
