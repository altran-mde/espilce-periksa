package org.espilce.periksa.validation;

import java.lang.reflect.Method;
import java.util.Map;

import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EObject;

public class State {
	public DiagnosticChain chain = null;
	public EObject currentObject = null;
	public Method currentMethod = null;
	public boolean hasErrors = false;
	public Map<Object, Object> context;
	
	public static class StateAccess {

		private AbstractDeclarativeValidator validator;

		protected StateAccess(AbstractDeclarativeValidator validator) {
			this.validator = validator;
		}

		public State getState() {
			State result = validator.state.get();
			if (result == null) {
				result = new State();
				validator.state.set(result);
			}
			return result;
		}

	}
}