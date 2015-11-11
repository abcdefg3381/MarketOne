package marketone.commodity.visulization;

import java.io.File;
import java.util.List;

import javax.swing.JFrame;

import maggie.network.gui.GuiUtils;
import marketone.commodity.algorithms.CountryLoader;
import marketone.commodity.algorithms.MatrixLoader;
import marketone.commodity.entity.Country;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;

public class MatrixToGraph {
	public static void main(String[] args) {
		MatrixToGraph mtg = new MatrixToGraph();
//		 mtg.outputExportGraph();
		mtg.outputPatternGraph();
		mtg.finish();
	}

	private void outputPatternGraph() {
		double[][][] matrices = MatrixLoader.loadPatternMatrix();
		List<Country> reporters = CountryLoader.loadReporters();
		for (int y = 0; y < matrices.length; y++) {
			// make graph
			UndirectedSparseMultigraph<Country, Flow> graph = new UndirectedSparseMultigraph<Country, Flow>();
			for (Country country : reporters) {
				graph.addVertex(country);
			}
			for (int i = 0; i < matrices[y].length; i++) {
				for (int j = i+1; j < matrices[y][i].length; j++) {
					// threshold
					if (matrices[y][i][j] <= 0.5)
						continue;
					graph.addEdge(new Flow(matrices[y][i][j]), reporters.get(i), reporters.get(j));
				}
			}
			// output graph
			GraphViewer gv = new GraphViewer(graph);
			gv.setSize(600, 600);
			File f = new File("./graph/" + (y + 2005) + ".bmp");
			GuiUtils.drawComponentToFile(gv, f, "bmp");
			JFrame frame = new JFrame();
			frame.add(gv);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			break;
		}
	}

	private void finish() {
		System.out.println("finish");
	}

	private void outputExportGraph() {
		double[][][] matrices = MatrixLoader.loadExportMatrix();
		List<Country> reporters = CountryLoader.loadReporters();
		for (int y = 0; y < matrices.length; y++) {
			// make graph
			DirectedSparseMultigraph<Country, Flow> graph = new DirectedSparseMultigraph<Country, Flow>();
			for (Country country : reporters) {
				graph.addVertex(country);
			}
			for (int i = 0; i < matrices[y].length; i++) {
				for (int j = 0; j < matrices[y][i].length; j++) {
					if (i == j || matrices[y][i][j] == 0)
						continue;
					// threshold
					if (matrices[y][i][j] <= 5000)
						continue;
					graph.addEdge(new Flow(matrices[y][i][j]), reporters.get(i), reporters.get(j));
				}
			}
			// output graph
			GraphViewer gv = new GraphViewer(graph);
			gv.setSize(600, 600);
			File f = new File("./graph/" + (y + 2005) + ".bmp");
			GuiUtils.drawComponentToFile(gv, f, "bmp");
			JFrame frame = new JFrame();
			frame.add(gv);
			frame.pack();
			frame.setVisible(true);
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			break;
		}

	}

}
