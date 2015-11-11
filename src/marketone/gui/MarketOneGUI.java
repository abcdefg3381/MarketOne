package marketone.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

import maggie.network.gui.GuiUtils;
import marketone.MainProgram;

/**
 * GUI of Client application
 * 
 * @author LIU Xiaofan
 * 
 */
public class MarketOneGUI extends JFrame {

	/**
	 * random serial version uid is given
	 */
	private static final long serialVersionUID = -1046660606841733678L;

	private JPanel baseContentPane = null;

	private NetworkPanel controlPanel = null;

	private ViewerPanel drawPanel = null;

	private MarketOneMenuBar menuBar = null;
	private JSplitPane splitPane = null;

	/**
	 * This method initializes
	 * 
	 */
	public MarketOneGUI() {
		super();
		initialize();
	}

	public void fetchData() {
		getControlPanel().getStockList().setListData(
				MainProgram.getInstance().getLogic().getIndexList());
	}

	/**
	 * This method initializes baseContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getBaseContentPane() {
		if (baseContentPane == null) {
			baseContentPane = new JPanel();
			baseContentPane.setLayout(new BorderLayout(0, 0));
			baseContentPane.add(getSplitPane());
		}
		return baseContentPane;
	}

	/**
	 * This method initializes listPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private NetworkPanel getControlPanel() {
		if (controlPanel == null) {
			controlPanel = new NetworkPanel();
		}
		return controlPanel;
	}

	/**
	 * This method initializes drawPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private ViewerPanel getDrawPanel() {
		if (drawPanel == null) {
			drawPanel = new ViewerPanel();
		}
		return drawPanel;
	}

	/**
	 * This method initializes jMenuBar
	 * 
	 * @return javax.swing.JMenuBar
	 */
	private MarketOneMenuBar getjMenuBar() {
		if (menuBar == null) {
			menuBar = new MarketOneMenuBar();
		}
		return menuBar;
	}

	/**
	 * This method initializes splitPane
	 * 
	 * @return javax.swing.JSplitPane
	 */
	private JSplitPane getSplitPane() {
		if (splitPane == null) {
			splitPane = new JSplitPane();
			splitPane.setLeftComponent(getControlPanel());
			splitPane.setRightComponent(getDrawPanel());
		}
		return splitPane;
	}

	/**
	 * This method initializes this
	 * 
	 */
	private void initialize() {
		this.setContentPane(getBaseContentPane());
		this.setJMenuBar(getjMenuBar());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getResource("icon.gif")));
		this.setPreferredSize(new Dimension(1400, 700));
		this.setTitle("Project MarketOne");
		this.pack();
		GuiUtils.centerWindow(this);
		this.setVisible(true);
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				MainProgram.getInstance().exit();
			}
		});
	}
}