package view;

import java.util.Observable;
import java.util.Observer;

import model.ResourceElement;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
	
	public ResourceWindow(String title, ObservableList<String> competences, ResourceElement value) {
		windowTitle = title;
		resourceName = new TextField("");
		competence = new ComboBox<String>();
		competence.setItems(competences);
		isConfirmed = false;
		if(value != null)
		{
			resourceName.setText(value.getName());
			competence.getSelectionModel().select(value.getCompetence());
		}
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
		HBox resourceLine = new HBox(10);
		HBox competenceLine = new HBox(10);
		HBox confirmLine = new HBox(10);
		Label resourceNameLabel = new Label("N�v:");
		Label competenceLabel = new Label("Kompetencia:");
		Button okButton = new Button("Ok");
		Button cancelButton = new Button("M�gse");
		
		resourceLine.setPadding(new Insets(10, 0, 0, 10));
		competenceLine.setPadding(new Insets(10, 0, 0, 10));
		confirmLine.setPadding(new Insets(0, 10, 0, 10));
		confirmLine.setAlignment(Pos.BASELINE_RIGHT);
		
        okButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	if(!resourceName.getText().isEmpty() && !competence.getSelectionModel().isEmpty()) {
            		isConfirmed = true;
            		((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
            	}
            }
        });
		
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	((Stage)(((Button)e.getSource()).getScene().getWindow())).close();
            }
        });
        
        resourceLine.getChildren().addAll(resourceNameLabel, resourceName);
		competenceLine.getChildren().addAll(competenceLabel, competence);
		confirmLine.getChildren().addAll(okButton, cancelButton);
		
		topArea.getChildren().addAll(resourceLine, competenceLine, confirmLine);
		
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
