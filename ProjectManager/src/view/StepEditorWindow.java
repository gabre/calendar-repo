package view;

import app.ProjectManagerApplication;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;

public class StepEditorWindow extends Window {
	
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
		return pane;
	}

}
