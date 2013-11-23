package view;

import view.ResourceWindow;

import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import app.ProjectManagerApplication;
import model.DataManager;
import model.ResourceElement;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ResourceManagementWindow extends Window implements Observer {

	private BorderPane mainPane;
	private BorderPane contentArea;
	private TableView<ResourceElement> resourceView;
	private TableView<String> competenceView;

	public ResourceManagementWindow(ProjectManagerApplication app) {
		super();
		this.app = app;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle() {
		return new String("Project Manager / Resource Management");
	}

	// Should be factored down to smaller methods
	// If we doesn't want to use something (e.g. titleLabel) in the future, we can create a local variable and use it that way
	@SuppressWarnings("unchecked")
	@Override
	public Parent getView() {
		mainPane = new BorderPane();

		HBox topArea = new HBox(50);
        //topArea.getStyleClass().add("header");
        
        VBox resourceControlls = new VBox(5);
        
        resourceView = new TableView<ResourceElement>();
        TableColumn<ResourceElement, String> resCol1 = new TableColumn<ResourceElement, String>("Name");
        TableColumn<ResourceElement, String> resCol2 = new TableColumn<ResourceElement, String>("Competence");
        resCol1.setCellValueFactory(new PropertyValueFactory<ResourceElement,String>("name"));
        resCol2.setCellValueFactory(new PropertyValueFactory<ResourceElement,String>("competence"));
        resourceView.getColumns().addAll(resCol1, resCol2);
        try {
			resourceView.setItems(app.getDataManager().getResources());
		} catch (SQLException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		}
        resourceView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        resourceControlls.getChildren().add(resourceView);

        HBox resourceButtons = new HBox(5);
        
        Button addResourceButton = new Button("Add");
        addResourceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	ResourceWindow window = null;
            	try {
            		window = new ResourceWindow("Project Manager / Add resource", app.getDataManager().getCompetences());
            	} catch (SQLException ex) {
            	}
            	Stage stage = new Stage();
            	Scene scene = new Scene(window.getView(), 300, 200);
            	scene.getStylesheets().add("styles.css");
                stage.setScene(scene);
                stage.setTitle(window.getTitle());
                stage.showAndWait();
                if(window.isConfirmed()) {
                	try {
                		app.getDataManager().addResource(window.getName(), window.getCompetence());
                		resourceView.setItems(app.getDataManager().getResources());
                	} catch (SQLException ex) {
                		app.showMessage(ex.getMessage());
                	}
                }
            }
        });
        resourceButtons.getChildren().add(addResourceButton);
        
        Button deleteResourceButton = new Button("Delete");
        deleteResourceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //((Button)e.getSource()).setText("almafa");
            }
        });
        resourceButtons.getChildren().add(deleteResourceButton);
        
        Button editResourceButton = new Button("Edit");
        editResourceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	ResourceWindow window = null;
            	try {
            		window = new ResourceWindow("Project Manager / Edit resource", app.getDataManager().getCompetences());
            	} catch (SQLException ex) {
            		
            	}
            	Stage stage = new Stage();
            	Scene scene = new Scene(window.getView(), 300, 200);
            	scene.getStylesheets().add("styles.css");
                stage.setScene(scene);
                stage.setTitle(window.getTitle());
                stage.showAndWait();
            }
        });
        resourceButtons.getChildren().add(editResourceButton);
        
        Button filterResourceButton = new Button("Filter");
        filterResourceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	ResourceWindow window = null;
            	try {
            		window = new ResourceWindow("Project Manager / Filter resource", app.getDataManager().getCompetences());
            	} catch(SQLException ex) { 
            	}
            	Stage stage = new Stage();
            	Scene scene = new Scene(window.getView(), 300, 200);
            	scene.getStylesheets().add("styles.css");
                stage.setScene(scene);
                stage.setTitle(window.getTitle());
                stage.showAndWait();
            }
        });
        resourceButtons.getChildren().add(filterResourceButton);
        resourceControlls.getChildren().add(resourceButtons);
        
        topArea.getChildren().add(resourceControlls);

        VBox competenceControlls = new VBox(5);
        
        competenceView = new TableView<String>();
        competenceView.getColumns().clear();
        TableColumn<String, String> compCol = new TableColumn<String, String>("Competence");
        compCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<String, String> param) {
                return new ReadOnlyStringWrapper(param.getValue());
            }
        });
        competenceView.getColumns().add(compCol);
        competenceView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        try {
			competenceView.setItems(app.getDataManager().getCompetences());
		} catch (SQLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        competenceControlls.getChildren().add(competenceView);

        HBox competenceButtons = new HBox(5);
        
        Button addCompetenceButton = new Button("Add");
        addCompetenceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	CompetenceWindow window = new CompetenceWindow("Project Manager / Add competence");
            	Stage stage = new Stage();
            	Scene scene = new Scene(window.getView(), 300, 120);
            	scene.getStylesheets().add("styles.css");
                stage.setScene(scene);
                stage.setTitle(window.getTitle());
                stage.showAndWait();
                if(window.isConfirmed())
					try {
						app.getDataManager().addCompetence(window.getCompetence());
						competenceView.setItems(app.getDataManager().getCompetences());
					} catch (SQLException ex) {
						app.showMessage(ex.getMessage());
					}
            }
        });
        competenceButtons.getChildren().add(addCompetenceButton);
        
        Button deleteCompetenceButton = new Button("Delete");
        deleteCompetenceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                //((Button)e.getSource()).setText("almafa");
            }
        });
        competenceButtons.getChildren().add(deleteCompetenceButton);
        
        Button editCompetenceButton = new Button("Edit");
        editCompetenceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	CompetenceWindow window = new CompetenceWindow("Project Manager / Edit competence");
            	Stage stage = new Stage();
            	Scene scene = new Scene(window.getView(), 300, 120);
            	scene.getStylesheets().add("styles.css");
                stage.setScene(scene);
                stage.setTitle(window.getTitle());
                stage.showAndWait();
            }
        });
        competenceButtons.getChildren().add(editCompetenceButton);
        
        Button filterCompetenceButton = new Button("Filter");
        filterCompetenceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	CompetenceWindow window = new CompetenceWindow("Project Manager / Filter competence");
            	Stage stage = new Stage();
            	Scene scene = new Scene(window.getView(), 300, 120);
            	scene.getStylesheets().add("styles.css");
                stage.setScene(scene);
                stage.setTitle(window.getTitle());
                stage.showAndWait();
            }
        });
        competenceButtons.getChildren().add(filterCompetenceButton);
        competenceControlls.getChildren().add(competenceButtons);
        
        topArea.getChildren().add(competenceControlls);
        
        mainPane.setTop(topArea);

        //we use this contentPane because this way we can add style (i.e. padding)
        contentArea = new BorderPane();
        // contentArea.centerProperty().bind(model.contentProperty());
        contentArea.getStyleClass().add("body");
        mainPane.setCenter(contentArea);
        
        return mainPane;
	}
}
