package Sketchy;

/*
 * My layer class keeps track of the indices of the shape's node in the
 * Pane's list of children (Observable Children) and the index of the shape in
 * the other data structures (shapes and saveables array lists). It has getters and setters for each index of a shape.
 */

public class Layer {

	private int _paneIndex;
	private int _shapesIndex;
	private int _saveablesIndex;
	private SketchyShape _sketchyShape;

	/*
	 * When a new layer is created it takes in a pane index, saveables index, shapes
	 * index, and a sketchy shape associated with those indices. This is a more
	 * abstract view of the layer class that I justify in my README
	 */
	public Layer(int paneIndex, int shapesIndex, int saveablesIndex, SketchyShape shape) {
		_paneIndex = paneIndex;
		_shapesIndex = shapesIndex;
		_saveablesIndex = saveablesIndex;
		_sketchyShape = shape;
	}

	// returns the sketchy shape associated with a given layer
	public SketchyShape getShape() {
		return _sketchyShape;
	}

	// returns the pane index of a layer
	public int getPaneIndex() {
		return _paneIndex;
	}

	// sets the pane index of a layer to a passed in int
	public void setPaneIndex(int paneIndex) {
		_paneIndex = paneIndex;
	}

	// gets shapes index of a layer
	public int getShapesIndex() {
		return _shapesIndex;
	}

	// sets shape index of a layer
	public void setShapesIndex(int shapesIndex) {
		_shapesIndex = shapesIndex;
	}

	// gets saveables index
	public int getSaveablesIndex() {
		return _saveablesIndex;
	}

	// sets saveables index
	public void setSaveablesIndex(int saveablesIndex) {
		_saveablesIndex = saveablesIndex;
	}

}
