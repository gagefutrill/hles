
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

public class JournalSearch extends Application {

    private Connection conn;

    public JournalSearch(Connection conn) {
        this.conn = conn;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Search Journals");

        // Create labels and text fields for searching
        Label nameLabel = new Label("Title:");
        TextField nameField = new TextField();

        Label issueLabel = new Label("Issue:");
        TextField issueField = new TextField();
        
        Label volumeLabel = new Label("Volume:");
        TextField volumeField = new TextField();
        
        Label issnLabel = new Label("ISSN:");
        TextField issnField = new TextField();
        
        Label numArtLabel = new Label("Number of Articles:");
        TextField numArtField = new TextField();
        
        Label pubdateLabel = new Label("Publish Date:");
        TextField pubdateField = new TextField();
        
        Label genreLabel = new Label("Genre:");
        TextField genreField = new TextField();
        
        Label languageLabel = new Label("Language:");
        TextField languageField = new TextField();

        // Create search button
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            // Get search parameters
            String name = nameField.getText();
            String issue = issueField.getText();
            String volume = volumeField.getText();
            String issn = issnField.getText();
            String numArt = numArtField.getText();
            String pubDate = pubdateField.getText();
            String genre = pubdateField.getText();
            String language = pubdateField.getText();
            
      

            // Construct query
            String query = "SELECT * FROM hles.journal WHERE ";
                   query += "name LIKE ? OR issue LIKE ?";
                   query += "volume LIKE ? OR issn LIKE ?";
                   query += "numArt LIKE ? OR pubDate LIKE ?";
                   query += "genre LIKE ? OR language LIKE ?";

            try {
                // Execute query
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "%" + name + "%");
                stmt.setString(2, "%" + issue + "%");
                stmt.setString(3, "%" + volume + "%");
                stmt.setString(4, "%" + issn + "%");
                stmt.setString(5, "%" + numArt + "%");
                stmt.setString(6, "%" + pubDate + "%");
                stmt.setString(7, "%" + genre + "%");
                stmt.setString(8, "%" + language + "%");
                
                ResultSet rs = stmt.executeQuery();

                // Display results in console
                while (rs.next()) {
                    int id = rs.getInt("Journal_id");
                    String nameResult = rs.getString("name");
                    String issueResult = rs.getString("issue");
                    String volumeResult = rs.getString("volume");
                    String issnResult = rs.getString("issn");
                    String numArtResult = rs.getString("Number of Articles");
                    String pubdateResult = rs.getString("Publish Date");
                    String genreResult = rs.getString("genre");
                    String languageResult = rs.getString("language");
              
                    
                    System.out.println(id + " " + nameResult + " " + issueResult + " " + volumeResult + " " + issnResult + " " + numArtResult + " " + pubdateResult + " " + genreResult + " " + languageResult);
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
        
        grid.add(issueLabel, 2, 1);
        grid.add(issueField, 3, 1);

        grid.add(volumeLabel, 0, 1);
        grid.add(volumeField, 1, 1);
        
        grid.add(issnLabel, 2, 0);
        grid.add(issnField, 3, 0);
        
        grid.add(numArtLabel, 2, 3);
        grid.add(numArtField, 3, 3);
        
        grid.add(pubdateLabel, 0, 3);
        grid.add(pubdateField, 1, 3);
        
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