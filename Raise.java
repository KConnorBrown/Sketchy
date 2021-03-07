package Sketchy;

/*
 * raise is a command class that models the action of raising a shape one layer
 */
public class Raise implements Command {

	private Sketchy _sketchy;
	private Layer _layer;

	/*
	 * to store the necessary information when raising a shape I just need a sketchy
	 * instance and a layer. This is because each time I create a layer it is
	 * associated with a shape and can return that shape
	 */
	public Raise(Sketchy sketchy, Layer layer) {
		_sketchy = sketchy;
		_layer = layer;
	}

	/*
	 * undoing a raise command means lowering the shape at that layer. Because a new
	 * lower command is added to the undo stack extraneously I immediately pop the
	 * undo stack to get rid of that unwanted lower command. 
	 */
	public void undo() {
		_sketchy.lower(_layer.getShape());
		_sketchy.getUndoStack().pop();
	}

	/*
	 * the redo of a raise command is just raising a shape again
	 */
	public void redo() {
		_sketchy.raise(_layer.getShape());
	}

}
