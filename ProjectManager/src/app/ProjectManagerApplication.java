package app;
 
import view.MainWindow;
import view.Window;
import model.Model;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class ProjectManagerApplication extends Application {
	
	// Model
	private Model model;
	
	// View
	private MainWindow mainWin;
	
	// ENTRY POINT
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        model 	= new Model();
        mainWin = new MainWindow();
        showWin(stage, mainWin);
    }

    // This method is used to "create windows" or something like that...
    // The first stage is created "by the platform" (according to the docs)
    private void showWin(Window win) 
    {
    	Stage stage = new Stage();
    	showWin(stage, win);
    }
    
    private void showWin(Stage stage, Window win) 
    {
    	Scene scene = new Scene(win.getView(), 800, 600);
        // Stylesheet could be added this way:
    	scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.setTitle(win.getTitle());
        stage.show();
    }
}