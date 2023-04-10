package gui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SearchMenu extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Set the title of the window
        primaryStage.setTitle("Search Menu");
        
        // Create the buttons
        Button publisherBtn = new Button("Publisher");
        Button authorBtn = new Button("Author");
        Button journalBtn = new Button("Journal");
        Button articleBtn = new Button("Article");
        Button bookBtn = new Button("Book");
        Button movieBtn = new Button("Movie");
        
        // Set the size of the buttons
        publisherBtn.setPrefSize(200, 50);
        authorBtn.setPrefSize(200, 50);
        journalBtn.setPrefSize(200, 50);
        articleBtn.setPrefSize(200, 50);
        bookBtn.setPrefSize(200, 50);
        movieBtn.setPrefSize(200, 50);
        
        // Create a VBox and add the buttons to it
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(publisherBtn, authorBtn, journalBtn, articleBtn, bookBtn, movieBtn);
        
        // Create a scene with the VBox and set it as the scene for the window
        Scene scene = new Scene(vbox, 220, 350);
        primaryStage.setScene(scene);
        
        // Show the window
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}

