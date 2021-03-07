package Sketchy;

/*
 * delete is a command class that models the action of deleting a shape
 */
public class Delete implements Command {

	private SketchyShape _shape;
	private Sketchy _sketchy;
	private int _paneIndex;
	private int _shapesIndex;
	private int _saveablesIndex;

	/*
	 * a delete command just needs a shape to delete and an instance of sketchy, for
	 * convenience the shapes indices are stored as well
	 */
	public Delete(SketchyShape shape, Sketchy sketchy) {
		_shape = shape;
		_sketchy = sketchy;
		_paneIndex = sketchy.getSketchPane().getChildren().indexOf(shape.getSketchyShapeNode());
		_shapesIndex = sketchy.getShapesArrayList().indexOf(shape);
		_saveablesIndex = sketchy.getSaveables().indexOf(shape);
	}

	/*
	 * undoing a delete command means adding the deleted shape at the indices it was at before it was deleted
	 */
	public void undo() {
		_sketchy.getSketchPane().getChildren().add(_paneIndex, _shape.getSketchyShapeNode());
		_sketchy.getSaveables().add(_saveablesIndex, _shape);
		_sketchy.getShapesArrayList().add(_shapesIndex, _shape);
	}

	/*
	 * redoing a delete command means removing the shape graphically and logically from Sketchy
	 */
	public void redo() {
		_sketchy.getSketchPane().getChildren().remove(_shape.getSketchyShapeNode());
		_sketchy.getSaveables().remove(_shape);
		_sketchy.getShapesArrayList().remove(_shape);
	}
}
