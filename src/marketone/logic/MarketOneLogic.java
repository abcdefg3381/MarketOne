package marketone.logic;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import maggie.common.algorithm.MatrixUtils;
import maggie.common.utils.MaggieReader;
import maggie.common.utils.ReportWriter;
import maggie.network.gui.GuiUtils;
import marketone.db.entity.Index;
import marketone.visualization.DrawChart;
import marketone.visualization.GraphViewerSimple;

import org.apache.commons.collections15.Factory;
import org.apache.commons.lang.ArrayUtils;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.SparseDoubleMatrix2D;
import edu.uci.ics.jung.algorithms.cluster.WeakComponentClusterer;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.matrix.GraphMatrixOperations;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * @author LIU Xiaofan
 * 
 */
public class MarketOneLogic extends ReportWriter {
	class EdgeFactory implements Factory<Integer> {
		int i = 0;

		@Override
		public Integer create() {
			return new Integer(i++);
		}
	}

	class GraphFactory implements Factory<Graph<Index, Integer>> {
		@Override
		public UndirectedSparseGraph<Index, Integer> create() {
			return new UndirectedSparseGraph<Index, Integer>();
		}
	}

	class VertexFactory implements Factory<Index> {
		int i = 0;

		@Override
		public Index create() {
			return indexList.get(i++);
		}
	}

	// matlab folder
	public static String MATLABFOLDER = "C:/Users/EF501b/workspace_matlab/MarketOne/";

	// network attributes
	private float[][][] adjMatrices; // window-index-index

	// public static void main(String[] args) {
	// MarketOneLogic pc = new MarketOneLogic();
	// try {
	// pc.run();
	// } catch (ParseException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }

	private float[][] assortativity;
	private int day = 1;
	private int[][] edgeWeightDistributions; // window-dist
	private List edgeWeights;
	// raw data index-day
	private float[][] indexClosePrices;
	// index network facts
	private List<Index> indexList;
	private float[][] indexLogReturns;
	private float[][] indexVolatility; // window-index
	private Graph<Index, Integer> network = null;
	private float[] networkSyn; // window
	private int[][] nodeStrengthDistribution; // window-dist
	private float[][] nodeStrengths; // window-index
	private int numberDays;
	private int numberIndices;
	private int numberWindows;
	private float threshold = 0.5f;
	private float[] worldClosePrices;
	private float[] worldLogReturns;
	private float[] worldVolatility;

