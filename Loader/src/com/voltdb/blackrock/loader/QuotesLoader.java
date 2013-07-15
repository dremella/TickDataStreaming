package com.voltdb.blackrock.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.voltdb.client.Client;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientStats;
import org.voltdb.client.ClientStatsContext;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.NullCallback;
import org.voltdb.types.TimestampType;

public class QuotesLoader extends Thread {

	public static Client client;
	public static ClientConfig config;
	public static ClientStatsContext statsCtx;
	private BufferedReader br;
	private File file;
	
	private String dateOfTrans;

	public QuotesLoader() throws Throwable {
		file = new File("/Volumes/Data/tmp/sorted_quote_data");
		FileReader fr = new FileReader(file);
		br = new BufferedReader(fr, 96000);
		String readLine = br.readLine();
		dateOfTrans = readLine.substring(6,10) + "-" + readLine.substring(2, 4) + "-" +  readLine.substring(4,6);
		String[] firstLineParts = readLine.split(":");
		int realTimeTPS = Integer
				.parseInt(firstLineParts[firstLineParts.length - 1].trim()) / 23400;
		System.out.println("Real time TPS: " + realTimeTPS);
		PlaybackTracker.realTimeQuoteTPS = realTimeTPS;
		PlaybackTracker.currQuoteTPS = realTimeTPS;
		PlaybackTracker.quotesTpsChanged = true;

		config = new ClientConfig();
		client = ClientFactory.createClient(config);
		client.createConnection("localhost");
		statsCtx = client.createStatsContext();
	}

	public void insertOneRow(String readLine) throws Exception {
		String timeStr = dateOfTrans + " " + readLine.substring(0, 2) + ":" + readLine.substring(2, 4) + ":" + readLine.substring(4, 6) + "." + readLine.substring(6,9);
		TimestampType timestamp = new TimestampType(timeStr);
		long time = timestamp.getTime();
		String exchange = readLine.substring(9, 10);
		String symbol = null;
		
		 if(readLine.toString().substring(9, 10).equals("A") || readLine.toString().substring(9, 10).equals("P")){
			 symbol = readLine.toString().substring(10,13).toString().trim();
		 } else if(readLine.toString().substring(9, 10).equals("B") || readLine.toString().substring(9, 10).equals("T") || readLine.toString().substring(9, 10).equals("T/Q") || readLine.toString().substring(9, 10).equals("X")) {
    	   symbol = readLine.toString().substring(10,14).toString().trim();
		 } else {
    	   symbol = readLine.toString().substring(10,26).toString().trim();
		 }
		
		String suffix = readLine.substring(16, 26);
		//float bidPrice = Float.parseFloat(readLine.substring(26, 37)) / 10000F;
		float bidPrice = Float.parseFloat(readLine.toString().substring(26,33)+"."+readLine.toString().substring(33,37));
		int bidSize = Integer.parseInt(readLine.substring(37, 44));
		//float askPrice = Float.parseFloat(readLine.substring(44, 55)) / 10000F;
		
		float askPrice = Float.parseFloat(readLine.toString().substring(44,51)+"."+readLine.toString().substring(51,55));
		int askSize = Integer.parseInt(readLine.substring(55, 62));
		String quoteCondition = readLine.substring(62, 63);
		short marketMaker = (short) 0;// new
								// Integer(readLine.substring(63,67).trim()).shortValue();
		String bidExchange = readLine.substring(67, 68);
		String askExchange = readLine.substring(68, 69);
		int sequenceNumber = Integer.parseInt(readLine.substring(69, 85));
		String natBBOIndStr = readLine.substring(85, 86).trim();
		byte natBBOInd = 0;
		if (!natBBOIndStr.equals(""))
			natBBOInd = Byte.parseByte(readLine.substring(85, 86));
		String nasdBBOIndStr = readLine.substring(86, 87).trim();
		byte nasdBBOInd = 0;
		if (!nasdBBOIndStr.equals(""))
			nasdBBOInd = Byte.parseByte(readLine.substring(86, 87));
		String quoteCxlCorr = readLine.substring(87, 88);
		String sourceOfQuote = readLine.substring(88, 89);
		String rpi = readLine.substring(89, 90);
		String shortSale = readLine.substring(90, 91);
		String cqs = readLine.substring(91, 92);
		String utp = readLine.substring(92, 93);
		String FAMInd = readLine.substring(93, 94);

		client.callProcedure(new NullCallback(), "QuotesInsert", time,
				exchange, symbol, suffix, bidPrice, bidSize, askPrice, askSize,
				quoteCondition, marketMaker, bidExchange, askExchange,
				sequenceNumber, natBBOInd, nasdBBOInd, quoteCxlCorr,
				sourceOfQuote, rpi, shortSale, cqs, utp, FAMInd);
	}

	public void run() {
		String readLine;
		try {

			while (true) {
				if (!PlaybackTracker.playbackPaused) {
					readLine = br.readLine();
					if (readLine != null) {
						/*
						 * Call the Procedure
						 */
						if (PlaybackTracker.quotesTpsChanged) {
							client.drain();
							client.close();

							config = new ClientConfig();
							if (PlaybackTracker.currQuoteTPS != 0) {
								System.out.println("Number of max Transactions for Quotes: " + PlaybackTracker.currQuoteTPS);
								config.setMaxTransactionsPerSecond(PlaybackTracker.currQuoteTPS);
							}
							client = ClientFactory.createClient(config);
							client.createConnection("localhost");
							PlaybackTracker.quotesTpsChanged = false;
						}

						insertOneRow(readLine);
					}
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void calibrate(int seconds) {
		long calibrationDuration = seconds * 1000L;
		long calibrationEndTime = System.currentTimeMillis()
				+ calibrationDuration;
		statsCtx.fetchAndResetBaseline();
		while (System.currentTimeMillis() < calibrationEndTime) {
			String readLine;
			try {
				readLine = br.readLine();
				if (readLine != null) {
					insertOneRow(readLine);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		ClientStats stats = statsCtx.fetchAndResetBaseline().getStats();
		long throughput = stats.getTxnThroughput();
		PlaybackTracker.topSpeed = (int) throughput;
	}
	
	public void deleteAllRows() {
		try {
			client.callProcedure(new NullCallback(), "DeleteAllQuotes");
		} catch (NoConnectionsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cleanup() {
		if (client != null) {
			try {
				client.drain();
				client.close();
				br.close();
			} catch (NoConnectionsException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
