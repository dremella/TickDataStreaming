package com.voltdb.blackrock.loader;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import com.voltdb.blackrock.loader.eventhandlers.CalibrateListener;
import com.voltdb.blackrock.loader.eventhandlers.PauseListener;
import com.voltdb.blackrock.loader.eventhandlers.PlayListener;
import com.voltdb.blackrock.loader.eventhandlers.TPSChangeListener;

public class DataLoaderWindow {

	private JFrame frame;
	
	public JLabel currQuotesTPS;
	public JLabel currTradesTPS;
	public JLabel rtQuotesTPS;
	public JLabel rtTradesTPS;
	public JLabel quotesRatio;
	public JLabel tradesRatio;
	
	public JSlider quotesSlider;
	public JSlider tradesSlider;
	public JButton playButton;
	public JButton pauseButton;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DataLoaderWindow window = new DataLoaderWindow();
					window.initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		JButton warmupButton = new JButton("Calibrate");
		warmupButton
				.setToolTipText("Loads records for 20 seconds, measures the TPS and sets that value to be 100%");
		warmupButton.addActionListener(new CalibrateListener(this));
		c.gridx = 0;
		c.gridy = 0;
		contentPane.add(warmupButton, c);

		JLabel l4 = new JLabel("Quotes: ");
		c.gridx = 0;
		c.gridy = 1;
		c.gridwidth = 1;
		contentPane.add(l4, c);
		quotesSlider = new JSlider(SwingConstants.HORIZONTAL);
		quotesSlider.setMinimum(0);
		quotesSlider.setMaximum(100);
		quotesSlider.setValue(0);
		quotesSlider.setMajorTickSpacing(10);
		quotesSlider.setMinorTickSpacing(1);
		quotesSlider.addChangeListener(new TPSChangeListener(this, "quotes"));
		c.gridx = 1;
		c.gridwidth = 3;
		contentPane.add(quotesSlider, c);

		JLabel l5 = new JLabel("Trades: ");
		c.gridx = 0;
		c.gridy = 2;
		c.gridwidth = 1;
		contentPane.add(l5, c);
		tradesSlider = new JSlider(SwingConstants.HORIZONTAL);
		tradesSlider.setMinimum(0);
		tradesSlider.setMaximum(100);
		tradesSlider.setValue(0);
		tradesSlider.setMajorTickSpacing(10);
		tradesSlider.setMinorTickSpacing(1);
		tradesSlider.addChangeListener(new TPSChangeListener(this, "trades"));
		c.gridx = 1;
		c.gridwidth = 3;
		contentPane.add(tradesSlider, c);

		playButton = new JButton("Start");
		c.gridx = 0;
		c.gridy = 3;
		c.gridwidth = 1;
		playButton.addActionListener(new PlayListener(this));
		contentPane.add(playButton, c);

		pauseButton = new JButton("Pause");
		c.gridx = 1;
		c.gridy = 3;
		c.gridwidth = 1;
		pauseButton.addActionListener(new PauseListener(this));
		contentPane.add(pauseButton, c);

		JLabel l1 = new JLabel("Current TPS: ");
		l1.setHorizontalAlignment(SwingConstants.RIGHT);
		l1.setSize(120, 25);
		c.gridx = 0;
		c.gridy = 4;
		c.insets = new Insets(10, 0, 0, 5);
		contentPane.add(l1, c);
		currQuotesTPS = new JLabel(String.valueOf(PlaybackTracker.currQuoteTPS));
		c.gridx = 1;
		contentPane.add(currQuotesTPS, c);
		c.insets = new Insets(10,5,0,0);
		c.gridx = 2;
		currTradesTPS = new JLabel(String.valueOf(PlaybackTracker.currTradeTPS));
		contentPane.add(currTradesTPS, c);

		JLabel l2 = new JLabel("Real Time TPS: ");
		l2.setHorizontalAlignment(SwingConstants.RIGHT);
		l2.setSize(120, 25);
		c.gridx = 0;
		c.gridy = 5;
		contentPane.add(l2, c);
		c.insets = new Insets(10, 0, 0, 5);
		rtQuotesTPS = new JLabel(String.valueOf(PlaybackTracker.realTimeQuoteTPS));
		c.gridx = 1;
		contentPane.add(rtQuotesTPS, c);
		rtTradesTPS = new JLabel(String.valueOf(PlaybackTracker.realTimeTradeTPS));
		c.gridx = 2;
		c.insets = new Insets(10, 5, 0, 0);
		contentPane.add(rtTradesTPS, c);

		JLabel l3 = new JLabel("Current/Real Ratio: ");
		l3.setHorizontalAlignment(SwingConstants.RIGHT);
		l3.setSize(120, 25);
		c.gridx = 0;
		c.gridy = 6;
		contentPane.add(l3, c);
		c.insets = new Insets(10, 0, 0, 5);
		quotesRatio = new JLabel("N/A");
		c.gridx = 1;
		contentPane.add(quotesRatio, c);
		c.insets = new Insets(10, 5, 0, 0);
		tradesRatio = new JLabel("N/A");
		c.gridx = 2;
		contentPane.add(tradesRatio, c);

	}

}