	public MarketOneLogic() {
		super();
		try {
			readData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * p_i=avg(rho_ij) <br>
	 * q_i=avg(rho_ik) <br>
	 * assortativity r = avg(p_i-q_i) <br>
	 * 
	 * @param file
	 */
	private void calcMarketAssort(File file) {
		assortativity = new float[numberWindows][3];
		// three market categories
		int[] marketSize = new int[3];
		// initialize variables
		int category;
		float pi, qi;
		// calculate category sizes
		for (Index index : indexList) {
			marketSize[index.getCategory()]++;
		}
		// begin assortativity calculation
		for (int t = 0; t < numberWindows; t++) {
			for (int i = 0; i < numberIndices; i++) {
				category = indexList.get(i).getCategory();
				pi = qi = 0;
				// calculate pi and qi
				for (int j = i + 1; j < numberIndices; j++) {
					if (category == indexList.get(j).getCategory()) {
						pi += adjMatrices[t][i][j];
					} else {
						qi += adjMatrices[t][i][j];
					}
				}
				pi /= (marketSize[category] - 1);
				qi /= (indexList.size() - marketSize[category]);
				assortativity[t][category] += (pi - qi) / 2;
			}
			for (int cate = 0; cate < 3; cate++) {
				assortativity[t][cate] /= marketSize[cate];
			}
		}
		// output
		System.out.println("Market Category & Market Size & Market Assortativity r\\\\");
		setPrintWriter(file);
		for (int t = 0; t < numberWindows; t++) {
			print(assortativity[t][0] + "\t");
		}
		println();
		for (int t = 0; t < numberWindows; t++) {
			print(assortativity[t][1] + "\t");
		}
		println();
		for (int t = 0; t < numberWindows; t++) {
			print(assortativity[t][2] + "\t");
		}
		println();
		closePrintWriter();
	}

	/**
	 * p_i=avg(rho_ij) <br>
	 * q_i=avg(rho_ik) <br>
	 * assortativity r = avg(p_i-q_i) <br>
	 * 
	 * @param file
	 */
	private void calcRegionAssort(File file) {
		assortativity = new float[numberWindows][4];
		// three market categories
		int[] regionSize = new int[4];
		// initialize variables
		int category;
		float pi, qi;
		// calculate category sizes
		for (Index index : indexList) {
			regionSize[index.getRegionIndex()]++;
		}
		// begin assortativity calculation
		for (int t = 0; t < numberWindows; t++) {
			for (int i = 0; i < numberIndices; i++) {
				category = indexList.get(i).getRegionIndex();
				pi = qi = 0;
				// calculate pi and qi
				for (int j = i + 1; j < numberIndices; j++) {
					if (category == indexList.get(j).getRegionIndex()) {
						pi += adjMatrices[t][i][j];
					} else {
						qi += adjMatrices[t][i][j];
					}
				}
				pi /= (regionSize[category] - 1);
				qi /= (indexList.size() - regionSize[category]);
				assortativity[t][category] += (pi - qi) / 2;
			}
			for (int cate = 0; cate < 4; cate++) {
				assortativity[t][cate] /= regionSize[cate];
			}
		}
		// output
		System.out.println("Market Category & Market Size & Market Assortativity r\\\\");
		setPrintWriter(file);
		for (int t = 0; t < numberWindows; t++) {
			print(assortativity[t][0] + "\t");
		}
		println();
		for (int t = 0; t < numberWindows; t++) {
			print(assortativity[t][1] + "\t");
		}
		println();
		for (int t = 0; t < numberWindows; t++) {
			print(assortativity[t][2] + "\t");
		}
		println();
		for (int t = 0; t < numberWindows; t++) {
			print(assortativity[t][3] + "\t");
		}
		closePrintWriter();
	}

	/**
	 * Find community structure in the network. Edges with minimum weights are
	 * removed first.
	 * 
	 * Result is not good. Changed to categorize with node strength
	 */
	private void communityStructure() {
		// calculate average adjacency matrix
		double[][] adjMatricesAverage =
				new double[adjMatrices[0].length][adjMatrices[0].length];
		for (int date = 0; date < adjMatrices.length; date++) {
			for (int i = 0; i < adjMatricesAverage.length; i++) {
				for (int j = 0; j < adjMatricesAverage.length; j++) {
					adjMatricesAverage[i][j] += adjMatrices[date][i][j];
					adjMatricesAverage[i][j] += adjMatrices[date][j][i];
				}
			}
		}
		for (int i = 0; i < adjMatricesAverage.length; i++) {
			for (int j = 0; j < adjMatricesAverage.length; j++) {
				adjMatricesAverage[i][j] /= adjMatrices.length;
			}
		}
		// convert the matrix to graph
		DoubleMatrix2D matrix = new SparseDoubleMatrix2D(adjMatricesAverage);
		Map<Integer, Number> edgeweight = new HashMap<Integer, Number>();
		Graph<Index, Integer> network =
				GraphMatrixOperations.matrixToGraph(matrix, new GraphFactory(),
						new VertexFactory(), new EdgeFactory(), edgeweight);
		// remove minimum weight edge, look for weak component
		List weights = new ArrayList(edgeweight.values());
		Collections.sort(weights);
		WeakComponentClusterer finder = new WeakComponentClusterer();
		Set<Set> clusters = null;
		for (Object weight : weights) {// start from minimum weight edge
			Integer edgeToRemove = null;
			for (Integer edge : network.getEdges()) {
				if (edgeweight.get(edge).floatValue() < 0.98) {
					// if (edgeweight.get(edge) == weight) {
					edgeToRemove = edge;
					break;
				}
			}
			network.removeEdge(edgeToRemove);
			clusters = finder.transform(network);// find clusters
			if (clusters.size() > 40)
				break;
		}
		// output weak component index names
		for (Set<Integer> cluster : clusters) {
			for (Integer i : cluster) {
				System.out.print(indexList.get(i).getCountry() + ",");
			}
			System.out.println();
		}
	}

	/**
	 * @param day2
	 * @param threshold2
	 */
	private void constructNetwork() {
		// initialize matrix
		double[][] matrix = new double[numberIndices][numberIndices];
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				matrix[i][j] = (double) adjMatrices[day-1][i][j];
			}
		}
		// form network
		DoubleMatrix2D matrix2D = new SparseDoubleMatrix2D(matrix);
		Map<Integer, Number> edgeweight = new HashMap<Integer, Number>();
		network =
				GraphMatrixOperations.matrixToGraph(matrix2D, new GraphFactory(),
						new VertexFactory(), new EdgeFactory(), edgeweight);
		// remove edge weight |rho|<threshold,
		edgeWeights = new ArrayList(edgeweight.values());
		for (Object weight : edgeWeights) {// start from minimum weight edge
			Integer edgeToRemove = null;
			for (Integer edge : network.getEdges()) {
				if (edgeweight.get(edge).floatValue() < threshold) {
					edgeToRemove = edge;
				}
			}
			network.removeEdge(edgeToRemove);
		}
		System.out.println(network.getVertexCount() + ":" + network.getEdgeCount());
	}

