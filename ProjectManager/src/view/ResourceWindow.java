package view;

import java.util.Observable;
import java.util.Observer;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ResourceWindow extends Window implements Observer {

	private String windowTitle;
	private BorderPane mainPane;
	private TextField resourceName;
	private ComboBox<String> competence;
	private boolean isConfirmed;
	
	public ResourceWindow(String title, ObservableList<String> competences) {
		windowTitle = title;
		resourceName = new TextField("");
		competence = new ComboBox<String>();
		competence.setItems(competences);
		isConfirmed = false;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
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
		
		HBox resourceLine = new HBox(10);
		HBox competenceLine = new HBox(10);
		HBox confirmLine = new HBox(10);
		
		Label resourceNameLabel = new Label("Name");
		resourceLine.getChildren().add(resourceNameLabel);
		resourceLine.getChildren().add(resourceName);
		
		Label competenceLabel = new Label("Competence");
		competenceLine.getChildren().add(competenceLabel);
		competenceLine.getChildren().add(competence);
		
		Button okButton = new Button("Ok");
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	if(!resourceName.getText().isEmpty() && !competence.getSelectionModel().isEmpty()) {
            		isConfirmed = true;
            		((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
            	}
            }
        });
		Button cancleButton = new Button("Cancle");
        cancleButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
            }
        });
		confirmLine.getChildren().add(okButton);
		confirmLine.getChildren().add(cancleButton);
		
		topArea.getChildren().add(resourceLine);
		topArea.getChildren().add(competenceLine);
		topArea.getChildren().add(confirmLine);
		
		mainPane.setTop(topArea);
		return mainPane;
	}
	
	public boolean isConfirmed() {
		return isConfirmed;
	}
	
	public String getName() {
		return resourceName.getText();
	}
	
	public String getCompetence() {
		return competence.getSelectionModel().getSelectedItem();
	}
}
