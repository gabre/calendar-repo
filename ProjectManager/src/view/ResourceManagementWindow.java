package view;

import view.ResourceWindow;

import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import app.ProjectManagerApplication;
import model.ResourceElement;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class ResourceManagementWindow extends Window implements Observer {

	private BorderPane mainPane;
	private TableView<ResourceElement> resourceView;
	private TableView<String> competenceView;
	private TextField resourceFilter;
	private TextField competenceFilter;

	@SuppressWarnings("unchecked")
	public ResourceManagementWindow(ProjectManagerApplication app) {
		super();
		this.app = app;
		
		resourceView = new TableView<ResourceElement>();
        TableColumn<ResourceElement, String> resCol1 = new TableColumn<ResourceElement, String>("Név");
        TableColumn<ResourceElement, String> resCol2 = new TableColumn<ResourceElement, String>("Kompetencia");
        resCol1.setCellValueFactory(new PropertyValueFactory<ResourceElement,String>("name"));
        resCol2.setCellValueFactory(new PropertyValueFactory<ResourceElement,String>("competence"));
        resourceView.getColumns().addAll(resCol1, resCol2);
        resourceView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        resourceView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        
        competenceView = new TableView<String>();
        TableColumn<String, String> compCol = new TableColumn<String, String>("Kompetencia");
        compCol.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<String,String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(CellDataFeatures<String, String> param) {
                return new ReadOnlyStringWrapper(param.getValue());
            }
        });
        competenceView.getColumns().add(compCol);
        competenceView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        competenceView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        resourceFilter = new TextField();
        competenceFilter = new TextField();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTitle() {
		return new String("Erõforráskezelõ");
	}

	// Should be factored down to smaller methods
	// If we doesn't want to use something (e.g. titleLabel) in the future, we can create a local variable and use it that way
	@Override
	public Parent getView() {
		mainPane = new BorderPane();

		HBox topArea = new HBox(50);
        
        VBox resourceControlls = new VBox(5);
        HBox resourceButtons = new HBox(5);
        HBox resourceFilterElements = new HBox(10);
        VBox competenceControlls = new VBox(5);
        HBox competenceButtons = new HBox(5);
        HBox competenceFilterElements = new HBox(10);
        
        Button addResourceButton = new Button("Felvétel");
        Button deleteResourceButton = new Button("Törlés");
        Button editResourceButton = new Button("Szerkesztés");
        Button filterResourceButton = new Button("Szûrés");
        Button addCompetenceButton = new Button("Felvétel");
        Button deleteCompetenceButton = new Button("Törlés");
        Button editCompetenceButton = new Button("Szerkesztés");
        Button filterCompetenceButton = new Button("Szûrés");
        
        Label resourceFilterLabel = new Label("Szûrés:");
        Label competenceFilterLabel = new Label("Szûrés:");
        
        resourceView.getItems().clear();
		resourceView.setItems(app.getModel().getResources());


        competenceView.getItems().clear();
		competenceView.setItems(app.getModel().getCompetences());
        
        addResourceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	addResourceClicked();
            }
        });
        
        deleteResourceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	deleteResourceClicked();
            }
        });
        
        editResourceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	editResourceClicked();
            }
        });
        
        filterResourceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	filterResourceClicked();
            }
        });
        
        addCompetenceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	addCompetenceClicked();
            }
        });
        
        deleteCompetenceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	deleteCompetenceClicked();
            }
        });
        
        editCompetenceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	editCompetenceClicked();
            }
        });
        
        filterCompetenceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
				filterCompetenceClicked();
            }
        });

        resourceControlls.getChildren().add(resourceView);
        resourceButtons.getChildren().addAll(addResourceButton, deleteResourceButton, editResourceButton);
        resourceControlls.getChildren().add(resourceButtons);
        resourceFilterElements.getChildren().addAll(resourceFilterLabel, resourceFilter, filterResourceButton);
        resourceControlls.getChildren().add(resourceFilterElements);
        
        competenceControlls.getChildren().add(competenceView);
        competenceButtons.getChildren().addAll(addCompetenceButton, deleteCompetenceButton, editCompetenceButton);
        competenceControlls.getChildren().add(competenceButtons);
        competenceFilterElements.getChildren().addAll(competenceFilterLabel, competenceFilter, filterCompetenceButton);
        competenceControlls.getChildren().add(competenceFilterElements);
        
        topArea.getChildren().addAll(resourceControlls, competenceControlls);
        
        mainPane.setTop(topArea);
        return mainPane;
	}
	
	private void showWindow(Window win, int width, int height) {
		Stage stage = new Stage();
    	Scene scene = new Scene(win.getView(), width, height);
    	scene.getStylesheets().add("styles.css");
        stage.setScene(scene);
        stage.setTitle(win.getTitle());
        stage.showAndWait();
	}
	
	private void addResourceClicked() {
		ResourceWindow window = new ResourceWindow("Új erõforrás felvétele", app.getModel().getCompetences(), null);
		showWindow(window, 300, 130);
		if(window.isConfirmed()) {
        	app.getModel().addResource(new ResourceElement(window.getName(), window.getCompetence()));
        	resourceView.setItems(app.getModel().getResources());
        }
	}
	
	private void deleteResourceClicked() {
		if(!resourceView.getSelectionModel().isEmpty()) {
			app.getModel().deleteResource(resourceView.getSelectionModel().getSelectedItem());
			resourceView.setItems(app.getModel().getResources());
		}
	}
	
	private void editResourceClicked() {
		if(!resourceView.getSelectionModel().isEmpty()) {
			ResourceWindow window = new ResourceWindow("Erõforrás szerkesztése", app.getModel().getCompetences(), resourceView.getSelectionModel().getSelectedItem());
			showWindow(window, 300, 130);
			if(window.isConfirmed()) {
				app.getModel().editResource(resourceView.getSelectionModel().getSelectedItem(), new ResourceElement(window.getName(), window.getCompetence()));
				resourceView.setItems(app.getModel().getResources());
			}
		}
	}
	
	private void filterResourceClicked() {
		ObservableList<ResourceElement> values = app.getModel().getResources();
		ObservableList<ResourceElement> current = FXCollections.observableArrayList();
    	for(int i = values.size() - 1; i>=0; --i )
    	{	
    		ResourceElement elem = values.get(i);
    		if(elem.getName().startsWith(resourceFilter.getText()))
    			current.add(elem);
    	}
    	resourceView.setItems(current);
	}
	
	private void addCompetenceClicked() {
		CompetenceWindow window = new CompetenceWindow("Új kompetencia felvétele", "");
	    showWindow(window, 300, 100);
	    if(window.isConfirmed()) {
	    	app.getModel().addCompetence(window.getCompetence());
			competenceView.setItems(app.getModel().getCompetences());
	    }
	}
	
	private void deleteCompetenceClicked() {
		if(!competenceView.getSelectionModel().isEmpty()) {
			app.getModel().deleteCompetence(competenceView.getSelectionModel().getSelectedItem());
			competenceView.setItems(app.getModel().getCompetences());
		}
	}
	
	private void editCompetenceClicked() {
		if(!competenceView.getSelectionModel().isEmpty()) {
    		CompetenceWindow window = new CompetenceWindow("Kompetencia szerkesztése", competenceView.getSelectionModel().getSelectedItem());
    		showWindow(window, 300, 100);
    		if(window.isConfirmed()) {
				app.getModel().editCompetence(competenceView.getSelectionModel().getSelectedItem(), window.getCompetence());
				competenceView.setItems(app.getModel().getCompetences());
				resourceView.setItems(app.getModel().getResources());
    		}
    	}
	}
	
	private void filterCompetenceClicked() {
		ObservableList<String> values = app.getModel().getCompetences();
		ObservableList<String> current = FXCollections.observableArrayList();
    	for(int i = values.size() - 1; i>=0; --i )
    	{
    		String element = values.get(i);
    		if(element.startsWith(competenceFilter.getText()))
    			current.add(element);
    	}
    	competenceView.setItems(current);
	}
}
