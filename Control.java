package Sketchy;

import cs015.fnl.SketchySupport.FileIO;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/*
 * My control class deals with all the button handling. It factors out this functionality from sketchy.
 * The control class keeps track of the current color that sketchy is operating on and handles the undo and redo command stacks.
 * Control also instantiates the io for saving and loading which is used throughout sketchy.
 */

public class Control {

	private VBox _controlPanel;
	private ToggleGroup _group;
	private RadioButton _button1;
	private RadioButton _button2;
	private RadioButton _button3;
	private RadioButton _button4;
	private Button _b1;
	private Button _b2;
	private Button _b3;
	private Button _b4;
	private Button _b5;
	private Button _b6;
	private Button _b7;
	private Button _b8;
	private Button _b9;
	private Sketchy _sketchy;
	private Color _chosenColor;
	private Stage _stage;

	/*
	 * the control constructor takes in an instance of sketchy and stage which makes
	 * it possible for the control class to handle its designated functionality
	 * surrounding sketchy. The constructor sets up all the buttons and creates a
	 * control panel which is arranges them in. the Pane passsed into the
	 * constructor then adds this control panel.
	 */
	public Control(Pane pane, Sketchy sketchyInstance, Stage stage) {
		_sketchy = sketchyInstance;
		_stage = stage;
		_controlPanel = new VBox();
		_controlPanel.setStyle("-fx-background-color: lightgray;");
		_controlPanel.setPrefSize(Constants.PANEL_WIDTH, Constants.ROOT_HEIGHT);
		_controlPanel.setSpacing(Constants.CONTROL_PANEL_SPACING);
		_controlPanel.setAlignment(Pos.CENTER);
		this.setupDrawingOptions();
		this.setupColorPicker();
		this.setupShapeActions();
		this.setupOperations();
		this.setupQuitButton();
		pane.getChildren().add(_controlPanel);
	}

	// returns control panel
	public VBox getControlPanel() {
		return _controlPanel;
	}

	/*
	 * this method sets up the radio buttons that are modeled by the mousehandler in
	 * sketchy. All buttons are initially set to false when added to the toggle
	 * group
	 */
	public void setupDrawingOptions() {
		Label l1 = new Label("Drawing Options:");
		_group = new ToggleGroup();
		_button1 = new RadioButton("Select shape");
		_button1.setToggleGroup(_group);
		_button1.setSelected(false);
		_button1.setOnAction(new RadioButtonHandler());
		_button1.setFocusTraversable(false);
		_button2 = new RadioButton("Draw With Pen");
		_button2.setToggleGroup(_group);
		_button2.setFocusTraversable(false);
		_button2.setOnAction(new RadioButtonHandler());
		_button3 = new RadioButton("Draw a rectangle");
		_button3.setToggleGroup(_group);
		_button3.setFocusTraversable(false);
		_button3.setOnAction(new RadioButtonHandler());
		_button4 = new RadioButton("Draw an ellipse");
		_button4.setToggleGroup(_group);
		_button4.setFocusTraversable(false);
		_button4.setOnAction(new RadioButtonHandler());
		_controlPanel.getChildren().addAll(l1, _button1, _button2, _button3, _button4);
	}

	/*
	 * my radio button handler sets the option in sketchy to each respective radio
	 * button when that button is selected. This is made more readable through enums
	 * that represent each option.
	 */
	private class RadioButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {

