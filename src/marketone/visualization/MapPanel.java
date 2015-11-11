package marketone.visualization;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import marketone.db.entity.Index;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

/**
 * This class draws a world map, cities and lines among cities on a JPanel. Use
 * constructor public MapPanel(UndirectedSparseGraph myNetwork, float
 * weightThreshold) to initialize the class. To output the JPanel to a image
 * file, use the snapshot() function.
 * 
 * @author LIU Xiaofan, xfliu@eie.polyu.edu.hk
 * 
 */
public class MapPanel extends JPanel implements MouseListener, MouseMotionListener,
		MouseWheelListener {

	private static final long serialVersionUID = 8777430144233259580L;
	private static int mapWidth;
	private static int mapHeight;
	private int destinatex = 0;
	private int destinatey = 0;
	private BufferedImage mapImage;
	private Graph<Index, Integer> myNetwork;
	private int offsetx = 0;
	private int offsety = 0;
	private float scale = 1.0f;
	private int tempx = 0;
	private int tempy = 0;
	private float weightThreshold;
	private int iDToffset = 0;

	public MapPanel() {
		super();
		try {
			mapImage = ImageIO.read(new File("00-2560.jpg"));
			mapWidth = mapImage.getWidth();
			mapHeight = mapImage.getHeight();
		} catch (IOException e) {
			e.printStackTrace();
		}
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);
	}

	public MapPanel(Graph<Index, Integer> myNetwork, float weightThreshold) {
		this();
		setNetwork(myNetwork, weightThreshold);
	}

	/**
	 * @param g2d
	 */
	private void drawImageCities(Graphics2D g2d) {
		// draw cities on map
		int cr = 10; // city radiation
		g2d.setColor(Color.red);
		for (Index s : myNetwork.getVertices()) {
			g2d.fill(new Ellipse2D.Float((s.getLongitude() + 180) * mapWidth / 360 - cr, (s
					.getLatitude() + 90) * mapHeight / 180 - cr, cr * 2, cr * 2));
		}
	}

	/**
	 * @param g2d
	 */
	private void drawImageEdges(Graphics2D g2d) {
		// TODO major change of logic, use draw edges on map
		// g2d.setStroke(new BasicStroke(2.0f));
		// for (MarketEdge e : myNetwork.getEdgeList()) {
		// if (e.getPair().getFirst().getID() != 21 &&
		// e.getPair().getSecond().getID() != 21)
		// continue;
		// if (e.getWeight() > weightThreshold) {
		// g2d.setColor(getColor(e.getWeight()));
		// // g2d.setColor(Color.red);
		// g2d.draw(new Line2D.Float(((e.getPair().getFirst()).getLongitude() +
		// 180)
		// * mapWidth / 360, ((e.getPair().getFirst()).getLatitude() + 90)
		// * mapHeight / 180, ((e.getPair().getSecond()).getLongitude() + 180)
		// * mapWidth / 360, ((e.getPair().getSecond()).getLatitude() + 90)
		// * mapHeight / 180));
		// }
		// }
	}

	/**
	 * @param exportImage
	 * @param g2d
	 * 
	 */
	private void drawImageMap(Graphics2D g2d) {
		// left of financial day line
		g2d.drawImage(mapImage, 0, 0, (mapWidth * iDToffset / 24), mapHeight, (24 - iDToffset)
				* mapWidth / 24, 0, 2560, mapHeight, this);
		// right of financial day line
		g2d.drawImage(mapImage, (mapWidth * iDToffset / 24), 0, mapWidth, mapHeight, 0, 0,
				(24 - iDToffset) * mapWidth / 24, mapHeight, this);
		// display time
		g2d.setColor(Color.black);
		g2d.setFont(new Font("sansserif", Font.BOLD, 48));
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		// g2d.drawString( df.format(myNetwork.getStartDate()) + " - " +
		// df.format(myNetwork.getEndDate()), 1000, 1250);
	}

	/**
	 * @param g
	 * @param ratio
	 * 
	 */
	private void drawPanelCities(Graphics2D g2d, float ratio) {
		// draw cities on panel
		g2d.setColor(Color.red);
		for (Index s : myNetwork.getVertices()) {
			g2d.fill(new Ellipse2D.Float(destinatex + offsetx + ratio
					* (256 + s.getLongitude() * 512 / 360) - 5, destinatey + offsety + ratio
					* (128 + s.getLatitude() * 256 / 180) - 5, 10, 10));
		}
	}

	/**
	 * @param ratio
	 */
	private void drawPanelEdges(Graphics2D g2d, float ratio) {
		// TODO major change of logic, user edgeweight map.
		// draw edges on panel
		// g2d.setStroke(new BasicStroke(0.5f));
		// for (MarketEdge e : myNetwork.getEdgeList()) {
		// if (e.getWeight() > weightThreshold) {
		// g2d.setColor(getColor(e.getWeight()));
		// g2d.draw(new Line2D.Float(destinatex + offsetx + ratio
		// * (256 + (e.getPair().getFirst()).getLongitude() * 512 / 360),
		// destinatey + offsety + ratio
		// * (128 + (e.getPair().getFirst()).getLatitude() * 256 / 180),
		// destinatex + offsetx + ratio
		// * (256 + (e.getPair().getSecond()).getLongitude() * 512 / 360),
		// destinatey + offsety + ratio
		// * (128 + (e.getPair().getSecond()).getLatitude() * 256 / 180)));
		// }
		// }
	}

	/**
	 * @param g2d
	 * @param ratio
	 */
	private void drawPanelMap(Graphics2D g2d, float ratio) {
		// left of financial day line
		g2d.drawImage(mapImage, destinatex + offsetx, destinatey + offsety, destinatex
				+ offsetx + (int) (ratio * 512 * iDToffset / 24), destinatey + offsety
				+ (int) (ratio * 256), (24 - iDToffset) * mapWidth / 24, 0, mapWidth,
				mapHeight, this);
		// right of financial day line
		g2d.drawImage(mapImage, destinatex + offsetx + (int) (ratio * 512 * iDToffset / 24),
				destinatey + offsety, destinatex + offsetx + (int) (ratio * 512), destinatey
						+ offsety + (int) (ratio * 256), 0, 0,
				(24 - iDToffset) * mapWidth / 24, mapHeight, this);
	}

	/**
	 * @param weight
	 * @return
	 */
	private Color getColor(float weight) {
		int r = 0, g = 0, b = 0, c = 4;
		int value =
				c * 255 - (int) ((weight - weightThreshold) * c * 255 / (1 - weightThreshold));
		int range = value / 255;
		switch (range) {
		case 0:
			r = 255;
			g = value;
			b = 0;
			break;
		case 1:
			r = 255 * 2 - value;
			g = 255;
			b = 0;
			break;
		case 2:
			r = 0;
			g = 255;
			b = value - 255 * 2;
			break;
		case 3:
			r = 0;
			g = 255 * 4 - value;
			b = 255;
			break;
		default:
			r = g = b = 255;
			break;
		}
		return new Color(r, g, b);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent
	 * )
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		offsetx = e.getX() - tempx;
		offsety = e.getY() - tempy;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		tempx = e.getX();
		tempy = e.getY();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		destinatex += offsetx;
		destinatey += offsety;
		offsetx = 0;
		offsety = 0;
		repaint(0);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seejava.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.
	 * MouseWheelEvent)
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// zoom in
		if (e.getWheelRotation() < 0) {
			scale *= 1.1f;
			destinatex -= 0.1f * (e.getX() - destinatex);
			destinatey -= 0.1f * (e.getY() - destinatey);
		}
		// zoom out
		else {
			scale *= 0.9f;
			destinatex += 0.1f * (e.getX() - destinatex);
			destinatey += 0.1f * (e.getY() - destinatey);
		}
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		// calculate ratio
		float wRatio, hRatio, ratio;
		wRatio = (float) this.getWidth() / 256;
		hRatio = (float) this.getHeight() / 128;
		if (wRatio > hRatio) {
			ratio = hRatio;
		} else {
			ratio = wRatio;
		}
		ratio *= scale;
		// draw image
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.red);
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
		drawPanelMap(g2d, ratio);
		drawPanelCities(g2d, ratio);
		drawPanelEdges(g2d, ratio);
	}

	/**
	 * @param weightThreshold2
	 * @param network2
	 */
	public void setNetwork(Graph<Index, Integer> myNetwork, float weightThreshold) {
		this.myNetwork = myNetwork;
		this.weightThreshold = weightThreshold;
		Properties ps = new Properties();
		try {
			ps.load(new FileInputStream("./dataset/cdnt.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		float lgtd, lttd;
		for (Index s : myNetwork.getVertices()) {
			try {
				lgtd =
						Float.parseFloat(ps.getProperty(s.getCountry().toUpperCase())
								.split(",")[0]) + iDToffset * 360 / 24;
				if (lgtd > 180) {
					lgtd -= 360;
				}
				lttd =
						Float.parseFloat(ps.getProperty(s.getCountry().toUpperCase())
								.split(",")[1]);
			} catch (Exception e) {
				// perhaps regional index
				System.out.println(e.getMessage());
				lgtd = lttd = 0;
			}
			s.setLongitude(lgtd);
			s.setLatitude(lttd);
		}
	}

	/**
	 * @return
	 */
	public BufferedImage snapShot() {
		BufferedImage exportImage =
				new BufferedImage(mapWidth, mapHeight, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = exportImage.createGraphics();
		drawImageMap(g2d);
		drawImageCities(g2d);
		drawImageEdges(g2d);
		return exportImage;
	}
}