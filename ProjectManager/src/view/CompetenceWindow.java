package view;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
		HBox competenceLine = new HBox(10);
		HBox confirmLine = new HBox(10);
		Label competenceLabel = new Label("Kompetencia:");
		Button okButton = new Button("Ok");
		Button cancelButton = new Button("Mégse");
		
		competenceLine.setPadding(new Insets(10, 0, 0, 10));
		confirmLine.setPadding(new Insets(0, 10, 0, 10));
		confirmLine.setAlignment(Pos.BASELINE_RIGHT);
		
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	isConfirmed = true;
            	((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
            }
        });
		
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
            }
        });
        
        competenceLine.getChildren().addAll(competenceLabel, competenceName);
		confirmLine.getChildren().addAll(okButton, cancelButton);
		topArea.getChildren().addAll(competenceLine, confirmLine);
		
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

