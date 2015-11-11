/**
 * 
 */
package marketone.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import marketone.MainProgram;
import marketone.db.entity.Index;
import marketone.visualization.GraphViewer;
import marketone.visualization.MapPanel;
import marketone.visualization.TreeViewer;

import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.TreeLayout;
import edu.uci.ics.jung.algorithms.shortestpath.MinimumSpanningForest2;
import edu.uci.ics.jung.graph.DelegateForest;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationModel;

/**
 * @author LIU Xiaofan
 * 
 */
public class ViewerPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3868654388119625964L;
	private JButton btnGraph;
	private JButton btnMap;
	private JButton btnTree;
	private JPanel buttonPanel = null;
	private Graph<Index, Integer> g;
	private FRLayout<Index, Integer> layout;
	private MapPanel mp;
	private JButton snapshotButton = null;
	private float weightThreshold;
	private int day;
	private JButton btnThresAdd = null;
	private JLabel weightThresholdLabel = null;
	private JLabel lblThres;
	private JButton btnThresSub = null;
	private JTextField textFieldThres = null;
	private JButton btnTrimNetwork;
	private JLabel lblDay;
	private JTextField textFieldDay;
	private JButton btnDayAdd;
	private JButton btnDaySub;
	private JCheckBox chckbxByRegion;

	/**
 * 
 */
	public ViewerPanel() {
		this.weightThreshold = MainProgram.getInstance().getLogic().getThreshold();
		this.day = MainProgram.getInstance().getLogic().getDay();
		
		addMouseWheelListener(new MouseWheelListener() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.isAltDown())
					System.out.println("scrolled down");
			}
		});
		setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "View Network",
				TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		setLayout(new BorderLayout());
		add(getButtonPanel(), BorderLayout.SOUTH);
	}

	private JButton getBtnDayAdd() {
		if (btnDayAdd == null) {
			btnDayAdd = new JButton("+");
			btnDayAdd.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					day++;
					resetDay();
				}
			});
		}
		return btnDayAdd;
	}

	private JButton getBtnDaySub() {
		if (btnDaySub == null) {
			btnDaySub = new JButton("-");
			btnDaySub.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					day--;
					resetDay();
				}
			});
		}
		return btnDaySub;
	}

	private JButton getBtnGraph() {
		if (btnGraph == null) {
			btnGraph = new JButton("Graph");
			btnGraph.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					vizGraph();
				}
			});
		}
		return btnGraph;
	}

	private JButton getBtnMap() {
		if (btnMap == null) {
			btnMap = new JButton("Map");
			btnMap.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					vizMap();
				}
			});
		}
		return btnMap;
	}

	/**
	 * This method initializes weightThresholdAddButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnThresAdd() {
		if (btnThresAdd == null) {
			btnThresAdd = new JButton();
			btnThresAdd.setText("+");
			btnThresAdd.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					weightThreshold += 0.02f;
					resetWeightThreshold();
				}
			});
		}
		return btnThresAdd;
	}

	/**
	 * This method initializes weightThresholdSubButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getBtnThresSub() {
		if (btnThresSub == null) {
			btnThresSub = new JButton();
			btnThresSub.setText("-");
			btnThresSub.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					weightThreshold -= 0.02f;
					resetWeightThreshold();
				}
			});
		}
		return btnThresSub;
	}

	private JButton getBtnTree() {
		if (btnTree == null) {
			btnTree = new JButton("Tree");
			btnTree.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					vizTree();
				}
			});
		}
		return btnTree;
	}

	private JButton getBtnTrimNetwork() {
		if (btnTrimNetwork == null) {
			btnTrimNetwork = new JButton("Trim Network");
			btnTrimNetwork.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					MainProgram.getInstance().getLogic().trimNetwork();
					refreshViewer();
				}
			});
		}
		return btnTrimNetwork;
	}

	/**
	 * This method initializes buttonPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getButtonPanel() {
		if (buttonPanel == null) {
			lblThres = new JLabel();
			lblThres.setText("Weight:");
			buttonPanel = new JPanel();
			buttonPanel.setLayout(new FlowLayout());
			buttonPanel.add(getBtnGraph());
			buttonPanel.add(getBtnTree());
			buttonPanel.add(getBtnMap());
			buttonPanel.add(getBtnTrimNetwork());
			buttonPanel.add(getChckbxByRegion());
			buttonPanel.add(getSnapshotButton(), null);
			buttonPanel.add(lblThres, null);
			buttonPanel.add(getThresholdTextField(), null);
			buttonPanel.add(getBtnThresAdd(), null);
			buttonPanel.add(getBtnThresSub(), null);
			buttonPanel.add(getLblDay());
			buttonPanel.add(getTextFieldDay());
			buttonPanel.add(getBtnDayAdd());
			buttonPanel.add(getBtnDaySub());
		}
		return buttonPanel;
	}

	/**
	 * Generates a graph: in this case, reads it from current network
	 * 
	 * @param currentNetwork
	 * 
	 * @return A sample undirected graph
	 */
	private Graph<Index, Integer> getGraph() {
		// new graph
		Graph<Index, Integer> g = MainProgram.getInstance().getLogic().getNetwork();
		System.out.println("edge: " + g.getEdgeCount() + "\tvertice: " + g.getVertexCount());
		return g;
	}

	private JLabel getLblDay() {
		if (lblDay == null) {
			lblDay = new JLabel("Day:");
		}
		return lblDay;
	}

	/**
	 * This method initializes snapshotButton
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getSnapshotButton() {
		if (snapshotButton == null) {
			snapshotButton = new JButton();
			snapshotButton.setText("Snap shot");
			snapshotButton.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					snapshot();
				}
			});
		}
		return snapshotButton;
	}

	private JTextField getTextFieldDay() {
		if (textFieldDay == null) {
			textFieldDay = new JTextField(Integer.toString(day));
			textFieldDay.setColumns(3);
			textFieldDay.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_ENTER) {
						try {
							day = Integer.parseInt(getTextFieldDay().getText());
							resetDay();
						} catch (Exception ex) {
							System.err.println(ex.getMessage());
						}
					}
				}
			});
		}
		return textFieldDay;
	}

	/**
	 * This method initializes weightThresholdTextField
	 * 
	 * @return javax.swing.JTextField
	 */
	private JTextField getThresholdTextField() {
		if (textFieldThres == null) {
			textFieldThres = new JTextField(Float.toString(weightThreshold));
			textFieldThres.setColumns(4);
			textFieldThres.addKeyListener(new java.awt.event.KeyAdapter() {
				@Override
				public void keyTyped(java.awt.event.KeyEvent e) {
					if (e.getKeyChar() == KeyEvent.VK_ENTER) {
						try {
							weightThreshold =
									Float.parseFloat(getThresholdTextField().getText());
							resetWeightThreshold();
						} catch (Exception ex) {
							System.err.println(ex.getMessage());
						}
					}
				}

			});
		}
		return textFieldThres;
	}

	private void refreshViewer() {
		for (Component c : getComponents()) {
			if (c.getClass().equals(TreeViewer.class)) {
				vizTree();
			} else if (c.getClass().equals(GraphViewer.class)) {
				vizGraph();
			} else if (c.getClass().equals(MapPanel.class)) {
				vizMap();
			}
		}
	}

	private void resetWeightThreshold() {
		MainProgram.getInstance().getLogic().setThreshold(weightThreshold);
		getThresholdTextField().setText(String.format("%1.2f", weightThreshold));
		getGraph();
		refreshViewer();
	}

	private void resetDay() {
		MainProgram.getInstance().getLogic().setDay(day);
		getTextFieldDay().setText(Integer.toString(day));
		getGraph();
		refreshViewer();
	}

	private void snapshot() {
		for (Component c : getComponents()) {
			if (c.getClass().equals(TreeViewer.class)) {
				Dimension dim = c.getSize();
				Image im = c.createImage(dim.width, dim.height);
				c.paint(im.getGraphics());
				try {
					File imageFile =
							new File("./report/trees/" + day + "." + weightThreshold + ".bmp");
					ImageIO.write((RenderedImage) im, "bmp", imageFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}

			} else if (c.getClass().equals(GraphViewer.class)) {
				Dimension dim = c.getSize();
				Image im = c.createImage(dim.width, dim.height);
				c.paint(im.getGraphics());
				try {
					File imageFile =
							new File("./report/image/" + day + "." + weightThreshold + ".bmp");
					ImageIO.write((RenderedImage) im, "bmp", imageFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			} else if (c.getClass().equals(MapPanel.class)) {
				try {
					File imageFile =
							new File("./report/maps/" + day + "." + weightThreshold + ".bmp");
					ImageIO.write(((MapPanel) c).snapShot(), "bmp", imageFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	private boolean vizCheckNetwork() {
		if (MainProgram.getInstance().getLogic().getNetwork() == null) {
			JOptionPane.showMessageDialog(null, "No MyNetwork Formed!", "MyNetwork Not Found",
					JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	private boolean vizClearContent() {
		for (Component c : getComponents()) {
			if (c.getClass().equals(GraphViewer.class) || c.getClass().equals(TreeViewer.class)
					|| c.getClass().equals(MapPanel.class)) {
				remove(c);
			}
		}
		validate();
		return true;
	}

	private void vizGraph() {
		if (vizCheckNetwork() && vizClearContent()) {
			g = getGraph();
			// layout = new AggregateLayout<Index, MyEdge>(new SpringLayout(g));
			layout = new FRLayout<Index, Integer>(g);
			GraphViewer gv = new GraphViewer(layout, getChckbxByRegion().isSelected());
			gv.setSize(new Dimension(900, 900));
			add(gv, BorderLayout.CENTER);
			revalidate();
		}
	}

	private void vizMap() {
		if (vizCheckNetwork()) {
			for (Component c : getComponents()) {
				if (c.getClass().equals(GraphViewer.class)
						|| c.getClass().equals(TreeViewer.class)
						|| c.getClass().equals(MapPanel.class)) {
					remove(c);
				}
			}
			mp =
					new MapPanel(MainProgram.getInstance().getLogic().getNetwork(),
							weightThreshold);
			add(mp, BorderLayout.CENTER);
			revalidate();
			return;
		}
	}

	@SuppressWarnings("unchecked")
	private void vizTree() {
		if (vizCheckNetwork() && vizClearContent()) {
			g = getGraph();
			MinimumSpanningForest2 prim =
					new MinimumSpanningForest2(g, new DelegateForest(),
							DelegateTree.getFactory(), new ConstantTransformer(1.0));
			Forest tree = prim.getForest();
			// create two layouts for the one graph, one layout for each model
			Layout layout1 = new TreeLayout(tree);
			VisualizationModel vm1 = new DefaultVisualizationModel(layout1);
			TreeViewer tv = new TreeViewer(vm1);
			add(tv, BorderLayout.CENTER);
			revalidate();
		}
	}

	private JCheckBox getChckbxByRegion() {
		if (chckbxByRegion == null) {
			chckbxByRegion = new JCheckBox("By Region");
			chckbxByRegion.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					refreshViewer();
				}
			});
		}
		return chckbxByRegion;
	}
}
