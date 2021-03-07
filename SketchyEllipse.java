package Sketchy;

import cs015.fnl.SketchySupport.FileIO;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;

/*
 * the SketchyEllipse class implements both sketchy shape and saveable. It is a wrapper class for a java Ellipse. 
 * It is a java ellipse that knows how to resize, translate, rotate, save itself, switch colors, rotate a point contained by itself, among other small functionalities.
 */
public class SketchyEllipse implements SketchyShape, Saveable {

	private Ellipse _sketchyEllipse;
	private Color _initialColor;
	private Point2D _finalLocation;
	private double _finalRotation;
	private Point2D _location;
	private Point2D _size;
	private Color _currentColor;

	/*
	 * the constructor takes in a pane, and x and y location, and a color. it sets
	 * the size of the new ellipse initially to the default of zero and resizes from
	 * there in the sketchy class. a SketchyEllipse knows about its current color
	 * and initial color which makes undoing and redoing fill commands possible
	 */
	public SketchyEllipse(Pane pane, int x, int y, Color color) {
		_sketchyEllipse = new Ellipse(Constants.SHAPE_DEFAULT, Constants.SHAPE_DEFAULT);
		this.setLocation(new Point2D(x, y));
		_sketchyEllipse.setFill(color);
		pane.getChildren().add(_sketchyEllipse);
		_initialColor = color;
		_currentColor = color;
	}

	// returns location of ellipse
	public Point2D getLocation() {
		return _location;
	}

	// rotates a point according to the psuedocode provided
	public Point2D rotatePoint(Point2D pointToRotate, Point2D COR, double angle) {
		double sine = Math.sin(Math.toRadians(angle));
		double cosine = Math.cos(Math.toRadians(angle));
		Point2D point = new Point2D(pointToRotate.getX() - COR.getX(), pointToRotate.getY() - COR.getY());
		point = new Point2D(point.getX() * cosine + point.getY() * sine, -point.getX() * sine + point.getY() * cosine);
		point = new Point2D(point.getX() + COR.getX(), point.getY() + COR.getY());
		return point;
	}

	// returns the COR of an ellipse, its center
	public Point2D getCenterOfRotation() {
		return new Point2D(_sketchyEllipse.getCenterX(), _sketchyEllipse.getCenterY());
	}

	// rotates a sketchy ellipse by an angle passed in
	public void setRotate(double angle) {
		_sketchyEllipse.setRotate(angle);
	}

	// returns the rotate of an sketchy ellipse
	public double getRotate() {
		return _sketchyEllipse.getRotate();
	}

	// sets the location of a sketchy ellipse by the center of the ellipse and
	// updates location
	public void setLocation(Point2D point) {
		_sketchyEllipse.setCenterX(point.getX());
		_sketchyEllipse.setCenterY(point.getY());
		_location = new Point2D(point.getX(), point.getY());
	}

	// sets the size of the ellipse by a passed in width and height
	public void setSize(double width, double height) {
		_sketchyEllipse.setRadiusX(width);
		_sketchyEllipse.setRadiusY(height);
	}

	/*
	 * the translate method takes in a prev pt and curr pt and finds the dx and dy
	 * between them in order to get a future point which the sketchy ellipse sets
	 * its location to. This future pt updates the variable final location everytime
	 * translate is called.
	 */
	public void translate(Point2D prev, Point2D curr) {
		double dx = (curr.getX() - prev.getX()); // future x minus previous x
		double dy = (curr.getY() - prev.getY()); // future y minus previous y
		Point2D futurePt = prev.add(dx, dy);
		this.setLocation(futurePt);
		_finalLocation = futurePt;
	}

	// returns final location for creating translate command when mouse is released
	public Point2D getFinalLocation() {
		return _finalLocation;
	}

	/*
	 * rotate takes in a prev and curr pt and rotate the whole shape by the rotation
	 * found between the two points. The ellipses center is the center of rotation.
	 * the final rotation is updated to be used in a rotate command if mouse is
	 * released
	 */
	public void rotate(Point2D prev, Point2D curr) {
		Point2D COR = this.getCenterOfRotation();
		double dϴ = Math.atan2((prev.getY() - COR.getY()), (prev.getX() - COR.getX()))
				- Math.atan2((curr.getY() - COR.getY()), (curr.getX() - COR.getX()));
		this.setRotate(this.getRotate() - (180 / Math.PI) * dϴ);
		_finalRotation = this.getRotate() - (180 / Math.PI) * dϴ;
	}