	private void countDistribution() {
		// this.standardDeviations[day] = standardDeviations;
		// this.nodeStrengths[day] = nodeStrengths;
		// this.correlationDistributions[day] = corrDistribution;

		// edge weight distributions and network synchronization and node
		// strength
		for (int t = 0; t < numberWindows; t++) {
			int[] edgeWeightDistribution = new int[20];
			for (int i = 0; i < numberIndices; i++) {
				for (int j = i + 1; j < numberIndices; j++) {
					networkSyn[t] += adjMatrices[t][i][j] / (67 * 66 / 2);
					nodeStrengths[t][i] += adjMatrices[t][i][j] / 66;
					nodeStrengths[t][j] += adjMatrices[t][i][j] / 66;
					int bin = (int) (adjMatrices[t][i][j] / 0.1f + 10);
					if (bin == 20)
						bin = 19;
					edgeWeightDistribution[bin]++;
				}
			}
			edgeWeightDistributions[t] = edgeWeightDistribution;
		}
		// this.nodeStrengthDistribution[day] = nsDistribution;

		printMatrix(nodeStrengths, new File("node.strength"));
		printMatrix(networkSyn, new File("network.syn"));

	}

	/**
	 * 
	 */
	private void customizedPrint() {
		setPrintWriter(new File(MATLABFOLDER + "./hkus.strength"));
		// node strength of hong kong (23) and us (72)
		for (int window = 0; window < numberWindows; window++) {
			getPrintWriter().print(nodeStrengths[window][20] + "\t");
		}
		println();
		for (int window = 0; window < numberWindows; window++) {
			getPrintWriter().print(nodeStrengths[window][65] + "\t");
		}
		println();
		closePrintWriter();

		// network syn
		setPrintWriter(new File(MATLABFOLDER + "./network.syn"));
		System.out.print("netsyn=[");
		for (int i = 0; i < numberWindows; i++) {
			getPrintWriter().print(networkSyn[i] + "\t");
		}
		System.out.println("];");
		getPrintWriter().println();
		closePrintWriter();

		// // window return of hong kong and world
		// for (int index = 0; index < indexList.size(); index++) {
		// if (indexList.get(index).getID() == 23) {
		// System.out.print("hkpr=[");
		// for (int window = 0; window < numberWindows; window++) {
		// getPrintWriter().print(priceReturn[window][index] + "\t");
		// }
		// System.out.println("];");
		// getPrintWriter().println();
		// }
		// }
		// System.out.print("wdpr=[");
		// for (int window = 0; window < numberWindows; window++) {
		// getPrintWriter().print(worldPriceReturn[window] + "\t");
		// }
		// System.out.println("];");
		// getPrintWriter().println();
		//
		// // window average of hong kong and world
		// for (int index = 0; index < indexList.size(); index++) {
		// if (indexList.get(index).getID() == 23) {
		// System.out.print("hkavg=[");
		// for (int window = 0; window < numberWindows; window++) {
		// getPrintWriter().print(priceMeans[window][index] + "\t");
		// }
		// System.out.println("];");
		// getPrintWriter().println();
		// }
		// }
		// System.out.print("wdavg=[");
		// for (int window = 0; window < numberWindows; window++) {
		// getPrintWriter().print(worldPriceMeans[window] + "\t");
		// }
		// System.out.println("];");
		// getPrintWriter().println();
		//
		// // window volatility of hong kong and world
		// for (int index = 0; index < indexList.size(); index++) {
		// if (indexList.get(index).getID() == 23) {
		// System.out.print("hkstd=[");
		// for (int window = 0; window < numberWindows; window++) {
		// getPrintWriter().print(standardDeviations[window][index] + "\t");
		// }
		// System.out.println("];");
		// getPrintWriter().println();
		// }
		// }
		// System.out.print("wdstd=[");
		// for (int window = 0; window < numberWindows; window++) {
		// getPrintWriter().print(worldStandardDeviations[window] + "\t");
		// }
		// System.out.println("];");
		// getPrintWriter().println();

	}

