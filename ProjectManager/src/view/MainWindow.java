package view;

import java.util.Observable;
import java.util.Observer;

import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainWindow extends Window implements Observer {

	private BorderPane mainPane;
	private BorderPane contentArea;

	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle() {
		return new String("Project Manager / Calendar");
	}

	// Should be factored down to smaller methods
	// If we doesn't want to use something (e.g. titleLabel) in the future, we can create a local variable and use it that way
	@Override
	public Parent getView() {
		mainPane = new BorderPane();
		
		VBox topArea = new VBox(5);
        topArea.getStyleClass().add("header");

        Label titleLabel = new Label("So fancy!");
        titleLabel.getStyleClass().add("title");
        topArea.getChildren().add(titleLabel);

        Label tagLine = new Label("JavaFX 2.0 fancy Window!");
        tagLine.getStyleClass().add("tag-line");
        topArea.getChildren().add(tagLine);

        mainPane.setTop(topArea);

        // we use this contentPane because this way we can add style (i.e. padding)
        contentArea = new BorderPane();
        // contentArea.centerProperty().bind(model.contentProperty());
        contentArea.getStyleClass().add("body");
        mainPane.setCenter(contentArea);
        
        return mainPane;
	}

}
