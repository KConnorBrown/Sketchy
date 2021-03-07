package Sketchy;

/*
 * createShape is a command class that models the action of creating a shape.
 */
public class CreateShape implements Command {

	private Sketchy _sketchy;
	private SketchyShape _shape;
	private int _shapesIndex;
	private int _saveablesIndex;
	private int _paneIndex;

	/*
	 * the createShape command just needs a shape and a sketchy instance to store
	 * the necessary info to undo and redo creating that shape. the indices of the
	 * shape are stored for conveniece. Because each shape is self aware of its own
	 * dimensions when it is initially added to sketchy all I have to do is keep
	 * track of the shape and its indices.
	 */
	public CreateShape(SketchyShape shape, Sketchy sketchy) {
		_sketchy = sketchy;
		_shape = shape;
		_shapesIndex = sketchy.getShapesArrayList().indexOf(shape);
		_saveablesIndex = sketchy.getSaveables().indexOf(shape);
		_paneIndex = sketchy.getSketchPane().getChildren().indexOf(shape.getSketchyShapeNode());
	}

	/*
	 * undo means removing a shape logically and graphically from sketchy
	 */
	public void undo() {
		_sketchy.getSketchPane().getChildren().remove(_shape.getSketchyShapeNode());
		_sketchy.getSaveables().remove(_shape);
		_sketchy.getShapesArrayList().remove(_shape);
	}

	/*
	 * redoing a create shape means adding the shape at the indices it was at before
	 * its creation was undid
	 */
	public void redo() {
		_sketchy.getSketchPane().getChildren().add(_paneIndex, _shape.getSketchyShapeNode());
		_sketchy.getSaveables().add(_saveablesIndex, _shape);
		_sketchy.getShapesArrayList().add(_shapesIndex, _shape);
	}

}
