package Sketchy;

import java.util.ArrayList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import java.util.Stack;
import cs015.fnl.SketchySupport.FileIO;

/*
 * This is my Sketchy class. It models the properties of a GUI that can perform all the actions on the buttons. Sketchy class can draw, create shapes, transform them, delete them, save, load, and quit.
 *
 */
public class Sketchy {

	private Pane _sketchPane;
	private SketchyEnums _currentOption;
	private CurvedLine _curvedLine;
	private Color _currentColor;
	private ArrayList<SketchyShape> _shapes;
	private SketchyShape _selectedShape;
	private ArrayList<Saveable> _saveables;
	private boolean _clickedOnAShape;
	private MouseEvent _currentMouseEvent;
	private Point2D _prevMousePt;
	private Point2D _currMousePt;
	private Stack<Command> _undo;
	private Stack<Command> _redo;
	private Color _selectedShapesColor;
	private int _fillCounter;
	private SketchyRectangle _sketchyRect;
	private SketchyEllipse _sketchyEllipse;
	private boolean _justRotatedOrResized;
	private Point2D _before;
	private double _startingAngle;
	private Point2D _startSize;
	private double _x;
	private double _y;

	/*
	 * My sketchy constructor sets all the instance variables that are used
	 * throughout the sketchy class to the proper values when the program is
	 * launched. The constructor also creates the undo and redo stacks as well as
	 * the shapes and saveables array lists
	 */
	public Sketchy(Pane pane) {
		_fillCounter = 0;
		_justRotatedOrResized = false;
		_before = null; // this is the initial location of the shape I'm
						// translating
		_undo = new Stack<Command>();
		_redo = new Stack<Command>();
		_selectedShape = null;
		_currMousePt = null;
		_shapes = new ArrayList<SketchyShape>();
		_saveables = new ArrayList<Saveable>();
		_currentColor = Color.WHITE;
		_sketchPane = new Pane();
		_sketchPane.addEventHandler(MouseEvent.ANY, new MouseHandler());
		_sketchPane.setPrefSize(Constants.CONTROL_PANEL_SPACING, Constants.ROOT_HEIGHT);
		pane.getChildren().add(_sketchPane);
		_currentOption = SketchyEnums.SELECT_SHAPE;
		_currentMouseEvent = null;
	}

	// returns redo stack
	public Stack<Command> getRedoStack() {
		return _redo;
	}

	// returns undo stack
	public Stack<Command> getUndoStack() {
		return _undo;
	}

	// returns sketchPane where everything that happens logically in sketchy is
	// reflected graphically
	public Pane getSketchPane() {
		return _sketchPane;
	}

	// this method switches the currentColor used throughout sketchy to whichever
	// color was passed in when the method was called
	public void switchColor(Color color) {
		_currentColor = color;
	}

	// this method fills a shape with a passed in color. Additionally, this method
	// keeps track of a selectedShape's color everytime it is changed so that
	// command can be added to the undo stack. The command is only added if fill
	// shape is called rather than just switch color which changes the color of all
	// newly created shapes and lines. This is kept track of with a counter that
	// increments everytime a shape is filled.
	public void FillShape(Color color) {
		if (_fillCounter == 0) {
			if (_selectedShape != null) {
				_selectedShapesColor = _selectedShape.getShapesInitialColor();
			}
		}
		_undo.push(new Fill(_selectedShape, _selectedShapesColor, color));
		_redo.clear(); // clear the redo stack everytime a new undoable command is added
		_selectedShape.setColor(color);
		_selectedShapesColor = color;
		_fillCounter++;

	}

	/*
	 * This method is used in the control class which takes care of all the button
	 * handling. It sets the current option for the mousehandler which logically
	 * models the functions of the radio buttons to whichever enum is passed in.
	 */
	public void setOption(SketchyEnums sketchyEnum) {
		_currentOption = sketchyEnum;
	}

	// returns selectedShape
	public SketchyShape getSelectedShape() {
		return _selectedShape;
	}

