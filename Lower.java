package Sketchy;

/*
 * lower is a command class that models the action of lowering a shape one layer
 */
public class Lower implements Command {

	private Sketchy _sketchy;
	private Layer _layer;

	/*
	 * similar to raise, all I need to store a lower command is an instance of
	 * sketchy to call raise or lower and a layer
	 */
	public Lower(Sketchy sketchy, Layer layer) {
		_sketchy = sketchy;
		_layer = layer;
	}

	/*
	 * undoing a lower command means raising a shape, which adds an unwanted raise
	 * command to the undo stack which is then popped off
	 */
	public void undo() {
		_sketchy.raise(_layer.getShape());
		_sketchy.getUndoStack().pop();
	}

	/*
	 * redoing a lower command is just lowering a shape
	 */
	public void redo() {
		_sketchy.lower(_layer.getShape());
	}
}
