package com.voltdb.blackrock.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.voltdb.client.Client;
import org.voltdb.client.ClientConfig;
import org.voltdb.client.ClientFactory;
import org.voltdb.client.ClientStats;
import org.voltdb.client.ClientStatsContext;
import org.voltdb.client.NoConnectionsException;
import org.voltdb.client.NullCallback;
import org.voltdb.types.TimestampType;

public class TradesLoader extends Thread {

	public static Client client;
	public static ClientConfig config;
	public static ClientStatsContext statsCtx;
	private BufferedReader br;
	private File file;
	private String dateOfTrans;
	private String todayDate;

	public TradesLoader() throws Throwable {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");	    		
 		todayDate = df.format(new Date()).toString();
 		
		file = new File("/Volumes/Data/tmp/sorted_trade_data");
		FileReader fr = new FileReader(file);
		br = new BufferedReader(fr);
		String readLine = br.readLine();
		dateOfTrans = readLine.substring(6,10) + "-" + readLine.substring(2, 4) + "-" +  readLine.substring(4,6);
		String[] firstLineParts = readLine.split(":");
		int realTimeTPS = Integer.parseInt(firstLineParts[firstLineParts.length - 1].trim()) / 23400;
		System.out.println("Real time TPS: " + realTimeTPS);
		PlaybackTracker.realTimeTradeTPS = realTimeTPS;
		PlaybackTracker.currTradeTPS = realTimeTPS;
		PlaybackTracker.tradesTpsChanged = true;

		config = new ClientConfig();
		client = ClientFactory.createClient(config);
		client.createConnection("localhost");
		statsCtx = client.createStatsContext();
	}

