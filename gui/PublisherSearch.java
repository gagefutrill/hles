
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

        
        TableView<Publisher> table = new TableView<>();
        table.setEditable(false);
        table.setPrefSize(400, 200);

        TableColumn<Publisher, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Publisher, String> locationColumn = new TableColumn<>("Location");
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("location"));

        table.getColumns().addAll(nameColumn, locationColumn);

        
        // Create dropdown for choosing search operator
        Label operatorLabel = new Label("Search Operator:");
        ComboBox<String> operatorDropdown = new ComboBox<>();
        operatorDropdown.getItems().addAll("AND", "OR");
        operatorDropdown.setValue("AND");

        // Create search button
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            // Get search parameters
            String name = nameField.getText();
            String city = locationField.getText();
            String operator = operatorDropdown.getValue();

            // Construct query
            String query = "SELECT name, city FROM hles.publisher WHERE ";
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
                ObservableList<Publisher> publishers = FXCollections.observableArrayList();
                // Display results in console
                while (rs.next()) {
                    String nameResult = rs.getString("name");
                    String locationResult = rs.getString("city");
                    //System.out.println("Name: " + nameResult + " Location: " + locationResult);
                    publishers.add(new Publisher(nameResult,locationResult));
                }
                table.setItems(publishers);
            } catch (SQLException ex) {
                System.out.println("Error executing query: " + ex.getMessage());
                ex.printStackTrace();
            }
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
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(searchButton);

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(grid, hbox, table);

        // Set scene and show window
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static class Publisher {
        private  SimpleStringProperty name;
        private  SimpleStringProperty location;

        public Publisher(String name, String location) {
            this.name = new SimpleStringProperty(name);
            this.location = new SimpleStringProperty(location);
        }

        public String getName() {
            return name.get();
        }

        public String getLocation() {
            return location.get();
        }
    }
}