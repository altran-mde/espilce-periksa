package org.espilce.periksa.validation.internal;

import java.util.LinkedHashSet;

import org.eclipse.emf.ecore.EValidator;
import org.espilce.periksa.validation.ECompositeValidator;

public final class CompositeValidator implements ECompositeValidator {
	private final LinkedHashSet<EValidator> validators = new LinkedHashSet<>(4);

	@Override
	public Iterable<EValidator> getEValidators() {
		return validators;
	}

	@Override
	public boolean addEValidator(EValidator validator) {
		if (this == validator) {
			return false;
		}
		if (validator instanceof ECompositeValidator) {
			boolean result = false;
			for (EValidator v : ((ECompositeValidator) validator).getEValidators()) {
				result |= validators.add(v);
			}
			return result;
		}
		return validators.add(validator);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((validators == null) ? 0 : validators.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CompositeValidator other = (CompositeValidator) obj;
		if (validators == null) {
			if (other.validators != null)
				return false;
		} else if (!validators.equals(other.validators))
			return false;
		return true;
	}
}
