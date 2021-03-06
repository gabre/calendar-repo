package app;
 
import java.sql.SQLException;
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.CalendarEntry;
import model.Descriptor;
import model.Model;
import model.Project;
import model.ProjectStep;
import view.EntryWindow;
import view.GraphEditorWindow;
import view.MainWindow;
import view.ModalWindow;
import view.ProjectImportWindow;
import view.ProjectSchedulerWindow;
import view.ResourceManagementWindow;
import view.StepEditorWindow;
import view.Window;
 
public class ProjectManagerApplication extends Application {
	// Model
	private Model model;
	
	// View
	private MainWindow mainWin;
	
	private EntryWindow entryWin;
	private Stage entryWinStage;
	
	private ProjectImportWindow importProjectWin;
	private Stage importProjectStage;
	
	private GraphEditorWindow graphEditorWin;
	private Stage graphEditorWinStage;
	
	private ProjectSchedulerWindow projSchedWin;
	private Stage projSchedWinStage;
	
	private StepEditorWindow stepEditorWin;
	private Stage stepEditorWinStage;

	private ResourceManagementWindow resourceManagementWin;
	private Stage resourceManagementWinStage;
	
	private Stage dialogStage;
	
	// ENTRY POINT
    public static void main(String[] args) {
        launch(args);
    }

	@Override
    public void start(Stage stage) {
		try {
	        model = new Model();
		} catch (Exception ex) {
			System.exit(1);
		}

        mainWin = new MainWindow(this);
        entryWin = new EntryWindow(this);
        importProjectWin = new ProjectImportWindow(this);
        projSchedWin = new ProjectSchedulerWindow(this);
        graphEditorWin = new GraphEditorWindow(this);
        stepEditorWin = new StepEditorWindow(this);
        resourceManagementWin = new ResourceManagementWindow(this);
        
        createWin(stage, mainWin);
        entryWinStage = createWin(entryWin);
        importProjectStage = createWin(importProjectWin);
        projSchedWinStage = createWin(projSchedWin);
        graphEditorWinStage = createWin(graphEditorWin);
        stepEditorWinStage = createWin(stepEditorWin);
        resourceManagementWinStage = createWin(resourceManagementWin);
        
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				model.saveToDb();
				Platform.exit();
			}
        });
    }
    
    public Model getModel() {
    	return model;
    }
    
    public void entryWindowClosed() {
		entryWinStage.close();
	}
    
    public void entryWindowClosed(CalendarEntry newEntry) {
    	model.modifyEntry(entryWin.getCurrentEntry(), newEntry);
		entryWindowClosed();
	}
    
	public void calendarEntryOpened(CalendarEntry entry) {
		entryWin.setCurrentEntry(entry);
		entryWinStage.show();
	}
	
	public void importProjectWindowOpened(Project p) {
		importProjectWin.setProject(p);
		importProjectStage.show();
	}
	
	public void importProjectWindowClosed() {
		importProjectStage.close();
	}

	public void graphEditorWinOpened() {
		graphEditorWinStage.show();
	}
	
	public void projSchedWinOpened(Project project) {
		projSchedWin.setCurrentProject(project);
		projSchedWinStage.show();
	}
	
	public void projSchedWinClosed() {
		projSchedWinStage.close();
	}	
	
	public void stepEditorWinAddModeOpened(Project project, ProjectStep step) {
		stepEditorWin.setCurrentStep(project, step, false);
		stepEditorWinStage.show();
	}	
	
	public void stepEditorWinEditModeOpened(Project project, ProjectStep step) {
		stepEditorWin.setCurrentStep(project, step, true);
		stepEditorWinStage.show();
	}	
	
	public void stepEditorWinClosed() {
		stepEditorWinStage.close();
		projSchedWin.resetMetrics();
	}	
	
	public void resourceManagementOpened() {
		resourceManagementWinStage.show();
	}
	
	public void showMessage(String message)
	{
		ModalWindow mWin = new ModalWindow("Info", message, this);
		dialogStage = createWin(mWin);
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.showAndWait();
	}
	
	public void closeModal() {
		dialogStage.close();
	}

    // This method is used to "create windows" or something like that...
    // The first stage is created "by the platform" (according to the docs)
    private Stage createWin(Window win) {
    	Stage stage = new Stage();
    	createWin(stage, win);
    	return stage;
    }
    
    private Stage createWin(Stage stage, Window win) {
    	Scene scene = new Scene(win.getView());
        // Stylesheet could be added this way:
    	scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.setTitle(win.getTitle());
        return stage;
    }	

}