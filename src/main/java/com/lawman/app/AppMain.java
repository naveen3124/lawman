package com.lawman.app;

import org.lawman.daemon.DaemonController;
import org.lawman.daemon.IDaemon;

public class AppMain extends DaemonController {
	@Override
	public String getDefaultInstanceName() {
		return "default";
	}

	@Override
	public Class<? extends IDaemon> getDaemonClass() {
		return AppDaemon.class;
	}

	public static void main(String[] args) {
		AppMain controller = new AppMain();
		controller.exec(args);
	}
}
