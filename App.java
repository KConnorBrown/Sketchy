package Sketchy;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * It's time for Sketchy! This is the main class to get things started.
 *
 * The main method of this application calls the start method. You will need to
 * fill in the start method to instantiate your game.
 *
 * This is my App class. It deals with the highest level of graphics. It passes
 * an instance of Stage to the PaneOrganizer constructor to be used in my loading
 * and saving handlers.
 *
 */

public class App extends Application {

	@Override
	public void start(Stage stage) {
		PaneOrganizer organizer = new PaneOrganizer(stage);
		Scene scene = new Scene(organizer.getRoot());
		stage.setScene(scene);
		stage.setTitle("Sketchy");
		stage.show();
	}

	/*
	 * Here is the mainline! No need to change this.
	 */
	public static void main(String[] argv) {
		// launch is a method inherited from Application
		launch(argv);
	}
}