	/**
	 * @param selectedValue
	 */
	public void drawChart(Index index) {
		new DrawChart(index);
	}

	public int getDay() {
		return day;
	}

	/**
	 * @return
	 */
	public Index[] getIndexList() {
		return indexList.toArray(new Index[indexList.size()]);
	}

	/**
	 * @return
	 */
	public Graph getNetwork() {
		if (this.network == null)
			constructNetwork();
		return this.network;
	}

	public float getThreshold() {
		return threshold;
	}

	/**
	 * Initializes the arrays
	 */
	private void initializeMatrices() {
		System.out.println("initializing matrices...");
		numberDays = 818;
		numberWindows = numberDays - 1;
		numberIndices = indexList.size();
		// network
		edgeWeightDistributions = new int[numberWindows][21];
		nodeStrengthDistribution = new int[numberWindows][21];
		nodeStrengths = new float[numberWindows][numberIndices];
		networkSyn = new float[numberWindows];
	}

	/**
	 * Prints the 2-D Integer matrix (distribution)
	 * 
	 * @param dist
	 * @param file
	 */
	private void printDistribution(int[][] dist, File file) {
		setPrintWriter(file);
		// table head
		for (int i = -10; i < 10; i += 1) {
			System.out.print(String.format("\t %.1f to %.1f", i / 10f, (i + 1) / 10f));
		}
		System.out.println();
		// table body
		for (int i = 0; i < dist.length; i++) {
			for (int j = 0; j < dist[0].length; j++) {
				getPrintWriter().print(dist[i][j] + "\t");
			}
			getPrintWriter().println();
		}
		closePrintWriter();
	}

