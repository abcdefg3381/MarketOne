/**
 * 
 */
package marketone.visualization;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

import marketone.db.entity.Index;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

/**
 * @author LIU Xiaofan
 * 
 */
@SuppressWarnings("unchecked")
public class TreeViewer extends VisualizationViewer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8013482471604379161L;

	/**
	 * @param model
	 */
	public TreeViewer(VisualizationModel model) {
		super(model);
		initialize();
	}

	private void initialize() {
		// background color
		setBackground(Color.WHITE);
		// index size
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
		getRenderContext().setVertexFillPaintTransformer(new Transformer<Index, Color>() {
			@Override
			public Color transform(Index s) {
				if (s.getRegion().equals(Index.regions[0]))
					return Color.yellow;
				else if (s.getRegion().equals(Index.regions[1]))
					return Color.black;
				else if (s.getRegion().equals(Index.regions[2]))
					return Color.blue;
				else if (s.getRegion().equals(Index.regions[3]))
					return Color.pink;
				return Color.white;
			}
		});
		getRenderContext().setVertexDrawPaintTransformer(new Transformer<Index, Paint>() {
			public Paint transform(Index v) {
				if (getPickedVertexState().isPicked(v))
					return Color.cyan;
				else
					return Color.BLACK;
			}
		});
		// index label
		getRenderContext().setVertexLabelTransformer(new Transformer<Index, String>() {
			@Override
			public String transform(Index index) {
				return index.getCountry();
			}
		});
		// edge shape
		// vv.getRenderContext().setEdgeShapeTransformer(
		// new EdgeShape.Line<Integer, Number>());
		// edge color
		// vv.getRenderContext().setEdgeDrawPaintTransformer(
		// new Transformer<MyEdge, Color>() {
		// @Override
		// public Color transform(MyEdge edge) {
		// if (edge.getWeight() < year) {
		// return new Color(1, 1, 1, 1);
		// }
		// int color = 255 - (edge.getWeight() + 1) * 50;
		// if (color < 0) {
		// color = 0;
		// }
		// return new Color(color, color, color);
		//
		// }
		// });

		// edge color
		getRenderContext().setEdgeDrawPaintTransformer(new Transformer<Integer, Color>() {
			final float basic = 2.0f;

			@Override
			public Color transform(Integer e) {
				try {
					return Color.black;
				} catch (Exception ex) {
					return Color.white;
				}
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
		// arrow color
		// vv.getRenderContext().setArrowFillPaintTransformer(
		// new Transformer<MyEdge, Color>() {
		// @Override
		// public Color transform(MyEdge edge) {
		// if (edge.getWeight() < year) {
		// return new Color(1, 1, 1, 1);
		// } else {
		// return Color.GRAY;
		// }
		//
		// }
		// });
		// vv.getRenderContext().setArrowDrawPaintTransformer(
		// new Transformer<MyEdge, Color>() {
		// @Override
		// public Color transform(MyEdge edge) {
		// if (edge.getWeight() < year) {
		// return new Color(1, 1, 1, 1);
		// } else {
		// return Color.GRAY;
		// }
		// }
		// });
		// mouse control
		setGraphMouse(new DefaultModalGraphMouse<Integer, Number>());
	}

}
