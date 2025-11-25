
package ca.yorku.eecs3311.othello.viewcontroller;
import ca.yorku.eecs3311.othello.model.*;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class OthelloApplication extends Application {
	// REMEMBER: To run this in the lab put 
	// --module-path "/cs/home/pyne/Downloads/javafx-sdk-21.0.9/lib" --add-modules javafx.controls,javafx.fxml
	// in the run configuration under VM arguments.
	// You can import the JavaFX.prototype launch configuration and use it as well.
	
	private Othello othello;
	private BoardView boardView;
	private BorderPane root;
	private Label statusLabel;
	private ChoiceBox<String> p1Choice;
	private ChoiceBox<String> p2Choice;

	@Override
	public void start(Stage stage) throws Exception {
		// Create and hook up the Model, View and the controller
		
		// MODEL
		othello = new Othello();
		
		// CONTROLLER
		// CONTROLLER->MODEL hookup
	
		// VIEW
		root = new BorderPane();
		statusLabel = new Label();
		statusLabel.setPadding(new Insets(5));
		root.setBottom(statusLabel);

		p1Choice = new ChoiceBox<>();
		p1Choice.getItems().addAll("Human", "Random", "Greedy");
		p1Choice.setValue("Human");

		p2Choice = new ChoiceBox<>();
		p2Choice.getItems().addAll("Human", "Random", "Greedy");
		p2Choice.setValue("Human");

		Button restartButton = new Button("Restart");
		Button undoButton = new Button("Undo");
		Button redoButton = new Button("Redo");

		restartButton.setOnAction(e -> startNewGame());
		undoButton.setOnAction(e -> {
			if (boardView != null) boardView.undoLastMove();
		});
		redoButton.setOnAction(e -> {
			if (boardView != null) boardView.redoLastMove();
		});

		HBox topBar = new HBox(10, new Label("P1:"), p1Choice, new Label("P2:"), p2Choice,
				restartButton, undoButton, redoButton);
		topBar.setPadding(new Insets(10));
		root.setTop(topBar);

		startNewGame();
		
		// VIEW->CONTROLLER hookup
		// MODEL->VIEW hookup
		
		Scene scene = new Scene(root); 
		stage.setTitle("Othello");
		stage.setScene(scene);
				
		// LAUNCH THE GUI
		stage.show();
	}

	private void startNewGame() {
		othello = new Othello();

		Player p1Strategy = createPlayerStrategy(p1Choice.getValue(), OthelloBoard.P1);
		Player p2Strategy = createPlayerStrategy(p2Choice.getValue(), OthelloBoard.P2);

		boardView = new BoardView(othello, statusLabel, p1Strategy, p2Strategy);
		othello.attach(boardView);
		root.setCenter(boardView);
	}

	private Player createPlayerStrategy(String type, char player) {
		if ("Random".equals(type)) {
			return new PlayerRandom(othello, player);
		} else if ("Greedy".equals(type)) {
			return new PlayerGreedy(othello, player);
		} else {
			return null;
		}
	}

	public static void main(String[] args) {
		OthelloApplication view = new OthelloApplication();
		launch(args);
	}
}