	// returns final rotation
	public double getFinalRotation() {
		return _finalRotation;
	}

	/*
	 * resize takes in prev and curr pt. it accounts for the shape if it is rotated.
	 * The ellipse changes in radius x and y by two times dy or dx plus the current
	 * radius. dx and dy are positive or negative depending on the conditions
	 * elaborated in the psuedocode. The resize method also continues to update the
	 * center of the ellipse as the shape grows or shrinks.
	 */
	public void resize(Point2D prev, Point2D curr) {
		double rotation = this.getRotate();
		Point2D oldCenter = this.getCenterOfRotation();
		Point2D rotatedPrev = this.rotatePoint(prev, oldCenter, rotation);
		Point2D rotatedCurr = this.rotatePoint(curr, oldCenter, rotation);

		double dx = Math.abs(rotatedPrev.getX() - rotatedCurr.getX());
		double dy = Math.abs(rotatedPrev.getY() - rotatedCurr.getY());

		double corX = oldCenter.getX();
		double rotatedPrevX = prev.getX();
		double rotatedCurrX = curr.getX();
		if (Math.abs(corX - rotatedPrevX) > Math.abs(corX - rotatedCurrX)) {
			dx = dx * -1;
		}

		double corY = oldCenter.getY();
		double rotatedPrevY = prev.getY();
		double rotatedCurrY = curr.getY();
		if (Math.abs(corY - rotatedPrevY) > Math.abs(corY - rotatedCurrY)) {
			dy = dy * -1;
		}

		this.setSize(_sketchyEllipse.getRadiusX() + (2 * dx), (_sketchyEllipse.getRadiusY() + (2 * dy)));
		_size = new Point2D(_sketchyEllipse.getRadiusX() + (2 * dx), (_sketchyEllipse.getRadiusY() + (2 * dy)));

		Point2D newCenter = this.getCenterOfRotation();
		if (oldCenter != newCenter) {
			dx = (oldCenter.getX() - newCenter.getX());
			dy = (oldCenter.getY() - newCenter.getY());
			_sketchyEllipse.setCenterX(this.getCenterOfRotation().getX() + dx);
			_sketchyEllipse.setCenterY(this.getCenterOfRotation().getY() + dy);
		}
	}

	// returns the size of the shape which is a Poin2D with x representing the x
	// radius and y representing the y radius
	public Point2D getSize() {
		return _size;
	}

	// sets the color of the ellipse to chosen color and updates current color of
	// the ellipse
	public void setColor(Color color) {
		_sketchyEllipse.setFill(color);
		_currentColor = color;
	}

	// returns current color of the ellipse
	public Color getColor() {
		return _currentColor;
	}

	// gets the node of an ellipse which is a sketchyEllipse
	public Shape getSketchyShapeNode() {
		return this.getNode();
	}

	// gets the ellipse of a sketchy ellipse
	private Ellipse getNode() {
		return _sketchyEllipse;
	}

	// gets the shapes initial color
	public Color getShapesInitialColor() {
		return _initialColor;
	}

	/*
	 * the save method takes in a fileIO. The ellipse knows how to save itself. It
	 * first writes the string "ellipse", then its location, then its size, then its
	 * rotation and so on
	 */
	public void save(FileIO io) {
		io.writeString("ellipse");
		io.writeDouble(this.getLocation().getX());
		io.writeDouble(this.getLocation().getY());
		io.writeDouble(_sketchyEllipse.getRadiusX());
		io.writeDouble(_sketchyEllipse.getRadiusY());
		io.writeDouble(this.getRotate());
		io.writeInt((int) (_currentColor.getRed() * 255));
		io.writeInt((int) (_currentColor.getGreen() * 255));
		io.writeInt((int) (_currentColor.getBlue() * 255));
	}

}