	// returns shaeps array list
	public ArrayList<SketchyShape> getShapesArrayList() {
		return _shapes;
	}

	// this method sets the selectedShape to null when necessary
	public void setSelectedShapeNull() {
		_selectedShape = null;
	}

	/*
	 * the save method takes in an instance of FileIO which loops through the array
	 * list of saveables from bottom to top thus saving them in order/preserving
	 * layering. Each saveable instance (shape or line) saves itself using its own
	 * save method. Sketchy is then graphically and logically cleared.
	 */
	public void save(FileIO io) {
		for (int i = 0; i < _saveables.size(); i++) {
			_saveables.get(i).save(io);
		}
		_sketchPane.getChildren().clear();
		_saveables.clear();
		_shapes.clear();
	}

	/*
	 * The load method clears whatever is on Sketchy both logically and graphically.
	 * While the IO has more data to read, it loops through and compares the string
	 * variable to those in the file. If it's a rect it calls the load rect helper
	 * method and so on.
	 */
	public void load(FileIO io) {
		_shapes.clear();
		_sketchPane.getChildren().clear();
		_saveables.clear();
		while (io.hasMoreData()) {
			String string = io.readString();
			if (string.equals("ellipse")) {
				this.loadEllipse(io);
			} else if (string.equals("rect")) {
				this.loadRect(io);
			} else if (string.equals("line")) {
				this.loadLine(io);
			}
		}
	}

	/*
	 * This is the load rect method. The io instance reads over the necessary info
	 * storing it in local variables which are then passed into a new sketchy rect
	 * instance which is then added graphically and logically to sketchy
	 */
	private void loadRect(FileIO io) {
		int x = (int) io.readDouble();
		int y = (int) io.readDouble();
		double width = io.readDouble();
		double height = io.readDouble();
		double rotate = io.readDouble();
		x = (int) (x - width / 2);
		y = (int) (y - height / 2);
		int r = io.readInt();
		int g = io.readInt();
		int b = io.readInt();
		_sketchyRect = new SketchyRectangle(_sketchPane, x, y, Color.rgb(r, g, b));
		_sketchyRect.setSize(width, height);
		_sketchyRect.getSketchyShapeNode().setRotate(rotate);
		_saveables.add(_sketchyRect);
		_shapes.add(_sketchyRect);
	}

	/*
	 * this method stores the info that the io reads over for a new sketchy ellipse
	 * in local variables which are then used to create the sketchy ellipse and add
	 * it graphically and logically to sketchy.
	 */
	private void loadEllipse(FileIO io) {
		int x = (int) io.readDouble();
		int y = (int) io.readDouble();
		double width = io.readDouble();
		double height = io.readDouble();
		double rotate = io.readDouble();
		int r = io.readInt();
		int g = io.readInt();
		int b = io.readInt();

		_sketchyEllipse = new SketchyEllipse(_sketchPane, x, y, Color.rgb(r, g, b, 1));
		_sketchyEllipse.setSize(width, height);
		_sketchyEllipse.setRotate(rotate);
		_saveables.add(_sketchyEllipse);
		_shapes.add(_sketchyEllipse);
	}

	/*
	 * the load line helper method stores the necessary information to instantiate a
	 * curved line (its color and the first x and y) as well as the num of points in
	 * the line (which after storing the first two is numPts -2). While there are
	 * more points to add, even numbers are x's and odds are y's, when it gets to
	 * updating the y it then adds that x and y pair and continues looping for the
	 * remaing length of the line
	 */
	private void loadLine(FileIO io) {
		int r = io.readInt();
		int g = io.readInt();
		int b = io.readInt();
		int numPts = io.readInt();
		double firstX = io.readDouble();
		double firstY = io.readDouble();

		_curvedLine = new CurvedLine(_sketchPane, firstX, firstY, Color.rgb(r, g, b));
		int i = 0;
		numPts = numPts - 2;
		while (i < numPts) {
			if (i % 2 == 0) {
				_x = io.readDouble();
			} else if (i % 2 != 0) {
				_y = io.readDouble();
				_curvedLine.getPolyline().getPoints().addAll(_x, _y);
			}
			i++;
		}
		_saveables.add(_curvedLine);
	}

