package org.espilce.periksa.validation;

import java.util.LinkedHashSet;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EValidator;

/**
 * @author yblankenstein I think we should remove this class from Periksa
 */
@Deprecated
public interface ERegistrableValidator extends EValidator {
	/**
	 * Returns the {@link EPackage}s for which this {@link EValidator} needs to be
	 * registered.
	 * 
	 * @return an iterable of EPackages, the iterable must not be <tt>null</tt> or empty.
	 */
	Iterable<EPackage> getEPackages();

	/**
	 * Registers this {@link EValidator} for all {@link #getEPackages()} in the
	 * default {@link EValidatorRegistrar}.
	 */
	default void register() {
		EValidatorRegistrar registrar = new EValidatorRegistrar();
		LinkedHashSet<EPackage> packages = new LinkedHashSet<>(4);
		getEPackages().forEach(packages::add);
		if (packages.isEmpty()) {
			throw new IllegalStateException("No EPackages are registered for the validator " + getClass().getName());
		}
		for (EPackage ePackage : packages) {
			registrar.register(ePackage, this);
		}
	}
}
