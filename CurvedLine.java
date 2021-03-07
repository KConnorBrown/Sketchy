package Sketchy;

import cs015.fnl.SketchySupport.FileIO;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;

/*
 * the curved line class is a wrapper class around a polyline. It is a self saving polyline that can be created in a color of sketchy/control's choosing
 */
public class CurvedLine implements Saveable {

	private Polyline _polyline;
	private Color _color;

	/*
	 * the curved line constructor takes in a pane to add the polyline to, a color
	 * to set the stroke of the polyline to, and a series of x's and y's to add to
	 * the polyline
	 */
	public CurvedLine(Pane pane, double x, double y, Color color) {
		_polyline = new Polyline();
		_polyline.setStroke(color);
		_polyline.getPoints().addAll(x, y);
		pane.getChildren().add(_polyline);
		_color = color;
	}

	// method that allows a curved line to add points directly
	public void addPoints(double x, double y) {
		_polyline.getPoints().addAll(x, y);
	}

	// returns the polyline of a curved line
	public Polyline getPolyline() {
		return _polyline;
	}

	/*
	 * A curved line saves itself by first writing the string "line" and then saving
	 * its color components and then the number of points in a polyline. Then for
	 * the length of the polyline the io loops through and saves the points
	 */
	public void save(FileIO io) {
		io.writeString("line");
		io.writeInt((int) (_color.getRed() * 255));
		io.writeInt((int) (_color.getGreen() * 255));
		io.writeInt((int) (_color.getBlue() * 255));
		io.writeInt(_polyline.getPoints().size());
		for (int i = 0; i < _polyline.getPoints().size(); i++) {
			io.writeDouble(_polyline.getPoints().get(i));
		}
	}

}
