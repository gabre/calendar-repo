package view;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import model.Project;
import model.ProjectImporter;
import app.ProjectManagerApplication;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProjectImportWindow extends Window {
	private static final DateFormat dateFormat = new SimpleDateFormat("yyyy. MM. dd.");
	
	private Project project;
	
	private TextField startDateField = new TextField();
	private Button btnOk = new Button("Ok");
	private Button btnCancel = new Button("Mégse");

	public ProjectImportWindow(final ProjectManagerApplication app) {
		super();
		
		startDateField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> event,
					String from, String to) {
				validateDateField(to);
			}
		});
		
		btnOk.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					Date date = dateFormat.parse(startDateField.getText());
					ProjectImporter.importProject(project, date, app.getModel().getCalendarEntries());
					app.importProjectWindowClosed();
				} catch (ParseException e) {}
				
			}
		});
		
		btnCancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				app.importProjectWindowClosed();
			}
		});
	}

	@Override
	public String getTitle() {
		return "Projekt naptárba vitele";
	}

	@Override
	public Parent getView() {
		VBox box = new VBox(5);
		box.setPadding(new Insets(5));
		
		HBox input = new HBox(5);
		input.getChildren().addAll(new Label("Kezdés dátuma:"), startDateField);
		
		HBox buttons = new HBox(5);
		buttons.getChildren().addAll(btnOk, btnCancel);
		
		box.getChildren().addAll(input, buttons);
		return box;
	}

	public void setProject(Project p) {
		project = p;
		startDateField.setText(dateFormat.format(new Date()));
	}
	
	private void validateDateField(String str) {
		try {
			dateFormat.parse(str);
			btnOk.setDisable(false);
		} catch (ParseException ex) {
			btnOk.setDisable(true);
		}
	}

}
