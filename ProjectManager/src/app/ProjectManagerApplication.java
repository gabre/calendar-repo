package app;
 
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
import view.MainWindow;
import view.ModalWindow;
import view.ProjectSchedulerWindow;
import view.StepEditorWindow;
import view.Window;
 
public class ProjectManagerApplication extends Application {
	// Model
	private Model model;
	
	// View
	private MainWindow mainWin;
	
	private EntryWindow entryWin;
	private Stage entryWinStage;
	
	private ProjectSchedulerWindow projSchedWin;
	private Stage projSchedWinStage;
	
	private StepEditorWindow stepEditorWin;
	private Stage stepEditorWinStage;

	private Stage dialogStage;
	
	
	// ENTRY POINT
    public static void main(String[] args) {
        launch(args);
    }

	@Override
    public void start(Stage stage) throws Exception {
        model = new Model();
        model.addEntry(new CalendarEntry("Elsõ", "Meg kell etetni a macskát.", new Date(113, 1, 1)));
        model.addEntry(new CalendarEntry("Második", "Meg kell itatni az iguánát.", new Date(113, 2, 2)));
        model.addEntry(new CalendarEntry("Harmadik", "Meg kell óvni az iguánát a macskától.", new Date(113, 3, 3)));
        Project almafa = new Project("almafa");
        model.addProject(almafa); 
        almafa.addStep(new ProjectStep(new Descriptor("step1", 10, "some description", 0)));
        almafa.addStep(new ProjectStep(new Descriptor("step2", 0, "blah blah blah some description", 0)));
        almafa.addStep(new ProjectStep(new Descriptor("step3", 40, "", 0)));
        
        mainWin = new MainWindow(this);
        entryWin = new EntryWindow(this);
        projSchedWin = new ProjectSchedulerWindow(this);
        stepEditorWin = new StepEditorWindow(this);
        
        createWin(stage, mainWin);
        entryWinStage = createWin(entryWin);
        projSchedWinStage = createWin(projSchedWin);
        stepEditorWinStage = createWin(stepEditorWin);
        
        
        stage.show();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
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