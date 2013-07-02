package br.ufc.great.syssu.ubicentre;

import java.util.logging.*;

public class Main {

	private static int port = 9090;
	private static String logfile;

	public static void main(String[] args) {
		Logger logger = Logger.getLogger("UbiCentre");
		logger.addHandler(new StreamHandler());

		try {
			for (int i = 0; i < args.length; i++) {
				String arg = args[i];
				if (arg.equals("--port")) {
					port = Integer.parseInt(args[++i]);
				} else if (arg.equals("--logfile")) {
					logfile = args[++i];
				} else {
					throw new Exception();
				}
			}
		} catch (Exception ex) {
			logger.severe("Invalid parameters.");
		}

		if (logfile != null) {
			try {
				FileHandler fileHandler = new FileHandler(logfile, true);
				fileHandler.setFormatter(new CustomLogFormatter());
				logger.addHandler(fileHandler);
			} catch (Exception ex) {
				logger.severe("Error in starting UbiCentre. Invalid log file.");
				System.exit(1);
			}
		}

		try {
			Thread t = new Thread(new UbiCentreProcess(port), "UbiCentre Process");
			t.start();
			logger.info("UbiCentre started and listening in port " + port + ".");
			t.join();
		} catch (Exception ex) {
			logger.log(Level.SEVERE, "Error in starting UbiCentre.", ex);
			System.exit(1);
		}
	}
	
}
