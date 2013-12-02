package view;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import model.CalendarEntry;
import model.Descriptor;
import model.Project;
import model.ProjectStep;
import app.ProjectManagerApplication;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class StepEditorWindow extends Window {
	
	private ProjectStep currentStep;
	private Project currentProject;
	private boolean justUpdate = false;
	
	private TextField nameField = new TextField();
	private TextField durationField = new TextField();
	private TextField descriptionField = new TextField();
	private TextField difficultyField = new TextField();
	private TextField costField = new TextField();
	private ChoiceBox competenceBox = new ChoiceBox();
	
	public StepEditorWindow(ProjectManagerApplication app) {
		super();
		this.app = app;
	}

	@Override
	public String getTitle() {
		return new String("Project step editor");
	}

	@Override
	public Parent getView() {
		BorderPane pane = new BorderPane();
		VBox vBox = new VBox();
		
		Label l1 = new Label("Név:");
		Label l2 = new Label("Idõtartam:");
		Label l3 = new Label("Leírás:");
		Label l4 = new Label("Nehézség:");
		Label l5 = new Label("Költség:");
		Label l6 = new Label("Szükséges tudás:");
		
		Button okButton = new Button("Rendben");
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				submitFields();
			}
		});
		
		vBox.getChildren().addAll(l1, nameField, l2, durationField, l3, descriptionField, 
								  l4, difficultyField, l5, costField, l6, competenceBox,
								  okButton);
		pane.setCenter(vBox);
		return pane;
	}

	protected void submitFields() {
		String name = nameField.getText();
		String description = descriptionField.getText();
		int duration = Integer.parseInt(durationField.getText());
		int dif = Integer.parseInt(difficultyField.getText());
		int cost = Integer.parseInt(costField.getText());
		ProjectStep newStep = new ProjectStep(new Descriptor(name, duration, description, dif, 
				(String)competenceBox.getSelectionModel().getSelectedItem(), cost));
		if(justUpdate)
		{
			currentProject.editStep(currentStep, newStep);
		} else
		{
			currentProject.addStep(newStep);
		}
		app.stepEditorWinClosed();
	}

	public void setCurrentStep(Project project, ProjectStep step, boolean justUpdate) {
		this.justUpdate = justUpdate;
		this.currentProject = project;
		this.currentStep = step;		
		
		nameField.setText(step.getName());
		durationField.setText(Integer.toString(step.getDuration()));
		descriptionField.setText(step.getDescription());
		difficultyField.setText(Integer.toString(step.getDifficulty()));
		costField.setText(Integer.toString(step.getCost()));
		try {
			competenceBox.setItems(app.getDataManager().getCompetences());
		} catch (SQLException e) {
			
		}
		competenceBox.getSelectionModel().select(step.getNeededCompetence());
	}

}
