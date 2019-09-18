package org.espilce.periksa.test;

import org.eclipse.ui.IStartup;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {
		new ModelValidator().register();
	}
}
