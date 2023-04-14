import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PublisherSearch extends Application {

    private Connection conn;
    private String user, pass, url;
    
    public PublisherSearch(String url,String user, String pass) {
        this.user = user;
        this.pass = pass;
        this.url = url;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	// Set Window Title and create connection
        primaryStage.setTitle("Search Publishers");
        conn = DriverManager.getConnection(url, user, pass);

        // Create labels and text fields for searching
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label locationLabel = new Label("Location:");
        TextField locationField = new TextField();

        // Create dropdown for choosing search operator
        Label operatorLabel = new Label("Search Operator:");
        ComboBox<String> operatorDropdown = new ComboBox<>();
        operatorDropdown.getItems().addAll("AND", "OR");
        operatorDropdown.setValue("AND");

        // Create search and back buttons
        Button searchButton = new Button("Search");
        Button backButton = new Button("Back");
        
        //Create table for output
    	TableView<Publisher> table = new TableView<>();
        table.setEditable(false);
        table.setPrefSize(400, 400);
        TableColumn<Publisher, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Publisher, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setMaxWidth(300); nameColumn.setPrefWidth(150);
        TableColumn<Publisher, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));
        locationColumn.setMaxWidth(300); locationColumn.setPrefWidth(150);
        table.getColumns().addAll(idColumn,nameColumn, locationColumn);
        
      //Create text wrapping for large fields
       nameColumn.setCellFactory(tc -> {
            TableCell<Publisher, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(nameColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
       locationColumn.setCellFactory(tc -> {
           TableCell<Publisher, String> cell = new TableCell<>();
           Text text = new Text();
           cell.setGraphic(text);
           cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
           text.wrappingWidthProperty().bind(locationColumn.widthProperty());
           text.textProperty().bind(cell.itemProperty());
           return cell ;
       });
        
        // Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(locationLabel, 0, 2);
        grid.add(locationField, 1, 2);
        grid.add(operatorLabel, 0, 1);
        grid.add(operatorDropdown, 1, 1);

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.TOP_CENTER);
        hbox.getChildren().addAll(searchButton,backButton);

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().addAll(grid, hbox);
        
        // Set scene and show window
        Scene scene = new Scene(vbox, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        
     
        
        //set Button Action
        searchButton.setOnAction(e -> {
            // Get search parameters
            String name = nameField.getText();
            String city = locationField.getText();
            String operator = operatorDropdown.getValue();

            // Construct query
            String query = "SELECT publisher_id,name, city FROM hles.publisher WHERE ";
            if (operator.equals("AND")) {
                query += "name LIKE ? AND city LIKE ?";
            } else {
                query += "name LIKE ? OR city LIKE ?";
            }

            try {
                // Execute query
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "%" + name + "%");
                stmt.setString(2, "%" + city + "%");
                ResultSet rs = stmt.executeQuery();
                //Create List to store entries in
                ObservableList<Publisher> publishers = FXCollections.observableArrayList();
                //Add results to table
                while (rs.next()) {
                	String idResult = rs.getString("publisher_id");
                    String nameResult = rs.getString("name");
                    String locationResult = rs.getString("city");
                    publishers.add(new Publisher(idResult,nameResult,locationResult));
                }
                //Add table with results to the window
        		vbox.getChildren().remove(table);
                vbox.getChildren().addAll(table);
                table.setItems(publishers);
            } catch (SQLException ex) {
                System.out.println("Error executing query: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        backButton.setOnAction(click -> {
        	SearchMenu searchMenu = new SearchMenu(url,user,pass);
        	try {
				searchMenu.start(primaryStage);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
        });
        
    }
    public static class Publisher {
		private SimpleIntegerProperty id;
    	private  SimpleStringProperty name, location;

        public Publisher(String id, String name, String location) {
        	this.id = new SimpleIntegerProperty(Integer.parseInt(id));
            this.name = new SimpleStringProperty(name);
            this.location = new SimpleStringProperty(location);
        }

        public String getName() {
            return name.get();
        }

        public String getLocation() {
            return location.get();
        }
        public int getId() {
        	return id.get();
        }
    }
}