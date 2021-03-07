package Sketchy;

import javafx.geometry.Point2D;

/*
 * resize is a command class that models the action of resizing a shape some amount
 */
public class Resize implements Command {

	private SketchyShape _shape;
	private double _beforeW;
	private double _afterW;
	private double _beforeH;
	private double _afterH;

	/*
	 * All I need to undo or redo a resize command is the start and end size of a
	 * shape that's been resized. the sizes are modelled by a Point2D with x
	 * correspondig to width and y to height
	 */
	public Resize(SketchyShape shape, Point2D startSize, Point2D finishSize) {
		_shape = shape;
		_beforeW = startSize.getX();
		_beforeH = startSize.getY();
		_afterW = finishSize.getX();
		_afterH = finishSize.getY();
	}

	/*
	 * undoing means setting the shape to its before dimensions. (I would have
	 * defined set size as taking in a point but each sketchy shape sets their size
	 * differently)
	 */
	public void undo() {
		_shape.setSize(_beforeW, _beforeH);
	}

	/*
	 * redoing means setting the shape to its after dimensions.
	 */
	public void redo() {
		_shape.setSize(_afterW, _afterH);
	}

}
