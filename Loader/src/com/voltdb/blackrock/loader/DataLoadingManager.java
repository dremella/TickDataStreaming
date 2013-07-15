package com.voltdb.blackrock.loader;

public class DataLoadingManager {

	private static QuotesLoader ql;
	private static TradesLoader tl;

	public DataLoadingManager() {
	}

	public static void calibrateQuoteTPS() {
		try {
			ql = new QuotesLoader();
			ql.calibrate(5);
			ql.deleteAllRows();
			ql.cleanup();
			ql = null;
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static void calibrateTradeTPS() {
		try {
			tl = new TradesLoader();
			tl.calibrate(5);
			tl.deleteAllRows();
			tl.cleanup();
			tl = null;
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public static void startLoading() {
		try {
			ql = new QuotesLoader();
			tl = new TradesLoader();

			ql.start();
			tl.start();
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}
