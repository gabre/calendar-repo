package app;
 
import java.util.Date;

import view.EntryWindow;
import view.MainWindow;
import view.Window;
import model.CalendarEntry;
import model.Model;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
 
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
        model 	= new Model();
        mainWin = new MainWindow();
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
        
        entryWinStage.show();
        entryWin.setCurrentEntry(new CalendarEntry("szia", "hohoho", new Date()));
    }
    
    public void entryWindowClosed() {
		entryWinStage.close();
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