			if (_button1.isSelected()) {
				_sketchy.setOption(SketchyEnums.SELECT_SHAPE);
			} else if (_button2.isSelected()) {
				_sketchy.setOption(SketchyEnums.PEN);
			} else if (_button3.isSelected()) {
				_sketchy.setOption(SketchyEnums.RECTANGLE);
			} else if (_button4.isSelected()) {
				_sketchy.setOption(SketchyEnums.ELLIPSE);
			}
			e.consume();
		}
	}

	/*
	 * the java color picker is setup using this method, everytime the use chooses a
	 * new color Control switches the color to the _chosenColor.
	 */
	public void setupColorPicker() {
		Label colorLabel = new Label("Set the color:");
		final ColorPicker colorPicker = new ColorPicker();
		colorPicker.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				_chosenColor = colorPicker.getValue();
				Control.this.switchColor(_chosenColor);
			}
		});
		_controlPanel.getChildren().addAll(colorLabel, colorPicker);
	}

	/*
	 * the switch Color method is really just sketchy calling its own switch color
	 * method. I decided to have this method in Control as well as sketchy because
	 * they both arguably model this property. Sketchy itself switches its own
	 * color, but the control instance is responsible for handling the color picker
	 * and controlling the button handling aspects of Sketchy
	 */
	private void switchColor(Color color) {
		_sketchy.switchColor(color);
	}

	/*
	 * this method sets up all the regular buttons that handle actions that pertain
	 * to shapes and sets them to false and adds their respective handlers to each
	 * button
	 */
	public void setupShapeActions() {
		Label l2 = new Label("Shape Actions:");
		_b1 = new Button("Raise");
		_b1.setFocusTraversable(false);
		_b1.setOnAction(new RaiseHandler());
		_b2 = new Button("Lower");
		_b2.setFocusTraversable(false);
		_b2.setOnAction(new LowerHandler());
		_b3 = new Button("Fill");
		_b3.setFocusTraversable(false);
		_b3.setOnAction(new FillHandler());
		_b4 = new Button("Delete");
		_b4.setFocusTraversable(false);
		_b4.setOnAction(new DeleteHandler());
		_controlPanel.getChildren().addAll(l2, _b1, _b2, _b3, _b4);
	}

	/*
	 * this method sets up all the regular buttons that model the operations that
	 * can be performed by sketchy, eg undo redo save load
	 */
	public void setupOperations() {
		Label l3 = new Label("Operations:");
		_b5 = new Button("Undo");
		_b5.setFocusTraversable(false);
		_b5.setOnAction(new UndoHandler());
		_b6 = new Button("Redo");
		_b6.setFocusTraversable(false);
		_b6.setOnAction(new RedoHandler());
		_b7 = new Button("Save");
		_b7.setFocusTraversable(false);
		_b7.setOnAction(new SaveHandler());
		_b8 = new Button("Load");
		_b8.setFocusTraversable(false);
		_b8.setOnAction(new LoadHandler());
		_controlPanel.getChildren().addAll(l3, _b5, _b6, _b7, _b8);
	}

	/*
	 * this sets up the quit button and its handler
	 */
	public void setupQuitButton() {
		Label l4 = new Label("Quit");
		_b9 = new Button("Exit dis bih");
		_b9.setFocusTraversable(false);
		_controlPanel.getChildren().addAll(l4, _b9);
		_b9.setOnAction(new QuitButtonHandler());
	}

	// run of the mill quit button handler, exits platform and what not
	private class QuitButtonHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			Platform.exit();
			e.consume();
		}
	}

	/*
	 * My raise handler has the sketchy instance passed in call raise on the
	 * selectedShape.
	 */
	private class RaiseHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			_sketchy.raise(_sketchy.());
			e.consume();
		}
	}

	/*
	 * the lower handler similar to the raise handler calls on sketchy to lower the
	 * selected shape
	 */
	private class LowerHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			_sketchy.lower(_sketchy.getSelectedShape());
			e.consume();
		}
	}

	/*
	 * the fill handler fills the shape with the chosen color by calling on sketchy
	 * to fill shape if the selected shape isn't null
	 */
	private class FillHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			if (_sketchy.getSelectedShape() != null) {
				_sketchy.FillShape(_chosenColor);
			}
			e.consume();
		}
	}

	/*
	 * the delete handler adds a new Delete command every time the delete button is
	 * pressed if the selected shape is not null (if a shape is selected). It
	 * logically and graphically removes the shape from sketchy.
	 */
	private class DeleteHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			if (_sketchy.getSelectedShape() != null) {
				_sketchy.getUndoStack().push(new Delete(_sketchy.getSelectedShape(), _sketchy));
				_sketchy.getRedoStack().clear();
				_sketchy.getSketchPane().getChildren().remove(_sketchy.getSelectedShape().getSketchyShapeNode());
				_sketchy.getShapesArrayList().remove(_sketchy.getSelectedShape());
				_sketchy.getSaveables().remove(_sketchy.getSelectedShape());
				_sketchy.setSelectedShapeNull();
			}
			e.consume();
		}
	}

	/*
	 * if the undo button is pressed, if the undo stack isn't empty, the undo stack
	 * pops off a command and undoes it. The redo stack adds pushes on the command
	 * that was undone
	 */
	private class UndoHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			if (!_sketchy.getUndoStack().isEmpty()) {
				Command commandToUndo = _sketchy.getUndoStack().pop();
				commandToUndo.undo();
				_sketchy.getRedoStack().push(commandToUndo);
				e.consume();
			}
		}
	}

	/*
	 * the redo stack if not empty pops off a command and redoes it, and the undo
	 * stack pushed on the redone command
	 */
	private class RedoHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			if (!_sketchy.getRedoStack().isEmpty()) {
				Command commandToRedo = _sketchy.getRedoStack().pop();
				commandToRedo.redo();
				_sketchy.getUndoStack().push(commandToRedo);
				e.consume();
			}
		}
	}

	/*
	 * the savehandler class models what happens when save button is pressed. The
	 * selected shape if not null gets set to null and deselected. an instance of
	 * FileIO gets the file name and if the user chooses to save opens write,
	 * sketcht saves itself, and io closes write.
	 */
	private class SaveHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			if (_sketchy.getSelectedShape() != null) {
				_sketchy.getSelectedShape().getSketchyShapeNode().setStroke(null);
				_sketchy.setSelectedShapeNull();
			}
			FileIO io = new FileIO();
			String fileName = FileIO.getFileName(true, _stage);
			if (fileName != null) {
				io.openWrite(fileName);
				_sketchy.save(io);
				io.closeWrite();
			}
			e.consume();
		}
	}

	/*
	 * the load handler models what happens when the load button is pressed. If the
	 * selectedShape isn't null it gets set to null and deselected. if the user
	 * chooses to load, the io opens to read, sketchy loads the files, and the io
	 * closes read.
	 */
	private class LoadHandler implements EventHandler<ActionEvent> {

		@Override
		public void handle(ActionEvent e) {
			if (_sketchy.getSelectedShape() != null) {
				_sketchy.getSelectedShape().getSketchyShapeNode().setStroke(null);
				_sketchy.setSelectedShapeNull();
			}
			FileIO io = new FileIO();
			String fileName = FileIO.getFileName(false, _stage);
			if (fileName != null) {
				io.openRead(fileName);
				_sketchy.load(io);
				io.closeRead();
			}
			e.consume();
		}
	}
}
