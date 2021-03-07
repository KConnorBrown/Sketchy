package Sketchy;

/*
 * draw is a command class that models the action of drawing a curved line
 */
public class Draw implements Command {

	private Sketchy _sketchy;
	private CurvedLine _line;
	private int _saveablesIndex;
	private int _paneIndex;

	/*
	 * all I need to undo or redo a draw command is an instance of Sketchy and a
	 * curved line instance. Sketchy gets the indices of the curved line.
	 */
	public Draw(Sketchy sketchy, CurvedLine line) {
		_sketchy = sketchy;
		_line = line;
		_saveablesIndex = sketchy.getSaveables().indexOf(line);
		_paneIndex = sketchy.getSketchPane().getChildren().indexOf(line.getPolyline());
	}

	/*
	 * undoing a drawn line means removing the line logically and graphically from sketchy
	 */
	public void undo() {
		_sketchy.getSketchPane().getChildren().remove(_line.getPolyline());
		_sketchy.getSaveables().remove(_line);
	}

	/*
	 * redoing a draw line means adding the curved line graphically and logically at the appropriate indices
	 */
	public void redo() {
		_sketchy.getSketchPane().getChildren().add(_paneIndex, _line.getPolyline());
		_sketchy.getSaveables().add(_saveablesIndex, _line);
	}

}
