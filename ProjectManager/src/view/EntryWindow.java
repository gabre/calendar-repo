package view;

import app.ProjectManagerApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

public class EntryWindow extends Window {
	private ProjectManagerApplication app;

	public EntryWindow(ProjectManagerApplication app) {
		this.app = app;
	}

	@Override
	public String getTitle() {
		return "Új naptárbejegyzés";
	}

	@Override
	public Parent getView() {
		final AnchorPane mainPane = new AnchorPane();
		
		final GridPane gridPane = new GridPane();
		gridPane.add(new Text("Dátum:"), 0, 0);
		gridPane.add(new Text("ggg gg"), 1, 0);
		
		Button exitButton = new Button("OK");
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				app.entryWindowClosed();
			}
		});
		
		mainPane.getChildren().addAll(gridPane, exitButton);
		AnchorPane.setTopAnchor(gridPane, 10.);
		AnchorPane.setBottomAnchor(exitButton, 10.);
		
		return mainPane;
	}

}
