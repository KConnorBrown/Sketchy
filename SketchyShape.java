package Sketchy;

import cs015.fnl.SketchySupport.FileIO;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

/*
 * Because we were advised to use a SketchyShape interface rather than an abstract class, to avoid complicated associations, 
 * my SketchyShape interface   has lots of methods that are implemented by both sketchy Ellipse and sketchy Rect and called on polymorphically 
 * in Sketchy.
 */

public interface SketchyShape extends Saveable {

	public Shape getSketchyShapeNode();

	public void setColor(Color color);

	public Color getColor();

	public void setLocation(Point2D point);

	public Point2D getCenterOfRotation();

	public void setRotate(double angle);

	public void translate(Point2D prev, Point2D curr);

	public void rotate(Point2D prev, Point2D curr);

	public void resize(Point2D prev, Point2D curr);

	public double getRotate();

	public Point2D getLocation();

	public void setSize(double width, double height);

	public Point2D getSize();

	public Point2D rotatePoint(Point2D pointToRotate, Point2D COR, double angle);

	public Color getShapesInitialColor();

	public Point2D getFinalLocation();

	public double getFinalRotation();

	public void save(FileIO io);

}
