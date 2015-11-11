package marketone.visualization;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import maggie.network.gui.GuiUtils;
import marketone.db.entity.Index;
import marketone.db.entity.Price;

/**
 * This class draws a network as MIDI piano roll plays on pop-up plane.<br>
 * This class is also able to skyline transfer one network and show them in red
 * color.
 * 
 * @author LIU Xiaofan
 * 
 */
public class DrawChart {

	class PriceVisualizer extends JComponent {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2942065431267793521L;

		private float divideHrz;
		private float divideVtc;
		private float highestClosing;
		private float lowestClosing;

		private int startID;

		public PriceVisualizer() {
			super();
			lowestClosing = Float.MAX_VALUE;
			highestClosing = Float.MIN_VALUE;
			for (Price p : index.getPriceList()) {
				if (p.getAdjClose() > highestClosing) {
					highestClosing = p.getAdjClose();
				}
				if (p.getAdjClose() < lowestClosing) {
					lowestClosing = p.getAdjClose();
				}
			}
			startID = index.getPriceList().get(0).getId();
			divideVtc = (index.getPriceList().size()) / 800;
			divideHrz = (highestClosing - lowestClosing) / 400;
		}

		@Override
		public void paint(Graphics g) {
			Graphics2D g2d = (Graphics2D) g;
			int x1, y1, x2, y2;
			for (int i = 1; i < index.getPriceList().size(); i++) {
				x1 =
						800 - (int) ((index.getPriceList().get(i - 1).getId() - startID) / divideVtc);
				y1 =
						400 - (int) ((index.getPriceList().get(i - 1).getAdjClose() - lowestClosing) / divideHrz);
				x2 = 800 - (int) ((index.getPriceList().get(i).getId() - startID) / divideVtc);
				y2 =
						400 - (int) ((index.getPriceList().get(i).getAdjClose() - lowestClosing) / divideHrz);
				g2d.drawLine(x1, y1, x2, y2);
			}
			this.setPreferredSize(new Dimension(800, 400));
		}
	}

	private JPanel buttonPanel = null;

	private JButton closeButton = null;

	protected Index index;

	private JPanel jContentPane = null;

	private JFrame jFrame = null;

	private JScrollPane jScrollPane = null;

	private PriceVisualizer mViz = null;

	private JButton snapshotButton = null;

	public DrawChart(Index index) {
		this.index = index;
		initialize();
	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());
			buttonPanel.add(getSnapshotButton(), null);
			buttonPanel.add(getCloseButton(), null);
		}
		return buttonPanel;
	}

	/**
	 * This method initializes closeButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCloseButton() {
		if (closeButton == null) {
			closeButton = new JButton();
			closeButton.setText("CLOSE");
			closeButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					jFrame.dispose();
				}
			});
		}
		return closeButton;
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getJScrollPane(), BorderLayout.CENTER);
			jContentPane.add(getButtonPanel(), BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jFrame
	 * 
	 * @return javax.swing.JFrame
	 */
	private JFrame getJFrame() {
		if (jFrame == null) {
			jFrame = new JFrame(index.getName() + " " + index.getCountry());
			jFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			jFrame.setContentPane(getJContentPane());
			jFrame.setFocusable(true);
			jFrame.addKeyListener(new KeyAdapter() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * java.awt.event.KeyAdapter#keyTyped(java.awt.event.KeyEvent)
				 */
				@Override
				public void keyTyped(KeyEvent e) {
					if (e.getKeyCode() == 0) {
						jFrame.dispose();
					}
				}
			});
			jFrame.pack();
		}
		return jFrame;
	}

	/**
	 * This method initializes jScrollPane
	 * 
	 * @return javax.swing.JScrollPane
	 */
	private JScrollPane getJScrollPane() {
		if (jScrollPane == null) {
			jScrollPane = new JScrollPane();
			jScrollPane.setPreferredSize(new Dimension(850, 450));
			jScrollPane.setViewportBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
			jScrollPane.setViewportView(getMIDIVisualizer());
		}
		return jScrollPane;
	}

	private JComponent getMIDIVisualizer() {
		if (mViz == null) {
			mViz = new PriceVisualizer();
		}
		return mViz;
	}

	/**
	 * This method initializes snapshotButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSnapshotButton() {
		if (snapshotButton == null) {
			snapshotButton = new JButton();
			snapshotButton.setText("Snap Shot");
			snapshotButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					snapshot();
				}
			});
		}
		return snapshotButton;
	}

	private void initialize() {
		GuiUtils.centerWindow(getJFrame());
		getJFrame().setVisible(true);
	}

	private void snapshot() {
		Dimension dim = mViz.getPreferredSize();
		Image im = mViz.createImage(dim.width, dim.height);
		mViz.paint(im.getGraphics());
		try {
			File imageFile = new File("./report/image/" + index.getName() + ".jpg");
			ImageIO.write((RenderedImage) im, "jpg", imageFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}