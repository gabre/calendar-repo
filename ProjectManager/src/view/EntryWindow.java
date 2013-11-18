package view;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import model.CalendarEntry;
import app.ProjectManagerApplication;

public class EntryWindow extends Window {
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy. MM. dd.");
	
	private ProjectManagerApplication app;
	private CalendarEntry currentEntry;
	
	private TextField dateField = new TextField();
	private TextField nameField = new TextField();
	private TextField descriptionField = new TextField();
	
	private Button submitButton = new Button("OK");
	private Button resetButton = new Button("Visszaállít");
	private Button cancelButton = new Button("Mégsem");

	public EntryWindow(final ProjectManagerApplication app) {
		this.app = app;
		
		dateField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> event,
					String from, String to) {
				validateDateField(to);
			}
		});
		
		submitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				submitFields();
			}
		});
		
		resetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				updateFields();
			}
		});
		
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				app.entryWindowClosed();
			}
		});
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
		
		gridPane.addRow(0, new Text("Dátum:"),  dateField);
		gridPane.addRow(1, new Text("Név:"),    nameField);
		gridPane.addRow(2, new Text("Leírás:"), descriptionField);
		
		HBox buttons = new HBox();
		buttons.setSpacing(5);
		buttons.getChildren().addAll(submitButton, resetButton, cancelButton);
		
		mainPane.setCenter(gridPane);
		mainPane.setBottom(buttons);
		
		return mainPane;
	}
	
	public CalendarEntry getCurrentEntry() {
		return currentEntry;
	}

	public void setCurrentEntry(CalendarEntry entry) {
		currentEntry = entry;
		updateFields();
	}
	
	private void updateFields() {
		dateField.setText(dateFormat.format(currentEntry.getDate()));
		nameField.setText(currentEntry.getName());
		descriptionField.setText(currentEntry.getDescription());
	}
	
	private void validateDateField(String str) {
		try {
			dateFormat.parse(str);
			submitButton.setDisable(false);
		} catch (ParseException ex) {
			submitButton.setDisable(true);
		}
	}
	
	private void submitFields() {
		String name = nameField.getText();
		String description = descriptionField.getText();
		Date date = null;
		try {
			date = dateFormat.parse(dateField.getText());
		} catch (ParseException ex) { }
		
		if (date == null || name.isEmpty()) {
			return;
		}
		
		app.entryWindowClosed(new CalendarEntry(name, description, date));
	}

}