	/**
	 * Prints the 2-D Integer matrix (distribution)
	 * 
	 * @param dist
	 * @param file
	 */
	private void printMatrix(float[] vector, File file) {
		setPrintWriter(file);
		// table body
		for (int i = 0; i < vector.length; i++) {
			// getPrintWriter().print("row " + i + "\t");
			getPrintWriter().println("" + vector[i]);
		}
		closePrintWriter();
	}
	/**
	 * Prints the 2-D Integer matrix (distribution)
	 * 
	 * @param dist
	 * @param file
	 */
	private void printMatrix(float[][] matrix, File file) {
		setPrintWriter(file);
		// table head
		// for (int i = 0; i < matrix[0].length; i ++) {
		// getPrintWriter().print("\t"+"column " + i );
		// }
		// getPrintWriter().println();
		// table body
		for (int i = 0; i < matrix.length; i++) {
			// getPrintWriter().print("row " + i + "\t");
			for (int j = 0; j < matrix[0].length; j++) {
				getPrintWriter().print(matrix[i][j] + "\t");
			}
			getPrintWriter().println();
		}
		closePrintWriter();
	}
	/**
	 * Calculates and prints the correlation between <b>windowly</b>
	 * <ul>
	 * <li>node strength
	 * <li>standard deviation
	 * <li>price return
	 * <li>price mean
	 * </ul>
	 * of each index.
	 */
	private void printNodeAttributeCorr(File file) {
		setPrintWriter(file);
		// nodes and countries

		getPrintWriter().print("country,category,continent,");
		getPrintWriter().print("strength VS volatility,");
		getPrintWriter().print("strength VS log return,");
		getPrintWriter().print("strength VS closing value,");
		getPrintWriter().print("volatility VS log return,");
		getPrintWriter().print("volatility VS closing value,");
		getPrintWriter().print("log return VS closing value,");
		getPrintWriter().println();

		for (int i = 0; i < numberIndices; i++) {
			float[] indexClosePriceSub =
					ArrayUtils.subarray(indexClosePrices[i], 1, numberDays);
			Index index = indexList.get(i);
			getPrintWriter().print(
					index.getName() + "," + index.categories[index.getCategory()] + ","
							+ index.getRegion() + ",");
			getPrintWriter().print(
					MatrixUtils.xCorr(nodeStrengths[i], indexVolatility[i]) + ",");
			getPrintWriter().print(
					MatrixUtils.xCorr(nodeStrengths[i], indexLogReturns[i]) + ",");
			getPrintWriter().print(
					MatrixUtils.xCorr(nodeStrengths[i], indexClosePriceSub) + ",");
			getPrintWriter().print(
					MatrixUtils.xCorr(indexVolatility[i], indexLogReturns[i]) + ",");
			getPrintWriter().print(
					MatrixUtils.xCorr(indexVolatility[i], indexClosePriceSub) + ",");
			getPrintWriter().print(
					MatrixUtils.xCorr(indexLogReturns[i], indexClosePriceSub) + ",");
			getPrintWriter().println();
		}
		closePrintWriter();
	}

	/**
	 * 
	 */
	private void printWorldAttributeCorr(File file) {
		setPrintWriter(file);
		float[] worldClosePriceSub = ArrayUtils.subarray(worldClosePrices, 1, numberDays);
		// network and the world
		getPrintWriter().println(",synchronization,close price,log return,volatility");
		getPrintWriter().print("synchronization,1,");
		getPrintWriter().print(MatrixUtils.xCorr(networkSyn, worldClosePriceSub) + ",");
		getPrintWriter().print(MatrixUtils.xCorr(networkSyn, worldLogReturns) + ",");
		getPrintWriter().println(MatrixUtils.xCorr(networkSyn, worldVolatility) + "");
		getPrintWriter().print("close price,0,1,");
		getPrintWriter().print(MatrixUtils.xCorr(worldVolatility, worldClosePriceSub) + ",");
		getPrintWriter().println(MatrixUtils.xCorr(worldLogReturns, worldClosePriceSub) + "");
		getPrintWriter().print("log return,0,0,1,");
		getPrintWriter().println(MatrixUtils.xCorr(worldVolatility, worldLogReturns) + "");
		closePrintWriter();
	}

