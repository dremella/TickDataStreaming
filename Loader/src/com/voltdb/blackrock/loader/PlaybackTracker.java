package com.voltdb.blackrock.loader;

import org.voltdb.client.Client;

public class PlaybackTracker {
	
	public static boolean quotesTpsChanged = false;
	public static boolean tradesTpsChanged = false;
	public static int topSpeed = 0;
	
	public static int currTradeTPS = 0;
	public static int currQuoteTPS = 0;
	public static int realTimeTradeTPS = 0;
	public static int realTimeQuoteTPS = 0;
	
	public static Client client;
	
	public static boolean playbackPaused = false;
}
