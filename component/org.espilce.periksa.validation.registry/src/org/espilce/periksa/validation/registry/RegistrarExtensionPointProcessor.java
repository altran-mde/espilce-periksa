package org.espilce.periksa.validation.registry;

import java.util.Objects;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.spi.RegistryContributor;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EValidator;
import org.espilce.commons.emf.registry.EPackageRegistryObserver;
import org.espilce.periksa.validation.DeclarativeValidator;
import org.espilce.periksa.validation.EValidatorRegistrar;
import org.osgi.framework.Bundle;

public class RegistrarExtensionPointProcessor implements EPackageRegistryObserver {
	private static final String EXTENSION_POINT = PluginActivator.PLUGIN_ID + ".registrar";

	private static final String ELEMENT_E_VALIDATOR = "EValidator";
	private static final String ELEMENT_REFLECTIVE_VALIDATOR = "ReflectiveValidator";
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

				switch (validatorElement.getName()) {
				case ELEMENT_E_VALIDATOR:
				case ELEMENT_REFLECTIVE_VALIDATOR: {
					if (isValidatingEPackage(validatorElement, uri)) {
						registerValidator(validatorElement, ePackage);
					}
					break;
				}
				default:
					PluginActivator.getLog().log(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID,
							"Unknown validator type: " + validatorElement.getName()));
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

	private void registerValidator(IConfigurationElement validatorElement, EPackage ePackage) {
		try {
			EValidator validator = null;
			if (ELEMENT_REFLECTIVE_VALIDATOR.equals(validatorElement.getName())) {
				Class<?> validatorClazz = getAttributeAsClass(validatorElement, ATTR_CLASS);
				if (!EValidator.class.isAssignableFrom(validatorClazz)) {
					validator = DeclarativeValidator.of(validatorClazz);
				}
			}
			if (null == validator) {
				validator = EValidator.class.cast(validatorElement.createExecutableExtension(ATTR_CLASS));
			}
			EValidatorRegistrar registrar = new EValidatorRegistrar();
			registrar.register(ePackage, validator);
		} catch (CoreException e) {
			PluginActivator.getLog().log(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID,
					"Failed to register EValidator: " + e.getMessage(), e));
		}
	}

	private static Class<?> getAttributeAsClass(IConfigurationElement configurationElement, String attributeName)
			throws CoreException {
		final IContributor contributor = configurationElement.getContributor();
		final String className = configurationElement.getAttribute(attributeName);
		if (className == null) {
			throw new CoreException(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID,
					String.format("Plug-in %s was unable to load class %s", contributor.getName(), className)));
		}
		final Bundle bundle;
		if (contributor instanceof RegistryContributor) {
			try {
				final long id = Long.parseLong(((RegistryContributor) contributor).getActualId());
				bundle = PluginActivator.getBundleContext().getBundle(id);
			} catch (NumberFormatException e) {
				throw new CoreException(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID,
						String.format("Plug-in INVALID BUNDLE ID was unable to load class %s", className), e));
			}
		} else {
			bundle = PluginActivator.getBundleContext().getBundle(contributor.getName());
		}
		if (bundle == null) {
			throw new CoreException(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID,
					String.format("Plug-in UNKNOWN BUNDLE was unable to load class %s", className)));
		}
		try {
			return bundle.loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new CoreException(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID,
					String.format("Plug-in %s was unable to load class %s", bundle.getSymbolicName(), className), e));
		}
	}

	@Override
	public void ePackageUnregistered(String uri, EPackage ePackage) {
		EValidatorRegistrar registrar = new EValidatorRegistrar();
		registrar.unregister(ePackage);
	}
}
