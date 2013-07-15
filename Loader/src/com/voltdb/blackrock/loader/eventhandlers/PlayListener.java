package com.voltdb.blackrock.loader.eventhandlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.voltdb.blackrock.loader.DataLoaderWindow;
import com.voltdb.blackrock.loader.DataLoadingManager;
import com.voltdb.blackrock.loader.PlaybackTracker;

public class PlayListener implements ActionListener {
	
	private DataLoaderWindow window;
	
	public PlayListener(DataLoaderWindow window) {
		this.window = window;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton playButton = (JButton) e.getSource();
		playButton.setEnabled(false);
		
		window.currQuotesTPS.setText(String.valueOf(PlaybackTracker.currQuoteTPS));
		window.currTradesTPS.setText(String.valueOf(PlaybackTracker.currTradeTPS));
		
		window.quotesSlider.setValue((PlaybackTracker.currQuoteTPS*100)/PlaybackTracker.topSpeed);
		window.tradesSlider.setValue((PlaybackTracker.currTradeTPS*100)/PlaybackTracker.topSpeed);
		
		
		DataLoadingManager.startLoading();

	}

}
