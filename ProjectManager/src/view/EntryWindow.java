package view;

import java.text.SimpleDateFormat;

import model.CalendarEntry;
import app.ProjectManagerApplication;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

public class EntryWindow extends Window {
	private ProjectManagerApplication app;
	private CalendarEntry currentEntry;
	
	private Text dateText = new Text();
	private TextField nameField = new TextField();
	private TextField descriptionField = new TextField();

	public EntryWindow(ProjectManagerApplication app) {
		this.app = app;
	}

	@Override
	public String getTitle() {
		return "Új naptárbejegyzés";
	}

	@Override
	public Parent getView() {
		BorderPane mainPane = new BorderPane();
		mainPane.setPadding(new Insets(10));
		
		GridPane gridPane = new GridPane();
		gridPane.setPadding(new Insets(10));
		gridPane.setHgap(15);
		gridPane.setVgap(5);
		
		gridPane.addRow(0, new Text("Dátum:"),  dateText);
		gridPane.addRow(1, new Text("Név:"),    nameField);
		gridPane.addRow(2, new Text("Leírás:"), descriptionField);
		
		HBox buttons = new HBox();
		buttons.setSpacing(5);
		
		Button submitButton = new Button("OK");
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				app.entryWindowClosed();
			}
		});
		
		Button resetButton = new Button("Visszaállít");
		resetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateFields();
			}
		});
		
		Button cancelButton = new Button("Mégse");
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				app.entryWindowClosed();
			}
		});
		
		buttons.getChildren().addAll(submitButton, resetButton, cancelButton);
		
		mainPane.setCenter(gridPane);
		mainPane.setBottom(buttons);
		
		return mainPane;
	}
	
	public void setCurrentEntry(CalendarEntry entry) {
		currentEntry = entry;
		updateFields();
	}
	
	private void updateFields() {
		dateText.setText(new SimpleDateFormat("yyyy. MM. dd.").format(currentEntry.getDate()));
		nameField.setText(currentEntry.getName());
		descriptionField.setText(currentEntry.getDescription());
	}

}
