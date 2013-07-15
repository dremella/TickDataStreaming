package com.voltdb.blackrock.loader.eventhandlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.voltdb.blackrock.loader.DataLoaderWindow;
import com.voltdb.blackrock.loader.DataLoadingManager;
import com.voltdb.blackrock.loader.PlaybackTracker;

public class PauseListener implements ActionListener {

	private DataLoaderWindow window;

	public PauseListener(DataLoaderWindow window) {
		this.window = window;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (PlaybackTracker.playbackPaused) {
			window.pauseButton.setText("Pause");
			PlaybackTracker.playbackPaused = false;
		} else {
			window.pauseButton.setText("Continue");
			PlaybackTracker.playbackPaused = true;
		}
	}

}
