package marketone.commodity.visulization;

import java.awt.Color;

import marketone.commodity.entity.Country;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;

@SuppressWarnings("rawtypes")
public class GraphViewer extends VisualizationViewer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9111226419973329984L;

	/**
	 * @param layout
	 * @param network
	 */
	@SuppressWarnings("unchecked")
	public GraphViewer(Graph g) {
		super(new KKLayout<Country, Flow>(g));
		initialize();
	}

	@SuppressWarnings("unchecked")
	private void initialize() {
		// background color
		setBackground(Color.WHITE);

		// node color
		getRenderContext().setVertexFillPaintTransformer(new Transformer<Country, Color>() {
			@Override
			public Color transform(Country s) {
				return Color.red;
			}
		});

		// node label
		getRenderContext().setVertexLabelTransformer(new Transformer<Country, String>() {
			@Override
			public String transform(Country c) {
				return c.getId();
			}
		});

		// edge shape
		getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Integer, Number>());

		// mouse control
		setGraphMouse(new DefaultModalGraphMouse<Integer, Number>());
	}
}