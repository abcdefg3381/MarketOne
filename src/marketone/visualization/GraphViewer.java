/**
 * 
 */
package marketone.visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import maggie.network.entity.Node;
import marketone.db.entity.Index;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

/**
 * @author LIU Xiaofan
 * 
 */
@SuppressWarnings("unchecked")
public class GraphViewer extends VisualizationViewer {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9152820148561556003L;
	private boolean byRegion;

	/**
	 * @param layout
	 * @param byRegion
	 */
	public GraphViewer(AbstractLayout<Index, Integer> layout, boolean byRegion) {
		super(layout);
		initialize();
		this.byRegion = byRegion;
	}

	private void initialize() {
		// background color
		setBackground(Color.WHITE);

		// // node size
		// getRenderContext().setVertexShapeTransformer(
		// new ConstantTransformer(new Ellipse2D.Float(-6, -6, 12, 12)));

		// node shape
		getRenderContext().setVertexShapeTransformer(new Transformer<Index, Object>() {

			@Override
			public Object transform(Index s) {
				if (byRegion) {
					if (s.getRegionIndex() == 0)
						return new Ellipse2D.Float(-6, -6, 12, 12);
					else if (s.getRegionIndex() == 1)
						return new Ellipse2D.Float(-6, -6, 12, 12);
					else if (s.getRegionIndex() == 2)
						return new Rectangle2D.Float(-6, -6, 12, 12);
					else if (s.getRegionIndex() == 3)
						return new Rectangle2D.Float(-6, -6, 12, 12);
					else
						return new Rectangle2D.Float(-6, -6, 12, 12);
				} else {
					if (s.getCategory() == 0)
						return new Ellipse2D.Float(-6, -6, 12, 12);
					else if (s.getCategory() == 1)
						return new Ellipse2D.Float(-6, -6, 12, 12);
					else if (s.getCategory() == 2)
						return new Ellipse2D.Float(-6, -6, 12, 12);
					else
						return new Rectangle2D.Float(-6, -6, 12, 12);
				}
			}

		});

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
				if (byRegion) {
					if (s.getRegionIndex() == 0)
						return Color.white;
					else if (s.getRegionIndex() == 1)
						return Color.black;
					else if (s.getRegionIndex() == 2)
						return Color.white;
					else if (s.getRegionIndex() == 3)
						return Color.black;
					else
						return Color.white;
				} else {
					if (s.getCategory() == 0)
						return Color.black;
					else if (s.getCategory() == 1)
						return Color.gray;
					else if (s.getCategory() == 2)
						return Color.white;
					else
						return Color.white;
				}
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
				return Color.gray;
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

		// mouse control
		setGraphMouse(new DefaultModalGraphMouse<Integer, Number>());
	}
}
