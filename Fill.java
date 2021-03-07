package Sketchy;

import javafx.scene.paint.Color;

/*
 * fill is a command class that models the action of filling a shape some color
 */
public class Fill implements Command {

	private Color _before;
	private Color _after;
	private SketchyShape _shape;

	/*
	 * a fill command just needs a shape and the color that shape was before and
	 * after being filled in sketchy
	 */
	public Fill(SketchyShape shape, Color before, Color after) {
		_before = before;
		_after = after;
		_shape = shape;
	}

	/*
	 * undoing a fill means setting the shape's color to before
	 */
	public void undo() {
		_shape.setColor(_before);
	}

	/*
	 * redoing a fill means setting the shape's color to after
	 */
	public void redo() {
		_shape.setColor(_after);
	}

}
