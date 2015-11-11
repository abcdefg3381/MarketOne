/**
 * 
 */
package marketone.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.ParseException;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import marketone.MainProgram;
import marketone.db.entity.Index;
import marketone.visualization.MovieMaker;

/**
 * @author LIU Xiaofan
 * 
 */
public class NetworkPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6100746860619353433L;
	private JButton chartButton = null;
	private JButton formNetworkButton = null;
	private JButton makeMovieButton = null;
	private JList stockList = null;
	private JPanel stocksPanel = null;
	private JScrollPane stocksScrollPane = null;
	private JButton btnStat;

	public NetworkPanel() {
		GridBagConstraints gridBagConstraints5 = new GridBagConstraints();
		gridBagConstraints5.gridx = 2;
		gridBagConstraints5.insets = new Insets(5, 5, 5, 5);
		gridBagConstraints5.gridy = 15;
		GridBagConstraints gridBagConstraints35 = new GridBagConstraints();
		gridBagConstraints35.gridx = 2;
		gridBagConstraints35.insets = new Insets(5, 5, 5, 0);
		gridBagConstraints35.gridy = 1;
		GridBagConstraints gridBagConstraints11 = new GridBagConstraints();
		gridBagConstraints11.insets = new Insets(0, 0, 0, 5);
		gridBagConstraints11.gridx = 0;
		gridBagConstraints11.weightx = 1.0D;
		gridBagConstraints11.weighty = 1.0D;
		gridBagConstraints11.gridheight = 13;
		gridBagConstraints11.gridwidth = 2;
		gridBagConstraints11.fill = GridBagConstraints.BOTH;
		gridBagConstraints11.gridy = 0;
		setLayout(new GridBagLayout());
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Form Network",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		add(getStocksPanel(), gridBagConstraints11);
		add(getChartButton(), gridBagConstraints35);
						GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
						gridBagConstraints1.gridx = 2;
						gridBagConstraints1.insets = new Insets(5, 5, 5, 0);
						gridBagConstraints1.gridy = 6;
						add(getFormNetworkButton(), gridBagConstraints1);
						GridBagConstraints gbc_btnStat = new GridBagConstraints();
						gbc_btnStat.insets = new Insets(0, 0, 5, 0);
						gbc_btnStat.gridx = 2;
						gbc_btnStat.gridy = 8;
						add(getBtnStat(), gbc_btnStat);
				
						GridBagConstraints gridBagConstraints14 = new GridBagConstraints();
						gridBagConstraints14.gridx = 2;
						gridBagConstraints14.insets = new Insets(5, 5, 5, 0);
						gridBagConstraints14.gridy = 9;
						add(getMakeMovieButton(), gridBagConstraints14);
	}

	/**
	 * This method initializes chartButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getChartButton() {
		if (chartButton == null) {
			chartButton = new JButton();
			chartButton.setText("Index Chart");
			chartButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					MainProgram.getInstance().getLogic()
							.drawChart((Index) getStockList().getSelectedValue());
				}
			});
		}
		return chartButton;
	}

	/**
	 * This method initializes formNetworkButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getFormNetworkButton() {
		if (formNetworkButton == null) {
			formNetworkButton = new JButton();
			formNetworkButton.setText("Form Network");
			formNetworkButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					MainProgram.getInstance().getLogic().getNetwork();
					setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					getBtnStat().setEnabled(true);
				}
			});
		}
		return formNetworkButton;
	}

	/**
	 * This method initializes makeMovieButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getMakeMovieButton() {
		if (makeMovieButton == null) {
			makeMovieButton = new JButton();
			makeMovieButton.setText("Make Movie");
			makeMovieButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					makeMovie();
				}
			});
		}
		return makeMovieButton;
	}

	/**
	 * This method initializes stockList
	 * 
	 * @return javax.swing.JList
	 */
	JList getStockList() {
		if (stockList == null) {
			stockList = new JList();
			stockList.setToolTipText("double click to draw chart");
			stockList.addMouseListener(new java.awt.event.MouseAdapter() {
				@Override
				public void mouseClicked(java.awt.event.MouseEvent e) {
					if (e.getClickCount() == 2) {
						MainProgram.getInstance().getLogic()
								.drawChart((Index) getStockList().getSelectedValue());
					}
				}
			});
		}
		return stockList;
	}

	/**
	 * This method initializes stocksPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getStocksPanel() {
		if (stocksPanel == null) {
			GridBagConstraints gridBagConstraints8 = new GridBagConstraints();
			gridBagConstraints8.fill = GridBagConstraints.BOTH;
			gridBagConstraints8.weighty = 1.0;
			gridBagConstraints8.weightx = 1.0;
			GridBagConstraints gridBagConstraints = new GridBagConstraints();
			gridBagConstraints.fill = GridBagConstraints.BOTH;
			gridBagConstraints.gridwidth = 1;
			gridBagConstraints.gridx = -1;
			gridBagConstraints.gridy = -1;
			gridBagConstraints.ipadx = -16;
			gridBagConstraints.ipady = 52;
			gridBagConstraints.weightx = 4.0D;
			gridBagConstraints.weighty = 4.0D;
			gridBagConstraints.gridheight = 1;
			stocksPanel = new JPanel();
			stocksPanel.setLayout(new BoxLayout(getStocksPanel(), BoxLayout.X_AXIS));
			stocksPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			stocksPanel.setPreferredSize(new Dimension(280, 141));
			stocksPanel.add(getStocksScrollPane(), null);
		}
		return stocksPanel;
	}

	/**
	 * This method initializes stocksScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getStocksScrollPane() {
		if (stocksScrollPane == null) {
			stocksScrollPane = new JScrollPane();
			stocksScrollPane.setViewportView(getStockList());
		}
		return stocksScrollPane;
	}

	private void makeMovie() {
		MovieMaker mm = new MovieMaker();
		mm.makeMovieFromDatabase();
	}

	private JButton getBtnStat() {
		if (btnStat == null) {
			btnStat = new JButton("Statistics");
			btnStat.setEnabled(false);
			btnStat.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						MainProgram.getInstance().getLogic().run();
					} catch (ParseException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		return btnStat;
	}
}
