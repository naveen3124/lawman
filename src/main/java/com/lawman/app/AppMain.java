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

	private static class Options extends DaemonController.Options {
	@Override
	public String getStdoutLogFileName() {
		String logFileName = super.getStdoutLogFileName();
		if (logFileName == null) {
			logFileName = "logs/stdout.log";
		}
		return logFileName;
	}
	}

	@Override
	protected org.lawman.daemon.DaemonController.Options createOptions() {
		return new Options();
	}

	public static void main(String[] args) {
		AppMain controller = new AppMain();
		controller.exec(args);
	}
}
