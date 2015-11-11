/**
 * 
 */
package marketone.visualization;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import maggie.common.utils.MaggieReader;
import marketone.MainProgram;
import marketone.db.entity.Index;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

/**
 * @author LIU Xiaofan
 * 
 */
public class MovieMaker {
	public static void main(String[] args) throws IOException {
		MovieMaker mm = new MovieMaker();
		// mm.makeMovieFromDatabase();
		mm.makeMovieFromFile();
	}

	/**
	 * @throws IOException
	 * 
	 */
	private void makeMovieFromFile() throws IOException {
		// read indices from file.
		ArrayList<Index> indexList = new ArrayList<Index>();
		MaggieReader in = new MaggieReader(new File("./dataset/indices67"));
		String line;
		String[] sgmt;
		while ((line = in.readLine()) != null) {
			sgmt = line.split("\t");
			Index index =
					new Index(Integer.parseInt(sgmt[0]), sgmt[1], sgmt[2], sgmt[3], sgmt[4]);
			indexList.add(index);

		}
		in.close();
		// read correlation matrix
		int numberIndices = 67;
		int nbDays = 818;
		double[][][] adjMatrices = new double[nbDays - 1][numberIndices][numberIndices];
		in = new MaggieReader(new File("./dataset/DCC.corr"));
		int lineCount = 0;
		try {
			while ((line = in.readLine()) != null) {
				sgmt = line.split(",");
				for (int position = 0; position < sgmt.length; position++) {
					adjMatrices[position / numberIndices][lineCount][position % numberIndices] =
							Double.parseDouble(sgmt[position]);
				}
				lineCount++;
			}
		} catch (Exception e) {
			System.out.println(line);
		}
		in.close();
		// draw network for each time
		double[][] weightMatrix;
		int[] vp = new int[] { 168, 185, 292, 297, 440, 482, 549, 610 };
		for (final int t : vp) {
			// form network
			MainProgram.getInstance().getLogic().setDay(t);
			MainProgram.getInstance().getLogic().trimNetwork();
			Graph<Index, Integer> network = MainProgram.getInstance().getLogic().getNetwork();
			// draw graph to panel
			final GraphViewer gv = new GraphViewer(new FRLayout<Index, Integer>(network), true);
			final DefaultModalGraphMouse graphMouse = new DefaultModalGraphMouse();
			// graphMouse.setMode(Mode.PICKING);
			gv.setGraphMouse(graphMouse);
			gv.addKeyListener(new KeyListener() {

				@Override
				public void keyTyped(KeyEvent e) {
					if (e.getKeyChar() == 'r') {
						Dimension dim = gv.getSize();
						Image im = gv.createImage(dim.width, dim.height);
						gv.paint(im.getGraphics());
						try {
							File imageFile = new File("./Day" + t + ".png");
							ImageIO.write((RenderedImage) im, "png", imageFile);
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}

				@Override
				public void keyReleased(KeyEvent e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void keyPressed(KeyEvent e) {
					// TODO Auto-generated method stub

				}
			});
			// show
			JFrame frame = new JFrame("day " + t);
			frame.add(gv);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setSize(800, 800);
			frame.setVisible(true);
		}
	}

	private static Calendar c = new GregorianCalendar();
	private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

	/**
	 * 
	 */
	public void makeMovieFromDatabase() {
		// TODO Major logic change
		// // start database connector
		// MainProgram.getInstance().getLogic().bootstrapStart();
		// long t = System.currentTimeMillis();
		// MapPanel frame = null;
		// MarketNetwork myNetwork = null;
		// NetworkPreprocessor np = new NetworkPreprocessor();
		// File imageFile = null;
		// int days = 200;
		// // date = 2009-09-10
		// try {
		// c.setTime(df.parse("2009-04-23"));
		// } catch (ParseException e) {
		// e.printStackTrace();
		// }
		// // for each day, draw a frame
		// for (int i = 0; i < days; c.set(Calendar.DATE, c.get(Calendar.DATE) -
		// 1)) {
		// // ignore weekends
		// if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
		// || c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
		// continue;
		// }
		// if ((i + 1) % 10 == 0) {
		// System.out.println("frame: " + (i + 1) + " of " + days);
		// }
		// // form network
		// myNetwork = new MarketNetwork();
		// myNetwork.setEndDate(c.getTime());
		// myNetwork.setWindowSize(20);
		// List<Index> allIndex =
		// MainProgram.getInstance().getLogic()
		// .getIndexList(c.getTime(), 20, Index.regions[3]);
		// allIndex.remove(32);
		// myNetwork.setNodeList(allIndex);
		// myNetwork.setStartingRegion(0);
		// myNetwork.setName(df.format(c.getTime()));
		// np.setNetwork(myNetwork);
		// // XXX add threshold
		// np.formEdges(0.86f);
		// frame = new MapPanel(myNetwork, 0.86f);
		// try {
		// imageFile = new File("./report/movie/" + myNetwork.getName() +
		// ".jpg");
		// ImageIO.write(frame.snapShot(), "jpg", imageFile);
		// } catch (IOException e1) {
		// e1.printStackTrace();
		// }
		// i++;
		// }
		// System.out.println("frames rendered in " +
		// (System.currentTimeMillis() - t) + "ms.");
		// // close database connector
		// MainProgram.getInstance().exit();
	}

}
