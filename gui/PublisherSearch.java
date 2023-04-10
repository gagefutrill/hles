
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PublisherSearch extends Application {

    private Connection conn;

    public PublisherSearch(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Search Publishers");

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

        // Create search button
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            // Get search parameters
            String name = nameField.getText();
            String location = locationField.getText();
            String operator = operatorDropdown.getValue();

            // Construct query
            String query = "SELECT * FROM hles.publisher WHERE ";
            if (operator.equals("AND")) {
                query += "name LIKE ? AND location LIKE ?";
            } else {
                query += "name LIKE ? OR location LIKE ?";
            }

            try {
                // Execute query
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "%" + name + "%");
                stmt.setString(2, "%" + location + "%");
                ResultSet rs = stmt.executeQuery();

                // Display results in console
                while (rs.next()) {
                    int id = rs.getInt("publisher_id");
                    String nameResult = rs.getString("name");
                    String locationResult = rs.getString("location");
                    System.out.println(id + " " + nameResult + " " + locationResult);
                }
            } catch (SQLException ex) {
                System.out.println("Error executing query: " + ex.getMessage());
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
        grid.add(locationLabel, 0, 1);
        grid.add(locationField, 1, 1);
        grid.add(operatorLabel, 0, 2);
        grid.add(operatorDropdown, 1, 2);

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(searchButton);

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(grid, hbox);

        // Set scene and show window
        Scene scene = new Scene(vbox, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
