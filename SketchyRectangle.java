package Sketchy;

import cs015.fnl.SketchySupport.FileIO;
import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

/*
 * My sketchyRectangle class is almost identical to sketchyEllipse. It too is a wrapper class around a java shape that creates a self aware,
 * self transforming shape ie a sketchy shape. I've noted this difference in method implementations between sketchy ellipse and sketchy rect
 *  without repeating all my comments from sketchy ellipse
 */
public class SketchyRectangle implements SketchyShape, Saveable {

	private Rectangle _sketchyRect;
	private double _rectWidth;
	private double _rectHeight;
	private Color _initialColor;
	private Point2D _location;
	private Point2D _finalLocation;
	private double _finalRotation;
	private Point2D _size;
	private Color _currentColor;

	public SketchyRectangle(Pane pane, int x, int y, Color color) {
		_rectWidth = Constants.SHAPE_DEFAULT;
		_rectHeight = Constants.SHAPE_DEFAULT;
		_sketchyRect = new Rectangle(_rectWidth, _rectHeight);
		this.setLocation(new Point2D(x, y));
		_sketchyRect.setFill(color);
		_initialColor = color;
		_currentColor = color;
		pane.getChildren().add(_sketchyRect);
	}

	/*
	 * the set location method for sketchy rect sets the location of the rect
	 * relative to its center. All shape manipulations performed on sketchy rect are
	 * done relative to the cener of the rectangle which makes for a convenient
	 * center of rotation
	 */
	public void setLocation(Point2D point) {
		_sketchyRect.setX(point.getX() - _rectWidth / 2);
		_sketchyRect.setY(point.getY() - _rectHeight / 2);
		_location = new Point2D(point.getX() - _rectWidth / 2, point.getY() - _rectHeight);
	}

	public Point2D getLocation() {
		return _location;
	}

	public Shape getSketchyShapeNode() {
		return this.getNode();
	}

	public Point2D getCenterOfRotation() {
		return new Point2D(_sketchyRect.getX() + (_rectWidth / 2), _sketchyRect.getY() + (_rectHeight / 2));
	}

	private Rectangle getNode() {
		return _sketchyRect;
	}

	public void setColor(Color color) {
		_sketchyRect.setFill(color);
		_currentColor = color;
	}

	public Color getColor() {
		return _currentColor;
	}

	public void translate(Point2D prev, Point2D curr) {

		double dx = (curr.getX() - prev.getX()); // future x minus previous x
		double dy = (curr.getY() - prev.getY()); // future y minus previous y

		Point2D futurePt = prev.add(dx, dy);
		this.setLocation(futurePt);
		_finalLocation = futurePt;
	}

	public Point2D getFinalLocation() {
		return _finalLocation;
	}

	public void rotate(Point2D prev, Point2D curr) {
		double previousY = prev.getY();
		double previousX = prev.getX();
		double futureY = curr.getY();
		double futureX = curr.getX();
		Point2D COR = this.getCenterOfRotation();
		double dϴ = Math.atan2((previousY - COR.getY()), (previousX - COR.getX()))
				- Math.atan2((futureY - COR.getY()), (futureX - COR.getX()));
		this.setRotate(this.getRotate() - (180 / Math.PI) * dϴ);
		_finalRotation = this.getRotate() - (180 / Math.PI) * dϴ;
	}

	public double getFinalRotation() {
		return _finalRotation;
	}

	public Point2D rotatePoint(Point2D pointToRotate, Point2D COR, double angle) {
		double sine = Math.sin(Math.toRadians(angle));
		double cosine = Math.cos(Math.toRadians(angle));
		Point2D point = new Point2D(pointToRotate.getX() - COR.getX(), pointToRotate.getY() - COR.getY());
		point = new Point2D(point.getX() * cosine + point.getY() * sine, -point.getX() * sine + point.getY() * cosine);
		point = new Point2D(point.getX() + COR.getX(), point.getY() + COR.getY());
		return point;
	}

	public void resize(Point2D prev, Point2D curr) {
		double rotation = this.getRotate();
		Point2D oldCenter = this.getCenterOfRotation();
		Point2D rotatedPrev = this.rotatePoint(prev, oldCenter, rotation);
		Point2D rotatedCurr = this.rotatePoint(curr, oldCenter, rotation);

		double dx = Math.abs(rotatedCurr.getX() - rotatedPrev.getX());
		double dy = Math.abs(rotatedCurr.getY() - rotatedPrev.getY());

		double shapeCenterX = oldCenter.getX();
		double rotatedPrevX = prev.getX();
		double rotatedCurrX = curr.getX();

		if (Math.abs(shapeCenterX - rotatedPrevX) > Math.abs(shapeCenterX - rotatedCurrX)) {
			dx = dx * -1;
		}

		double corY = oldCenter.getY();
		double rotatedPrevY = prev.getY();
		double rotatedCurrY = curr.getY();

		if (Math.abs(corY - rotatedPrevY) > Math.abs(corY - rotatedCurrY)) {
			dy = dy * -1;
		}

		_rectWidth = _sketchyRect.getWidth();
		_rectHeight = _sketchyRect.getHeight();

		this.setSize(_rectWidth + 2 * dx, _rectHeight + 2 * dy);

		_size = new Point2D(_rectWidth + 2 * dx, _rectHeight + 2 * dy);

		Point2D newCenter = this.getCenterOfRotation();
		if (oldCenter != newCenter) {
			dx = (oldCenter.getX() - newCenter.getX());
			dy = (oldCenter.getY() - newCenter.getY());
			_sketchyRect.setX((this.getCenterOfRotation().getX() + dx - _rectWidth / 2));
			_sketchyRect.setY((this.getCenterOfRotation().getY() + dy - _rectHeight / 2));
		}
	}

	public Point2D getSize() {
		return _size;
	}

	public void setSize(double width, double height) {
		_sketchyRect.setWidth(width);
		_sketchyRect.setHeight(height);
	}

	public Color getShapesInitialColor() {
		return _initialColor;
	}

	/*
	 * set center x and set center y set the location of a rectangle relative to its
	 * center point
	 */
	public void setCenterX(double x) {
		_sketchyRect.setX(_rectWidth / 2 + x);
	}

	public void setCenterY(double y) {
		_sketchyRect.setY(_rectHeight / 2 + y);
	}

	public void setRotate(double angle) {
		_sketchyRect.setRotate(angle);
	}

	public double getRotate() {
		return _sketchyRect.getRotate();
	}

	/*
	 * the rectangle saves itself by writing a "rect" string first which would alert
	 * the file io that it's reading a rect and load it as such.
	 */
	public void save(FileIO io) {
		io.writeString("rect");
		io.writeDouble(this.getLocation().getX());
		io.writeDouble(this.getLocation().getY());
		io.writeDouble(_rectWidth);
		io.writeDouble(_rectHeight);
		io.writeDouble(this.getRotate());
		io.writeInt((int) (_currentColor.getRed() * 255));
		io.writeInt((int) (_currentColor.getGreen() * 255));
		io.writeInt((int) (_currentColor.getBlue() * 255));
	}

}
