package org.espilce.periksa.validation.registry;

import java.util.Objects;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EValidator;
import org.espilce.commons.emf.registry.EPackageRegistryObserver;
import org.espilce.periksa.validation.EValidatorRegistrar;

public class RegistrarExtensionPointProcessor implements EPackageRegistryObserver {
	private static final String EXTENSION_POINT = PluginActivator.PLUGIN_ID + ".registrar";

	private static final String ELEMENT_E_VALIDATOR = "EValidator";
	private static final String ATTR_CLASS = "class";
	private static final String ATTR_URI = "uri";

	@Override
	public void ePackageRegistered(String uri, EPackage ePackage) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		IExtensionPoint point = registry.getExtensionPoint(EXTENSION_POINT);
		if (point == null) {
			return;
		}
		for (IExtension extension : point.getExtensions()) {
			for (IConfigurationElement validatorElement : extension.getConfigurationElements()) {
				if (!validatorElement.isValid()) {
					continue;
				}
				if (!ELEMENT_E_VALIDATOR.equals(validatorElement.getName())) {
					PluginActivator.getLog().log(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID,
							"Unknown validator type: " + validatorElement.getName()));
					continue;
				}
				if (!isValidatingEPackage(validatorElement, uri)) {
					continue;
				}
				try {
					EValidator validator = EValidator.class
							.cast(validatorElement.createExecutableExtension(ATTR_CLASS));
					EValidatorRegistrar registrar = new EValidatorRegistrar();
					registrar.register(ePackage, validator);
				} catch (CoreException e) {
					PluginActivator.getLog().log(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID,
							"Failed to register EValidator: " + e.getMessage(), e));
				}
			}
		}
	}

	private boolean isValidatingEPackage(IConfigurationElement validatorElement, String uri) {
		for (IConfigurationElement packageElement : validatorElement.getChildren("EPackage")) {
			if (Objects.equals(uri, packageElement.getAttribute(ATTR_URI))) {
				return true;
			}
		}
		return false;
	}

	@Override
	public void ePackageUnregistered(String uri, EPackage ePackage) {
		EValidatorRegistrar registrar = new EValidatorRegistrar();
		registrar.unregister(ePackage);
	}
}
