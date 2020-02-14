package org.espilce.periksa.validation.internal;

import java.lang.reflect.Field;

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
import org.espilce.periksa.validation.AbstractValidator;
import org.espilce.periksa.validation.EValidatorRegistrar;

final class RegistrarExtensionPointProcessor {
	private static final String EXTENSION_POINT = PluginActivator.PLUGIN_ID + ".registrar";

	private static final String ELEMENT_ABSTRACT_VALIDATOR = "AbstractValidator";
	private static final String ELEMENT_E_VALIDATOR = "EValidator";
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
				switch (validatorElement.getName()) {
				case ELEMENT_ABSTRACT_VALIDATOR:
					try {
						AbstractValidator validator = AbstractValidator.class.cast(
								validatorElement.createExecutableExtension(ATTR_CLASS));
						validator.register();
					} catch (CoreException e) {
						PluginActivator.getLog().log(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID, 
								"Failed to register AbstractValidator: " + e.getMessage(), e));
					}
					break;
				case ELEMENT_E_VALIDATOR:
					try {
						EValidator validator = EValidator.class.cast(
								validatorElement.createExecutableExtension(ATTR_CLASS));
						EValidatorRegistrar registrar = new EValidatorRegistrar();
						for (IConfigurationElement packageElement : validatorElement.getChildren("EPackage")) {
							try {
								String className = packageElement.getAttribute(ATTR_CLASS);
								Class<?> packageClass = Class.forName(className, true, validator.getClass().getClassLoader());
								Field field = packageClass.getField(FIELD_E_INSTANCE);
								EPackage ePackage = EPackage.class.cast(field.get(null));
								registrar.register(ePackage, validator);
							} catch (ClassNotFoundException e) {
								PluginActivator.getLog().log(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID, 
										"Failed to register EValidator, EPackage class not found: " + e.getMessage(), e));
							} catch (NoSuchFieldException | IllegalArgumentException e) {
								PluginActivator.getLog().log(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID, 
										"Failed to register EValidator, invalid EPackage: " + e.getMessage(), e));
							} catch (SecurityException | IllegalAccessException e) {
								PluginActivator.getLog().log(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID, 
										"Failed to register EValidator, could not access EPackage: " + e.getMessage(), e));
							}
						}
					} catch (CoreException e) {
						PluginActivator.getLog().log(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID, 
								"Failed to register EValidator: " + e.getMessage(), e));
					}
					break;
				default:
					PluginActivator.getLog().log(new Status(IStatus.ERROR, PluginActivator.PLUGIN_ID, 
							"Unknown validator type: " + validatorElement.getName()));
					break;
				}
			}
		}
	}
}
