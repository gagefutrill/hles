
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

public class MovieSearch extends Application {

    private Connection conn;

    public MovieSearch(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Search Movies");

        // Create labels and text fields for searching
        Label nameLabel = new Label("Title:");
        TextField nameField = new TextField();

        Label editionLabel = new Label("Edition:");
        TextField editionField = new TextField();
        
        Label directorLabel = new Label("Director:");
        TextField directorField = new TextField();
        
        Label isbnLabel = new Label("ISBN:");
        TextField isbnField = new TextField();
        
        Label pubYearLabel = new Label("Publish Year:");
        TextField pubYearField = new TextField();

        Label genreLabel = new Label("Genre:");
        TextField genreField = new TextField();
        
        Label languageLabel = new Label("Language:");
        TextField languageField = new TextField();

        // Create search button
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            // Get search parameters
            String name = nameField.getText();
            String edition = editionField.getText();
            String director = directorField.getText();
            String isbn = isbnField.getText();
            String pubYear = pubYearField.getText();
            String genre = genreField.getText();
            String language = languageField.getText();
            


            // Construct query
            String query = "SELECT * FROM hles.movie WHERE ";
                   query += "name LIKE ? OR edition LIKE ?";
                   query += "director LIKE ? OR isbn LIKE ?";
                   query += "pubYear LIKE ? OR genre LIKE ? OR language LIKE ?";
                   

            try {
                // Execute query
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "%" + name + "%");
                stmt.setString(2, "%" + edition + "%");
                stmt.setString(3, "%" + director + "%");
                stmt.setString(4, "%" + isbn + "%");
                stmt.setString(5, "%" + pubYear + "%");
                stmt.setString(6, "%" + genre + "%");
                stmt.setString(7, "%" + language + "%");
                
                ResultSet rs = stmt.executeQuery();

                // Display results in console
                while (rs.next()) {
                    int id = rs.getInt("Journal_id");
                    String nameResult = rs.getString("name");
                    String editionResult = rs.getString("Edition");
                    String directorResult = rs.getString("Director");
                    String isbnResult = rs.getString("ISBN");
                    String pubYearResult = rs.getString("Publish Year");
                    String genreResult = rs.getString("Genre");
                    String languageResult = rs.getString("Language");
              
                    
                    System.out.println(id + " " + nameResult + " " + editionResult + " " + directorResult + " " + isbnResult + " " + pubYearResult + " " + genreResult + " " + languageResult);
                }
            } catch (SQLException ex) {
                System.out.println("Error executing query: " + ex.getMessage());
            }
        });

        // Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(30);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        grid.add(nameLabel, 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(editionLabel, 2, 1);
        grid.add(editionField, 3, 1);
        grid.add(directorLabel, 0, 1);
        grid.add(directorField, 1, 1);
        grid.add(isbnLabel, 2, 0);
        grid.add(isbnField, 3, 0);
        grid.add(pubYearLabel, 0, 3);
        grid.add(pubYearField, 1, 3);
        grid.add(genreLabel, 0, 2);
        grid.add(genreField, 1, 2);
        grid.add(languageLabel, 2, 2);
        grid.add(languageField, 3, 2);
        

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(searchButton);

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.getChildren().addAll(grid, hbox);

        // Set scene and show window
        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}