package com.voltdb.blackrock.loader.eventhandlers;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.voltdb.blackrock.loader.DataLoaderWindow;
import com.voltdb.blackrock.loader.DataLoadingManager;
import com.voltdb.blackrock.loader.PlaybackTracker;

public class CalibrateListener implements ActionListener {
	
	DataLoaderWindow window;
	
	public CalibrateListener(DataLoaderWindow window) {
		this.window = window;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		final JButton calibrateButton = (JButton) e.getSource();
		calibrateButton.setText("Calibrating...");
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DataLoadingManager.calibrateQuoteTPS();
					DataLoadingManager.calibrateTradeTPS();
					calibrateButton.setText("Calibrate");
					window.rtQuotesTPS.setText(String.valueOf(PlaybackTracker.realTimeQuoteTPS));
					window.rtTradesTPS.setText(String.valueOf(PlaybackTracker.realTimeTradeTPS));
				} catch (Exception e) {
					
				}
			}
		});
	}

}