	/**
	 * get close prices from database convert close prices to log return
	 * 
	 * @throws IOException
	 */
	private void readData() throws IOException {
		// read indices from file.
		indexList = new ArrayList<Index>();
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
		// read close prices from file. line by date, column by indices

		initializeMatrices();
		indexClosePrices = new float[numberIndices][numberDays];
		worldClosePrices = new float[numberDays];
		in = new MaggieReader(new File("./dataset/allmarketsWorld.price"));
		int day = 0;
		try {
			while ((line = in.readLine()) != null) {
				sgmt = line.split("\t");
				for (int i = 0; i < numberIndices; i++) {
					indexClosePrices[i][day] = Float.parseFloat(sgmt[i]);
				}
				worldClosePrices[day] = Float.parseFloat(sgmt[numberIndices]);
				day++;
			}
		} catch (Exception e) {
			System.out.println(line);
		}
		in.close();
		// calculate log returns
		indexLogReturns = new float[numberIndices][numberWindows];
		for (int i = 0; i < numberIndices; i++) {
			indexLogReturns[i] = MatrixUtils.priceToLogReturn(indexClosePrices[i]);
		}
		worldLogReturns = MatrixUtils.priceToLogReturn(worldClosePrices);
		for (float r : worldLogReturns) {
			System.out.println(r);
		}
		// read correlation matrix
		adjMatrices = new float[numberWindows][numberIndices][numberIndices];
		in = new MaggieReader(new File("./dataset/DCC.corr"));
		int lineCount = 0;
		try {
			while ((line = in.readLine()) != null) {
				sgmt = line.split(",");
				for (int i = 0; i < sgmt.length; i++) {
					adjMatrices[i / numberIndices][lineCount][i % numberIndices] =
							Float.parseFloat(sgmt[i]);
				}
				lineCount++;
			}
		} catch (Exception e) {
			System.out.println(line);
		}
		in.close();
		// read all index volatility
		indexVolatility = new float[numberIndices][numberWindows];
		in = new MaggieReader(new File("./dataset/allmarkets.vol"));
		day = 0;
		try {
			while ((line = in.readLine()) != null) {
				sgmt = line.split(",");
				for (int i = 0; i < sgmt.length; i++) {
					indexVolatility[i][day] = Float.parseFloat(sgmt[i]);
				}
				day++;
			}
		} catch (Exception e) {
			System.out.println(line);
		}
		in.close();
		// read world index volatility
		worldVolatility = new float[numberWindows];
		in = new MaggieReader(new File("./dataset/world.vol"));
		day = 0;
		try {
			while ((line = in.readLine()) != null) {
				worldVolatility[day] = Float.parseFloat(line);
				day++;
			}
		} catch (Exception e) {
			System.out.println(line);
		}
		in.close();
	}

	/**
	 * gets data, initializes the arrays, does calculation for each window size,
	 * print results
	 * 
	 * @throws ParseException
	 * @throws IOException
	 * 
	 */
	public void run() throws ParseException, IOException {

		System.out.println("============ drawing network ===============");
		visualizeNetwork(600);

		// System.out.println("============ finding community structure ===============");
		// communityStructure();

		// System.out.println("============ calculating distributions ===============");
		// countDistribution();

		// System.out.println("============ printing customized result ===============");
		// customizedPrint();

		// System.out.println("======== distribution of edge weights ===========");
		// printMatrix(edgeWeightDistributions,
		// new File(MATLABFOLDER + "edge_weight.distribution"));

		// System.out.println("======== distribution of node strengths ===========");
		// printMatrix(nodeStrengthDistribution, new
		// File("node_strength.distribution"));

		// // correlations among 1. network synchronization, 2. price return,
		// // 3. price mmean, 4. standard deviation (volatility)
		// System.out.println("======= printing attribute correlations ==========");
		// printNodeAttributeCorr(new File("attribute_correlation_node.csv"));
		// printWorldAttributeCorr(new File("attribute_correlation_world.csv"));

		// // assortativity
		// System.out.println("============== assortativity ===================");
		// calcMarketAssort(new File(MATLABFOLDER + "region.assortativity"));
		// calcRegionAssort(new File(MATLABFOLDER + "region.assortativity"));
	}

	public void setDay(int day) {
		this.day = day;
		constructNetwork();
	}

	public void setThreshold(float threshold) {
		this.threshold = threshold;
		constructNetwork();
	}

	/**
	 * 
	 */
	public void trimNetwork() {
		WeakComponentClusterer tran = new WeakComponentClusterer();
		Set<Set> set = tran.transform(network);
		for (Set nodes : set) {
			if (nodes.size() < 5) {
				for (Object object : nodes) {
					network.removeVertex((Index) object);
				}
			}
		}
	}

	/**
	 * @param i
	 */
	private void visualizeNetwork(int day) {
		AbstractLayout layout = new FRLayout(network);
		VisualizationViewer gv = new GraphViewerSimple(layout);
		gv.setSize(600, 600);
		gv.revalidate();
		gv.setBackground(Color.WHITE);
		GuiUtils.drawComponentToFile(gv, new File("./test.png"), "png");
	}

}