	public void insertOneRow(String readLine) throws Exception {
		
		String timeStr = dateOfTrans + " " + readLine.substring(0, 2) + ":" + readLine.substring(2, 4) + ":" + readLine.substring(4, 6) + "." + readLine.substring(6,9);
		TimestampType timestamp = new TimestampType(timeStr);
		
		
		Timestamp timestamp1 = Timestamp.valueOf(todayDate+" "+readLine.toString().substring(0,2)+":"+readLine.toString().substring(2,4)+":"+readLine.toString().substring(4,6)+".0");
        
		long time = timestamp1.getTime();
		
		String timeStamp = readLine.substring(0, 9);
		String exchange = readLine.substring(9, 10);
		String symbol = null;
		
		
		if(readLine.toString().substring(9, 10).equals("A") || readLine.toString().substring(9, 10).equals("P")){
			symbol = readLine.toString().substring(10,13).toString().trim();
        } else if(readLine.toString().substring(9, 10).equals("B") || readLine.toString().substring(9, 10).equals("T") || readLine.toString().substring(9, 10).equals("T/Q") || readLine.toString().substring(9, 10).equals("X")) {
        	symbol = readLine.toString().substring(10,14).toString().trim();
        } else {
        	symbol = readLine.toString().substring(10,26).toString().trim();
        }
		
		
		String suffix = readLine.substring(16,26);
		String saleCondition = readLine.substring(26, 30);
		long tradeVolume = Long.parseLong(readLine.substring(30, 39));
		float tradePrice = Float.parseFloat(readLine.toString().substring(39,46)+"."+readLine.toString().substring(46,50));
		String tradeStopStockIndicator = readLine.substring(50, 51);
		String tradeCorrectionIndicator = readLine.substring(51, 53);
		int tradeSequenceNumber = Integer.parseInt(readLine.substring(53, 69));
		String sourceOfTrade = readLine.substring(69, 70);
		String tradeReportingFacility = readLine.substring(70, 71);
		
		/* if (symbol.equals("BIB")||
      		   symbol.equals("IRWD")||
      		   symbol.equals("LGND")||
      		   symbol.equals("ACHN")||
      		   symbol.equals("RGEN")||
      		   symbol.equals("ALKS")||
      		   symbol.equals("NPSP")||
      		   symbol.equals("SQNM")||
      		   symbol.equals("ENDP")||
      		   symbol.equals("GTXI")||
      		   symbol.equals("XNPT")||
      		   symbol.equals("ARQL")||
      		   symbol.equals("AMRI")||
      		   symbol.equals("MNTA")||
      		   symbol.equals("ALNY")||
      		   symbol.equals("CBST")||
      		   symbol.equals("ASTX")||
      		   symbol.equals("AFFX")||
      		   symbol.equals("BMRN")||
      		   symbol.equals("ARNA")||
      		   symbol.equals("MACK")||
      		   symbol.equals("GRFS")||
      		   symbol.equals("PGNX")||
      		   symbol.equals("SPPI")||
      		   symbol.equals("RPTP")||
      		   symbol.equals("AMRN")||
      		   symbol.equals("TECH")||
      		   symbol.equals("DNDN")||
      		   symbol.equals("LIFE")||
      		   symbol.equals("CLVS")||
      		   symbol.equals("AVNR")||
      		   symbol.equals("NBIX")||
      		   symbol.equals("ACAD")||
      		   symbol.equals("PTIE")||
      		   symbol.equals("GEVA")||
      		   symbol.equals("INFI")||
      		   symbol.equals("ARRY")||
      		   symbol.equals("VICL")||
      		   symbol.equals("ALXN")||
      		   symbol.equals("IMMU")||
      		   symbol.equals("SUPN")||
      		   symbol.equals("ECYT")||
      		   symbol.equals("ONTY")||
      		   symbol.equals("VRTX")||
      		   symbol.equals("SRPT")||
      		   symbol.equals("THRX")||
      		   symbol.equals("IPXL")||
      		   symbol.equals("QGEN")||
      		   symbol.equals("IMGN")||
      		   symbol.equals("MYL")||
      		   symbol.equals("SCLN")||
      		   symbol.equals("NKTR")||
      		   symbol.equals("ILMN")||
      		   symbol.equals("AEGR")||
      		   symbol.equals("ANAC")||
      		   symbol.equals("PDLI")||
      		   symbol.equals("GILD")||
      		   symbol.equals("WCRX")||
      		   symbol.equals("ACOR")||
      		   symbol.equals("OPTR")||
      		   symbol.equals("SLXP")||
      		   symbol.equals("GERN")||
      		   symbol.equals("OREX")||
      		   symbol.equals("CELG")||
      		   symbol.equals("MDVN")||
      		   symbol.equals("CLDX")||
      		   symbol.equals("DYAX")||
      		   symbol.equals("SGMO")||
      		   symbol.equals("HPTX")||
      		   symbol.equals("QLTI")||
      		   symbol.equals("NVAX")||
      		   symbol.equals("CERS")||
      		   symbol.equals("CYTX")||
      		   symbol.equals("SVA")||
      		   symbol.equals("SNTA")||
      		   symbol.equals("ZGNX")||
      		   symbol.equals("BIIB")||
      		   symbol.equals("RIGL")||
      		   symbol.equals("REGN")||
      		   symbol.equals("ONXX")||
      		   symbol.equals("SHPG")||
      		   symbol.equals("AKRX")||
      		   symbol.equals("DEPO")||
      		   symbol.equals("TSRX")||
      		   symbol.equals("ITMN")||
      		   symbol.equals("HITK")||
      		   symbol.equals("MDCO")||
      		   symbol.equals("OSIR")||
      		   symbol.equals("LMNX")||
      		   symbol.equals("SCMP")||
      		   symbol.equals("IDIX")||
      		   symbol.equals("UTHR")||
      		   symbol.equals("AMAG")||
      		   symbol.equals("TRGT")||
      		   symbol.equals("SGEN")||
      		   symbol.equals("SNTS")||
      		   symbol.equals("QCOR")||
      		   symbol.equals("PCRX")||
      		   symbol.equals("EXEL")||
      		   symbol.equals("MNKD")||
      		   symbol.equals("GHDX")||
      		   symbol.equals("MYGN")||
      		   symbol.equals("VVUS")||
      		   symbol.equals("ARIA")||
      		   symbol.equals("INCY")||
      		   symbol.equals("ENZN")||
      		   symbol.equals("HALO")||
      		   symbol.equals("CADX")||
      		   symbol.equals("OMER")||
      		   symbol.equals("SIGA")||
      		   symbol.equals("VPHM")||
      		   symbol.equals("FURX")||
      		   symbol.equals("NLNK")||
      		   symbol.equals("FOLD")||
      		   symbol.equals("CRIS")||
      		   symbol.equals("AUXL")||
      		   symbol.equals("PACB")||
      		   symbol.equals("AMGN")||
      		   symbol.equals("ISIS")||
      		   symbol.equals("SGYP")||
      		   symbol.equals("VNDA")||
      		   symbol.equals("AVEO")||
      		   symbol.equals("LXRX")||
      		   symbol.equals("XOMA")||
      		   symbol.equals("JAZZ")
      		    ) {*/
			 	client.callProcedure(new NullCallback(), "TradeInsert", timeStamp,
			 			exchange, symbol, suffix, saleCondition, tradeVolume, tradePrice,
			 			tradeStopStockIndicator, tradeCorrectionIndicator,
			 			tradeSequenceNumber, sourceOfTrade, tradeReportingFacility,time);
// 		 	}
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
						if (PlaybackTracker.tradesTpsChanged) {
							client.drain();
							client.close();

							config = new ClientConfig();
							if (PlaybackTracker.currTradeTPS != 0) {
								System.out.println("Number of max Transactions for Trades: " + PlaybackTracker.currTradeTPS);
								config.setMaxTransactionsPerSecond(PlaybackTracker.currTradeTPS);
							}
							client = ClientFactory.createClient(config);
							client.createConnection("localhost");
							PlaybackTracker.tradesTpsChanged = false;
						}

						insertOneRow(readLine);
					}
				}
			}
		} catch (Exception e) {
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
			client.callProcedure(new NullCallback(), "DeleteAllTrades");
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
