package org.espilce.periksa.validation.ui.internal;

import org.eclipse.ui.IStartup;
import org.espilce.periksa.validation.EValidatorRegistrar;

public class StartupListener implements IStartup {
	@Override
	public void earlyStartup() {
		// Creating a new registrar will activate the plugin, causing all EValidator extensions to be registered. 
		new EValidatorRegistrar();
	}
}
