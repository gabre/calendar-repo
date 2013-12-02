package view;

import view.ResourceWindow;

import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;

import app.ProjectManagerApplication;
import model.ResourceElement;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
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
	private Button addResourceButton;
	private Button deleteResourceButton;
	private Button editResourceButton;
	private Button filterResourceButton;

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
		return new String("Erõforráskezelõ");
	}

	// Should be factored down to smaller methods
	// If we doesn't want to use something (e.g. titleLabel) in the future, we can create a local variable and use it that way
	@SuppressWarnings("unchecked")
	@Override
	public Parent getView() {
		mainPane = new BorderPane();

		HBox topArea = new HBox(50);
        
        VBox resourceControlls = new VBox(5);
        
        resourceView = new TableView<ResourceElement>();
        TableColumn<ResourceElement, String> resCol1 = new TableColumn<ResourceElement, String>("Név");
        TableColumn<ResourceElement, String> resCol2 = new TableColumn<ResourceElement, String>("Kompetencia");
        resCol1.setCellValueFactory(new PropertyValueFactory<ResourceElement,String>("name"));
        resCol2.setCellValueFactory(new PropertyValueFactory<ResourceElement,String>("competence"));
        resourceView.getColumns().addAll(resCol1, resCol2);
        resourceView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        try {
			resourceView.setItems(app.getDataManager().getResources());
		} catch (SQLException ex) {
			app.showMessage(ex.getMessage());
		}
        resourceView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        resourceControlls.getChildren().add(resourceView);

        HBox resourceButtons = new HBox(5);
        
        addResourceButton = new Button("Felvétel");
        addResourceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	ResourceWindow window = null;
            	try {
            		window = new ResourceWindow("Új erõforrás felvétele", app.getDataManager().getCompetences(), null);
            		showWindow(window, 300, 130);
            		if(window.isConfirmed()) {
                    	app.getDataManager().addResource(window.getName(), window.getCompetence());
                    	resourceView.setItems(app.getDataManager().getResources());
                    }
            	} catch (SQLException ex) {
            		app.showMessage(ex.getMessage());
            	}
            }
        });
        resourceButtons.getChildren().add(addResourceButton);
        
        deleteResourceButton = new Button("Törlés");
        deleteResourceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	if(!resourceView.getSelectionModel().isEmpty())
            		try {
            			app.getDataManager().deleteResource(resourceView.getSelectionModel().getSelectedItem());
            			resourceView.setItems(app.getDataManager().getResources());
            		} catch (SQLException ex) {
            			app.showMessage(ex.getMessage());
            		}
            }
        });
        resourceButtons.getChildren().add(deleteResourceButton);
        
        editResourceButton = new Button("Szerkesztés");
        editResourceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	if(!resourceView.getSelectionModel().isEmpty())
            		try {
            			ResourceWindow window = new ResourceWindow("Erõforrás szerkesztése", app.getDataManager().getCompetences(), resourceView.getSelectionModel().getSelectedItem());
            			showWindow(window, 300, 130);
            			if(window.isConfirmed()) {
            				app.getDataManager().editResource(resourceView.getSelectionModel().getSelectedItem(), window.getName(), window.getCompetence());
            				resourceView.setItems(app.getDataManager().getResources());
            			}
            		} catch (SQLException ex) {
            			app.showMessage(ex.getMessage());
            		}
            }
        });
        resourceButtons.getChildren().add(editResourceButton);
        resourceControlls.getChildren().add(resourceButtons);
        
        HBox resourceFilterElements = new HBox(10);
        
        Label resourceFilterLabel = new Label("Szûrés:");
        resourceFilterElements.getChildren().add(resourceFilterLabel);
        
        resourceFilter = new TextField();
        resourceFilterElements.getChildren().add(resourceFilter);
        
        filterResourceButton = new Button("Szûrés");
        filterResourceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	try {
					ObservableList<ResourceElement> values = app.getDataManager().getResources();
	            	for(int i = values.size() - 1; i>=0; --i )
	            	{
	            		if(!values.get(i).getName().startsWith(resourceFilter.getText()))
	            			values.remove(i);
	            	}
	            	resourceView.setItems(values);
				} catch (SQLException ex) {
					app.showMessage(ex.getMessage());
				}
            }
        });
        resourceFilterElements.getChildren().add(filterResourceButton);
        resourceControlls.getChildren().add(resourceFilterElements);
        
        topArea.getChildren().add(resourceControlls);

        VBox competenceControlls = new VBox(5);
        
        competenceView = new TableView<String>();
        competenceView.getColumns().clear();
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
        try {
			competenceView.setItems(app.getDataManager().getCompetences());
		} catch (SQLException ex) {
			app.showMessage(ex.getMessage());
		}
        competenceControlls.getChildren().add(competenceView);

        HBox competenceButtons = new HBox(5);
        
        Button addCompetenceButton = new Button("Felvétel");
        addCompetenceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	CompetenceWindow window = new CompetenceWindow("Új kompetencia felvétele", "");
            	showWindow(window, 300, 100);
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
        
        Button deleteCompetenceButton = new Button("Törlés");
        deleteCompetenceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	if(!competenceView.getSelectionModel().isEmpty())
            		try {
            			app.getDataManager().deleteCompetence(competenceView.getSelectionModel().getSelectedItem());
            			competenceView.setItems(app.getDataManager().getCompetences());
            		} catch (SQLException ex) {
            			app.showMessage(ex.getMessage());
            		}
            }
        });
        competenceButtons.getChildren().add(deleteCompetenceButton);
        
        Button editCompetenceButton = new Button("Szerkesztés");
        editCompetenceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
            	if(!competenceView.getSelectionModel().isEmpty()) {
            		CompetenceWindow window = new CompetenceWindow("Kompetencia szerkesztése", competenceView.getSelectionModel().getSelectedItem());
            		showWindow(window, 300, 100);
            		if(window.isConfirmed()) {
            			try {
            				app.getDataManager().editCompetence(competenceView.getSelectionModel().getSelectedItem(), window.getCompetence());
            				competenceView.setItems(app.getDataManager().getCompetences());
            				resourceView.setItems(app.getDataManager().getResources());
            			} catch (SQLException ex) {
            				app.showMessage(ex.getMessage());
            			}
            		}
            	}
            }
        });
        competenceButtons.getChildren().add(editCompetenceButton);
        competenceControlls.getChildren().add(competenceButtons);
        
        HBox competenceFilterElements = new HBox(10);
        
        Label competenceFilterLabel = new Label("Szûrés:");
        competenceFilterElements.getChildren().add(competenceFilterLabel);
        
        competenceFilter = new TextField();
        competenceFilterElements.getChildren().add(competenceFilter);
        
        Button filterCompetenceButton = new Button("Szûrés");
        filterCompetenceButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
				try {
					ObservableList<String> values = app.getDataManager().getCompetences();
	            	for(int i = values.size() - 1; i>=0; --i )
	            	{
	            		if(!values.get(i).startsWith(competenceFilter.getText()))
	            			values.remove(i);
	            	}
	            	competenceView.setItems(values);
				} catch (SQLException ex) {
					app.showMessage(ex.getMessage());
				}
            }
        });
        competenceFilterElements.getChildren().add(filterCompetenceButton);
        competenceControlls.getChildren().add(competenceFilterElements);
        
        topArea.getChildren().add(competenceControlls);
        
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
}
