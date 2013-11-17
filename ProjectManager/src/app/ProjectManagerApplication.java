package app;
 
import java.util.Date;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.CalendarEntry;
import model.Model;
import view.EntryWindow;
import view.MainWindow;
import view.Window;
 
public class ProjectManagerApplication extends Application {
	// Model
	private Model model;
	
	// View
	private MainWindow mainWin;
	private EntryWindow entryWin;
	private Stage entryWinStage;
	
	
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
        
        mainWin = new MainWindow(this);
        entryWin = new EntryWindow(this);
        
        createWin(stage, mainWin, 800, 600);
        entryWinStage = createWin(entryWin, 320, 240);
        
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
    
	public void calendarEntryOpened(CalendarEntry entry) {
		entryWin.setCurrentEntry(entry);
		entryWinStage.show();
	}

    // This method is used to "create windows" or something like that...
    // The first stage is created "by the platform" (according to the docs)
    private Stage createWin(Window win, double width, double height) {
    	Stage stage = new Stage();
    	createWin(stage, win, width, height);
    	return stage;
    }
    
    private Stage createWin(Stage stage, Window win, double width, double height) {
    	Scene scene = new Scene(win.getView());
        // Stylesheet could be added this way:
    	scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.setTitle(win.getTitle());
        return stage;
    }

}