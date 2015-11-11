/**
 * 
 */
package marketone.visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import maggie.network.entity.Node;
import marketone.db.entity.Index;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * @author LIU Xiaofan
 * 
 */
@SuppressWarnings("unchecked")
public class GraphViewerSimple extends VisualizationViewer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9152820148561556003L;

	/**
	 * @param layout
	 */
	public GraphViewerSimple(AbstractLayout<Integer, Integer> layout) {
		super(layout);
		initialize();
	}

	private void initialize() {
		// background color
		setBackground(Color.WHITE);

		// node size
		// vv.getRenderContext().setVertexShapeTransformer(
		// new ConstantTransformer(new Ellipse2D.Float(-6, -6, 12, 12)));
		// index color
		// vv.getRenderContext().setVertexFillPaintTransformer(
		// new Transformer<Note, Paint>() {
		// @Override
		// public Paint transform(Note note) {
		// int degree = g.degree(note);
		// if (degree < 30) {
		// degree *= 8;
		// }
		// if (degree > 255) {
		// degree = 255;
		// }
		// return new Color(255, 255 - degree, 255 - degree);
		// }
		// });
		// vv.getRenderContext().setVertexFillPaintTransformer(
		// MapTransformer.<Index, Paint> getInstance(vertexPaints));

		// node color
		getRenderContext().setVertexFillPaintTransformer(new Transformer<Index, Color>() {
			@Override
			public Color transform(Index s) {
				if (s.getRegionIndex() == 0)
					return Color.blue;
				else if (s.getRegionIndex() == 1)
					return Color.pink;
				else if (s.getRegionIndex() == 2)
					return Color.yellow;
				else if (s.getRegionIndex() == 3)
					return Color.black;
				else
					return Color.white;
				// if (s.getCategory() == 0)
				// return Color.red;
				// else if (s.getCategory() == 1)
				// return Color.green;
				// else if (s.getCategory() == 2)
				// return Color.blue;
				// else
				// return Color.white;
			}
		});
		getRenderContext().setVertexDrawPaintTransformer(new Transformer<Node, Paint>() {
			public Paint transform(Node v) {
				if (getPickedVertexState().isPicked(v))
					return Color.cyan;
				else
					return Color.BLACK;
			}
		});

		// // node label
		// getRenderContext().setVertexLabelTransformer(new Transformer<Index,
		// String>() {
		// @Override
		// public String transform(Index i) {
		// return i.getCountry();
		// }
		// });

		// edge color
		getRenderContext().setEdgeDrawPaintTransformer(new Transformer<Integer, Color>() {
			final float basic = 2.0f;

			@Override
			public Color transform(Integer e) {
				// try {
				// return new Color(basic * (1 - e.getWeight()), basic * (1 -
				// e.getWeight()),
				// basic * (1 - e.getWeight()));
				// } catch (Exception ex) {
				// return Color.white;
				// }
				return Color.black;
			}
		});

		// MapTransformer.<MyEdge, Paint> getInstance(edgePaints));
		getRenderContext().setEdgeStrokeTransformer(new Transformer<Integer, Stroke>() {
			// protected final Stroke THICK = new BasicStroke(2);
			protected final Stroke THIN = new BasicStroke(1);

			public Stroke transform(Integer e) {
				return THIN;
			}
		});
	}
}
