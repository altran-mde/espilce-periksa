package org.espilce.periksa.validation.internal;

import static org.eclipse.core.runtime.IStatus.ERROR;
import static org.espilce.periksa.validation.internal.PluginActivator.PLUGIN_ID;

import java.lang.reflect.Field;

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
import org.espilce.periksa.validation.DeclarativeValidator;
import org.espilce.periksa.validation.ERegistrableValidator;
import org.espilce.periksa.validation.EValidatorRegistrar;
import org.osgi.framework.Bundle;

final class RegistrarExtensionPointProcessor {
	private static final String EXTENSION_POINT = PluginActivator.PLUGIN_ID + ".registrar";

	private static final String ELEMENT_E_VALIDATOR = "EValidator";
	private static final String ELEMENT_E_REGISTRABLE_VALIDATOR = "ERegistrableValidator";
	private static final String ELEMENT_REFLECTIVE_VALIDATOR = "ReflectiveValidator";
	private static final String ATTR_CLASS = "class";
	private static final String FIELD_E_INSTANCE = "eINSTANCE";

	static void registerValidators() {
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
				try {
					switch (validatorElement.getName()) {
					case ELEMENT_E_REGISTRABLE_VALIDATOR: {
						ERegistrableValidator validator = ERegistrableValidator.class
								.cast(validatorElement.createExecutableExtension(ATTR_CLASS));
						validator.register();
						break;
					}
					case ELEMENT_REFLECTIVE_VALIDATOR: {
						Class<?> validatorClazz = getAttributeAsClass(validatorElement, ATTR_CLASS);
						if (!EValidator.class.isAssignableFrom(validatorClazz)) {
							EValidator validator = DeclarativeValidator.of(validatorClazz);
							EValidatorRegistrar registrar = new EValidatorRegistrar();
							for (IConfigurationElement packageElement : validatorElement.getChildren("EPackage")) {
								EPackage ePackage = getAttributeAsEPackage(packageElement, ATTR_CLASS);
								registrar.register(ePackage, validator);
							}
							break;
						}
						// validatorClazz is an instance of EValidator, just continue with next case
					}
					case ELEMENT_E_VALIDATOR: {
						EValidator validator = EValidator.class
								.cast(validatorElement.createExecutableExtension(ATTR_CLASS));
						EValidatorRegistrar registrar = new EValidatorRegistrar();
						for (IConfigurationElement packageElement : validatorElement.getChildren("EPackage")) {
							EPackage ePackage = getAttributeAsEPackage(packageElement, ATTR_CLASS);
							registrar.register(ePackage, validator);
						}
						break;
					}
					default:
						PluginActivator.getLog().log(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID,
								"Unknown validator type: " + validatorElement.getName()));
						break;
					}
				} catch (CoreException e) {
					PluginActivator.getLog()
							.log(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID,
									String.format("Plug-in %s was unable to register %s %s",
											validatorElement.getContributor().getName(), validatorElement.getName(),
											validatorElement.getAttribute(ATTR_CLASS)),
									e));
				}
			}
		}
	}

	private static EPackage getAttributeAsEPackage(IConfigurationElement configurationElement, String attributeName)
			throws CoreException {
		Class<?> packageClass = getAttributeAsClass(configurationElement, attributeName);
		try {
			Field field = packageClass.getField(FIELD_E_INSTANCE);
			return EPackage.class.cast(field.get(null));
		} catch (Exception e) {
			throw new CoreException(
					new Status(ERROR, PLUGIN_ID, String.format("Plug-in %s was unable to resolve EPackage %s",
							configurationElement.getContributor().getName(), packageClass.getName()), e));
		}
	}

	private static Class<?> getAttributeAsClass(IConfigurationElement configurationElement, String attributeName)
			throws CoreException {
		final IContributor contributor = configurationElement.getContributor();
		final String className = configurationElement.getAttribute(attributeName);
		if (className == null) {
			throw new CoreException(new Status(ERROR, PLUGIN_ID,
					String.format("Plug-in %s was unable to load class %s", contributor.getName(), className)));
		}
		final Bundle bundle;
		if (contributor instanceof RegistryContributor) {
			bundle = PluginActivator.getBundleContext().getBundle(((RegistryContributor) contributor).getId());
		} else {
			bundle = PluginActivator.getBundleContext().getBundle(contributor.getName());
		}
		if (bundle == null) {
			throw new CoreException(new Status(ERROR, PLUGIN_ID,
					String.format("Plug-in UNKNOWN BUNDLE was unable to load class %s", className)));
		}
		try {
			return bundle.loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new CoreException(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID,
					String.format("Plug-in %s was unable to load class %s", bundle.getSymbolicName(), className), e));
		}
	}
}