	/*
	 * This method models selecting a shape when the mouse is pressed. It keeps
	 * track of the possible edge cases with overlapping shapes with a variable for
	 * selected shape and a boolean for clicking on a shape. Additionally, there are
	 * lots of instance variables that should be set to null when a shape is
	 * deselected (clicked onto the screen from a shape) or when a different shape
	 * is selected.
	 */
	private void selectShape(MouseEvent e) {
		_clickedOnAShape = false;
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
			_prevMousePt = new Point2D(e.getX(), e.getY());
			for (int i = 1; i <= _shapes.size(); i++) {
				Point2D rotatedPoint = _shapes.get(i - 1).rotatePoint(_prevMousePt,
						_shapes.get(i - 1).getCenterOfRotation(), _shapes.get(i - 1).getRotate());
				if (_shapes.get(i - 1).getSketchyShapeNode().contains(rotatedPoint.getX(), rotatedPoint.getY())) {
					// loops through and keeps setting selected shape to null so
					// that the shape on
					// top is the selected shape when two are overlapping
					if (_selectedShape != null) {
						_selectedShape.getSketchyShapeNode().setStroke(null);
					}
					_clickedOnAShape = true;
					_selectedShape = _shapes.get(i - 1);
					_selectedShape.getSketchyShapeNode().setStroke(Color.BLUE);
					_selectedShape.getSketchyShapeNode().setStrokeWidth(Constants.SHAPE_STROKE);
					_currMousePt = null;
					_justRotatedOrResized = false;
					_before = null;
				}
				// If I click on the screen and off the shape that was selected
				if (!_clickedOnAShape) {
					if (_selectedShape != null) {
						_selectedShape.getSketchyShapeNode().setStroke(null);
						_selectedShape = null;
						_currMousePt = null;
						_justRotatedOrResized = false;
						_before = null;
					}
				}
			}
		}
	}

	/*
	 * this method makes an instance of Translate which is a translate command if
	 * the mouse is pressed and at some point released while a shape is selected.
	 */
	private void newTranslate(MouseEvent e) {
		// getting the starting point
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && (!e.isShiftDown() && !e.isControlDown())) {
			_before = new Point2D(e.getX(), e.getY());
		}

		// getting the ending pt
		if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			if (_currMousePt != null && _before != null) {
				Point2D after = _selectedShape.getFinalLocation();
				_undo.push(new Translate(_selectedShape, _before, after));
				_redo.clear();
				_before = null;
			}
		}
	}

	/*
	 * similar to newTranslate this method makes a new Translate command and adds it
	 * to undo if mouse is pressed while control is down and then released at some
	 * point
	 */
	private void newRotate(MouseEvent e) {
		// getting the starting angle
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && (e.isControlDown() && !e.isShiftDown())) {
			_startingAngle = _selectedShape.getRotate();
		}

		// getting the ending angle
		if (e.getEventType() == MouseEvent.MOUSE_RELEASED && (_startingAngle != _selectedShape.getFinalRotation())) {
			_undo.push(new Rotate(_selectedShape, _startingAngle, _selectedShape.getFinalRotation()));
			_redo.clear();
			_startingAngle = _selectedShape.getFinalRotation();
		}
	}

	/*
	 * this creates a new Resize command and adds it to undo if the mouse is pressed
	 * and shift is down and then mouse is released at some point
	 */
	private void newResize(MouseEvent e) {
		// getting the starting size
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED && (e.isShiftDown() && !e.isControlDown())) {
			_startSize = _selectedShape.getSize();
		}

		// getting the ending size
		if (e.getEventType() == MouseEvent.MOUSE_RELEASED && _startSize != null) {
			_undo.push(new Resize(_selectedShape, _startSize, _selectedShape.getSize()));
			_redo.clear();
			_startSize = null;
		}
	}

	/*
	 * this method updates the previous and current mouse points which are used to
	 * rotate, translate, and resize the shapes in the mouse handler. it first gets
	 * the previousMousePt and then the current mouse pt. (this method is what
	 * continues getting a new current mouse pt).
	 */
	private void updateMousePts(MouseEvent e) {
		// whenever I release the Mouse it should set prevMousePt to null
		if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
			_prevMousePt = null;
		}
		if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
			_prevMousePt = new Point2D(e.getX(), e.getY());
		}
		if (_prevMousePt != null) {
			if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				_currentMouseEvent = e;
				if (_currentMouseEvent != null) {
					_currMousePt = new Point2D(e.getX(), e.getY());
				}
			}
		}
	}

	/*
	 * this method keeps track of all the conditions that must be met for a shape to
	 * be able to translate, and if they're all met the selectedShape translate
	 * itself and then updates the prev and curr mouse pts.
	 */
	private void translateHelper(MouseEvent e) {
		if (!e.isControlDown() && !e.isShiftDown()) {
			if (!_justRotatedOrResized) {
				if (_selectedShape != null) { // if a shape is selected
					if (_prevMousePt != null && _currMousePt != null) {
						_selectedShape.translate(_prevMousePt, _currMousePt);
						_prevMousePt = _currMousePt;
					}
				}
			}
		}
	}

	/*
	 * this method keeps track of conditions to rotate and if met the selected shape
	 * rotates itself and updates current and previous mouse pts.
	 */
	private void rotateHelper(MouseEvent e) {
		if (!e.isShiftDown() && e.isControlDown()) {
			_before = null;
			if (_selectedShape != null) { // if a shape is selected
				if (_prevMousePt != null && _currMousePt != null) {
					_selectedShape.rotate(_prevMousePt, _currMousePt);
					_prevMousePt = _currMousePt;
					_justRotatedOrResized = true;
				}
			}
		}
	}

	/*
	 * this method checks the conditions for resizing a shape and if met it selected
	 * shape resizes itself and updates current and prev mouse pts.
	 */
	private void resizeHelper(MouseEvent e) {
		if (!e.isControlDown() && e.isShiftDown()) {
			_before = null; // the starting point for a translate
							// command (_before) is set to null when rotating,
							// resizing, or when clicking onto a new
							// shape or de-selecting a shape
			if (_selectedShape != null) {
				if (_prevMousePt != null && _currMousePt != null) {
					_selectedShape.resize(_prevMousePt, _currMousePt);
					_justRotatedOrResized = true;
					_prevMousePt = _currMousePt;
				}
			}
		}
	}

	/*
	 * this method returns true if both shift and control are down otherwise it
	 * returns false. The idea behind this method is that nothing should happen
	 * while both these keys are down, so it's checked in the mousehandler
	 */
	private boolean isControlAndShiftDown(MouseEvent e) {
		if (e.isControlDown() && e.isShiftDown()) {
			return true;
		}
		return false;
	}

	/*
	 * My mouseHandler class takes care of all the radio button functions like
	 * selecting shapes, creating shapes, creating line, and transforming shapes in
	 * some way (resizing, rotating, translating).d
	 */
	private class MouseHandler implements EventHandler<MouseEvent> {

		@Override
		public void handle(MouseEvent e) {
			// every time handle is called the mouse pts alwyas update.
			Sketchy.this.updateMousePts(e);
			// the fucntionality between buttons is handled by a switch statement which
			// switches on the current option enum variable.
			switch (_currentOption) {

			case SELECT_SHAPE:
				Sketchy.this.selectShape(e);
				/*
				 * if control and shift aren't down, these methods are always being called to
				 * see if any of their functionality should be implemented. This design choice
				 * makes my mousehandler cleaner and more readable, and factors out the
				 * conditional logic into helper methods
				 */
				if (_selectedShape != null && !Sketchy.this.isControlAndShiftDown(e)) {
					Sketchy.this.newTranslate(e);
					Sketchy.this.newRotate(e);
					Sketchy.this.newResize(e);
					Sketchy.this.translateHelper(e);
					Sketchy.this.rotateHelper(e);
					Sketchy.this.resizeHelper(e);
				}
				break;

			case PEN:
				/*
				 * this section of code takes care of what should happen when the draw radio
				 * button is selected. It creates a curved line using the current color of
				 * Sketchy and adds a Draw command to the undo stack
				 */
				if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
					_curvedLine = new CurvedLine(_sketchPane, e.getX(), e.getY(), _currentColor);
					_saveables.add(_curvedLine);
					if (_selectedShape != null) {
						_selectedShape.getSketchyShapeNode().setStroke(_selectedShape.getColor());
						_selectedShape = null;
					}
				} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					_curvedLine.addPoints(e.getX(), e.getY());
				} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
					_undo.push(new Draw(Sketchy.this, _curvedLine));
					_redo.clear();
				}
				break;

			case RECTANGLE:
				/*
				 * this creates a sketchy rect which is initially resized until mouse is
				 * released, upon being released a CreateShape command is added to the undo
				 * stack.
				 */
				if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
					_prevMousePt = new Point2D(e.getX(), e.getY());
					_sketchyRect = new SketchyRectangle(_sketchPane, (int) _prevMousePt.getX(),
							(int) _prevMousePt.getY(), _currentColor);
					_shapes.add(_sketchyRect);
					_saveables.add(_sketchyRect);
					if (_selectedShape != null) {
						_selectedShape.getSketchyShapeNode().setStroke(_selectedShape.getColor());
						_selectedShape = null;
					}
				} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					if (_currMousePt != null) {
						_sketchyRect.resize(_prevMousePt, _currMousePt);
						_prevMousePt = _currMousePt;
					}
					_sketchyRect.getSketchyShapeNode().setStroke(Color.BLUE);
					_sketchyRect.getSketchyShapeNode().setStrokeWidth(Constants.SHAPE_STROKE);
				} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
					_undo.push(new CreateShape(_sketchyRect, Sketchy.this));
					_redo.clear();
					_sketchyRect.getSketchyShapeNode().setStroke(null);
				}
				break;

			case ELLIPSE:
				/*
				 * same reasoning as with the RECTANGLE section ^
				 */
				if (e.getEventType() == MouseEvent.MOUSE_PRESSED) {
					_prevMousePt = new Point2D(e.getX(), e.getY());
					_sketchyEllipse = new SketchyEllipse(_sketchPane, (int) _prevMousePt.getX(),
							(int) _prevMousePt.getY(), _currentColor);
					_shapes.add(_sketchyEllipse);
					_saveables.add(_sketchyEllipse);
					if (_selectedShape != null) {
						_selectedShape.getSketchyShapeNode().setStroke(_selectedShape.getColor());
						_selectedShape = null;
					}
				} else if (e.getEventType() == MouseEvent.MOUSE_DRAGGED) {
					_sketchyEllipse.resize(_prevMousePt, _currMousePt);
					_prevMousePt = _currMousePt;
					_sketchyEllipse.getSketchyShapeNode().setStroke(Color.BLUE);
					_sketchyEllipse.getSketchyShapeNode().setStrokeWidth(Constants.SHAPE_STROKE);
				} else if (e.getEventType() == MouseEvent.MOUSE_RELEASED) {
					_undo.push(new CreateShape(_sketchyEllipse, Sketchy.this));
					_redo.clear();
					_sketchyEllipse.getSketchyShapeNode().setStroke(null);
				}
				break;

			default:
				return;
			}
			e.consume();
		}
	}

	/*
	 * my moveShapeToLayer method is a helper method used in the raise and lower
	 * methods. It takes in a shape and a layer and then performs the necessary
	 * actions to move the shape graphically and logically to that layer
	 */
	private void moveShapeToLayer(SketchyShape shapeToMove, Layer layer) {
		// three indices of the layer I'm moving to
		int paneIndex = layer.getPaneIndex();
		int shapesIndex = layer.getShapesIndex();
		int saveablesIndex = layer.getSaveablesIndex();

		// remove the shape at the layer its currently at
		_sketchPane.getChildren().remove(shapeToMove.getSketchyShapeNode());
		_shapes.remove(shapeToMove);
		_saveables.remove(shapeToMove);

		// add the shape at the layer
		_sketchPane.getChildren().add(paneIndex, shapeToMove.getSketchyShapeNode());
		_shapes.add(shapesIndex, shapeToMove);
		_saveables.add(saveablesIndex, shapeToMove);
	}

	/*
	 * this method takes ina a shape to lower. It lowers a shape by getting the
	 * indices of that shape and the indices of where we want to move the shape. It
	 * then checks to make sure the new indices are permissible before setting the
	 * shape's layer to those indices, it moves shape to layer and adds a new Lower
	 * command to the undo stack
	 */
	public void lower(SketchyShape shape) {
		// indices of where the shape is currently
		int currPaneIndex = _sketchPane.getChildren().indexOf(shape.getSketchyShapeNode());
		int currShapesIndex = _shapes.indexOf(shape);
		int currSaveablesIndex = _saveables.indexOf(shape);

		// indices of where I want to move the shape to
		int newPaneIndex = _sketchPane.getChildren().indexOf(shape.getSketchyShapeNode()) - 1;
		int newShapesIndex = _shapes.indexOf(shape) - 1;
		int newSaveablesIndex = _saveables.indexOf(shape) - 1;
		Layer layer = new Layer(currPaneIndex, currShapesIndex, currSaveablesIndex, shape);

		if (newPaneIndex >= 0) {
			layer.setPaneIndex(newPaneIndex);
		}
		if (newShapesIndex >= 0) {
			layer.setShapesIndex(newShapesIndex);
		}
		if (newSaveablesIndex >= 0) {
			layer.setSaveablesIndex(newSaveablesIndex);
		}
		this.moveShapeToLayer(shape, layer);
		_undo.push(new Lower(this, layer));
		_redo.clear();
	}

	/*
	 * the same as lower method but it checks the indices above the current shapes
	 * indices to make sure the shape isn't at the top of any of the array lists it
	 * belongs to. It adds a raise command to the stack
	 */
	public void raise(SketchyShape shape) {
		// indices of where the shape is currently
		int currPaneIndex = _sketchPane.getChildren().indexOf(shape.getSketchyShapeNode());
		int currShapesIndex = _shapes.indexOf(shape);
		int currSaveablesIndex = _saveables.indexOf(shape);

		// indices where I want to move the shape
		int newPaneIndex = _sketchPane.getChildren().indexOf(shape.getSketchyShapeNode()) + 1;
		int newShapesIndex = _shapes.indexOf(shape) + 1;
		int newSaveablesIndex = _saveables.indexOf(shape) + 1;
		Layer layer = new Layer(currPaneIndex, currShapesIndex, currSaveablesIndex, shape);
		if (newPaneIndex <= _sketchPane.getChildren().size() - 1) {
			layer.setPaneIndex(newPaneIndex);
		}
		if (newShapesIndex <= _shapes.size() - 1) {
			layer.setShapesIndex(newShapesIndex);
		}
		if (newSaveablesIndex <= _saveables.size() - 1) {
			layer.setSaveablesIndex(newSaveablesIndex);
		}
		this.moveShapeToLayer(shape, layer);
		_undo.push(new Raise(this, layer));
		_redo.clear();
	}

	//returns saveables array list
	public ArrayList<Saveable> getSaveables() {
		return _saveables;
	}

}
