package Sketchy;

/*
 * rotate is a command class that models the action of rotating a shape some amount
 */
public class Rotate implements Command {

	private SketchyShape _shape;
	private double _angleBefore;
	private double _angleAfter;

	/*
	 * All I need to be able to store a rotate command is a shape and its before and after angle of rotation
	 */
	public Rotate(SketchyShape shape, double before, double after) {
		_shape = shape;
		_angleBefore = before;
		_angleAfter = after;
	}

	/*
	 * undoing a rotate command sets the shapes rotation to before
	 */
	public void undo() {
		_shape.setRotate(_angleBefore);
	}

	/*
	 * redoing a rotate command sets the shapes rotation to after
	 */
	public void redo() {
		_shape.setRotate(_angleAfter);
	}

}
