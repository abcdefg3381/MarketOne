package marketone.logic;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import marketone.db.entity.Index;
import marketone.db.entity.MarketNetwork;
import marketone.logic.algorithm.NetworkPreprocessor;
import marketone.logic.algorithm.NetworkStatistics;
import marketone.logic.util.NetworkExportor;
import marketone.visualization.DrawChart;

import org.apache.commons.lang.ArrayUtils;

/**
 * Main logic of client application.
 * 
 * @author LIU Xiaofan
 * 
 */
/**
 * @author LIU Xiaofan
 * 
 */
public class MarketOneLogicOld {

	private MarketNetwork myNetwork;
	private NetworkPreprocessor np = new NetworkPreprocessor();

	/**
	 * @param file
	 */
	public void exportNetwork() {
		NetworkExportor ne = new NetworkExportor(myNetwork);
		ne.export();
	}

	public void findMotifs(String args) {
		File statFile = new File("mfinder/motif.txt");
		NetworkStatistics stat = new NetworkStatistics(myNetwork, statFile);
		stat.getMotif(statFile, args);
	}

	/**
	 * @param indices
	 * @param date
	 * @param size
	 * @param financialDay
	 * 
	 */
	public void formNetwork(Object[] indices, Date date, String size, Object financialDay) {
		long tm = System.currentTimeMillis();
		myNetwork = new MarketNetwork();
		myNetwork.setEndDate(date);
		try {
			myNetwork.setWindowSize(Integer.parseInt(size));
		} catch (NumberFormatException e) {
			myNetwork.setWindowSize(20);
		}
		myNetwork.setStartingRegion(ArrayUtils.indexOf(Index.regions, financialDay));
		// myNetwork.setNodeList(getIndexList(indices, date,
		// myNetwork.getWindowSize(),
		// financialDay));
		np.setNetwork(myNetwork);
		// TODO add threshold
		np.formEdges(0);
		np.calcDegree();
		System.out.println("network formed in " + (System.currentTimeMillis() - tm) + " ms");
	}

	public MarketNetwork getNetwork() {
		return myNetwork;
	}

	public void getStatistics() {
		File statFile = new File("report/stat.txt");
		NetworkStatistics stat = new NetworkStatistics(myNetwork, statFile);
		stat.printReport();
		try {
			Desktop.getDesktop().open(statFile.getAbsoluteFile());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}
