package Sketchy;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*
 * This is my PaneOrganizer class. It factors out the graphical layout from the app class and deals with organizing all the graphical elements within the root (border pane).
 */
public class PaneOrganizer {

	private BorderPane _root;

	/*
	 * My PaneOrganizer constructor takes in a stage in its constructor to pass on
	 * to an instance of Control which uses it in the save and load handling. In my
	 * constructor I instantiate control and sketchy and make the proper
	 * associations between them.
	 */
	public PaneOrganizer(Stage stage) {
		_root = new BorderPane();
		_root.setPrefSize(Constants.ROOT_WIDTH, Constants.ROOT_HEIGHT);
		_root.setStyle("-fx-background-color: white;");
		Pane canvasPane = new Pane();
		Sketchy sketchy = new Sketchy(canvasPane);
		Pane controlPanelPane = new Pane();
		Control control = new Control(controlPanelPane, sketchy, stage);
		_root.getChildren().addAll(canvasPane, controlPanelPane);
		_root.setCenter(sketchy.getSketchPane());
		_root.setLeft(control.getControlPanel());
	}

	// returns the BorderPane instance
	public BorderPane getRoot() {
		return _root;
	}

}
