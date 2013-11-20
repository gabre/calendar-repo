package view;

import app.ProjectManagerApplication;
import javafx.scene.Parent;

public abstract class Window {
	protected ProjectManagerApplication app;
	
	public abstract String getTitle();
	public abstract Parent getView();
}
