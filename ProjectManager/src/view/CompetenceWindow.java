package view;

import java.util.Observable;
import java.util.Observer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CompetenceWindow extends Window {

	private String windowTitle;
	private BorderPane mainPane;
	private TextField competenceName;
	private boolean isConfirmed;
	
	public CompetenceWindow(String title, String value) {
		windowTitle = title;
		competenceName = new TextField();
		isConfirmed = false;
		if (!value.isEmpty())
			competenceName.setText(value);
	}

	@Override
	public String getTitle() {
		return windowTitle;
	}
	
	@Override
	public Parent getView() {
		mainPane = new BorderPane();
		
		VBox topArea = new VBox(10);
		//topArea.getStyleClass().add("header");
		
		HBox competenceLine = new HBox(10);
		HBox confirmLine = new HBox(10);
		
		Label competenceLabel = new Label("Competence");
		competenceLine.getChildren().add(competenceLabel);
		competenceLine.getChildren().add(competenceName);
		
		Button okButton = new Button("Ok");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	isConfirmed = true;
            	((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
            }
        });
		Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
            }
        });
		confirmLine.getChildren().add(okButton);
		confirmLine.getChildren().add(cancelButton);
		
		topArea.getChildren().add(competenceLine);
		topArea.getChildren().add(confirmLine);
		
		mainPane.setTop(topArea);
		return mainPane;
	}
	
	public boolean isConfirmed() {
		return isConfirmed;
	}
	
	public String getCompetence() {
		return competenceName.getText();
	}
}

