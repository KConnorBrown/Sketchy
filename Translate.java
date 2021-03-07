package Sketchy;

import javafx.geometry.Point2D;

/*
 * translate is a command class that models the action of translating a shape some distance
 */
public class Translate implements Command {

	private Point2D _before;
	private Point2D _after;
	private SketchyShape _shape;

	/*
	 * All I need to store for a translate command in order to be able to undo it or
	 * redo it is the shape that was translated and its before and after points
	 */
	public Translate(SketchyShape shape, Point2D before, Point2D after) {
		_before = before;
		_after = after;
		_shape = shape;
	}

	/*
	 * undoing a translate method means setting the shape to the before location
	 */
	public void undo() {
		_shape.setLocation(_before);
	}

	/*
	 * redoing a translate method means setting the shape to the after location
	 */
	public void redo() {
		_shape.setLocation(_after);
	}

}
