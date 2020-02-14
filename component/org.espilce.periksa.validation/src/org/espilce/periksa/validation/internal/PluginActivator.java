package org.espilce.periksa.validation.internal;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class PluginActivator implements BundleActivator {
	public static final String PLUGIN_ID = "org.espilce.periksa.validation";

	private static BundleContext bundleContext = null;
	
	@Override
	public void start(BundleContext context) throws Exception {
		bundleContext = context;
		RegistrarExtensionPointProcessor.registerValidators();
	}
	
	@Override
	public void stop(BundleContext context) throws Exception {
		bundleContext = null;
	}
	
	public static ILog getLog() {
		return bundleContext == null ? null : Platform.getLog(bundleContext.getBundle());
	}
}
