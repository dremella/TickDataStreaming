package com.voltdb.blackrock.loader.eventhandlers;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.voltdb.blackrock.loader.DataLoaderWindow;
import com.voltdb.blackrock.loader.PlaybackTracker;

public class TPSChangeListener implements ChangeListener {
	
	private String sliderType = "";
	private DataLoaderWindow window;
	
	public TPSChangeListener(DataLoaderWindow window, String type) {
		this.window = window;
		this.sliderType = type;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider tpsSlider = (JSlider) e.getSource();
		
		
		if (!tpsSlider.getValueIsAdjusting()) {
			int tps = tpsSlider.getValue();
			System.out.println("New TPS scale: " + tps);
			if (sliderType.equals("quotes")) {
				
				PlaybackTracker.currQuoteTPS = (PlaybackTracker.topSpeed*tps)/100;
				float ratio = (float)PlaybackTracker.currQuoteTPS/((float)PlaybackTracker.realTimeQuoteTPS);
				
				window.currQuotesTPS.setText(String.valueOf(PlaybackTracker.currQuoteTPS));
				window.quotesRatio.setText(String.valueOf(ratio));
				PlaybackTracker.quotesTpsChanged = true;
			} else if (sliderType.equals("trades")) {
				PlaybackTracker.currTradeTPS = (PlaybackTracker.topSpeed*tps)/100;
				float ratio = (float)PlaybackTracker.currTradeTPS/((float)PlaybackTracker.realTimeTradeTPS);
				
				window.currTradesTPS.setText(String.valueOf(PlaybackTracker.currTradeTPS));
				window.tradesRatio.setText(String.valueOf(ratio));
				PlaybackTracker.tradesTpsChanged = true;
			}
		}
	}

}
