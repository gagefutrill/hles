import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SearchMenu extends Application {
	private Connection conn;
	private String user, pass, url;
	
    public SearchMenu(String url, String user, String pass) {
        this.user = user;
        this.pass = pass;
        this.url = url;
    }
    
    @Override
    public void start(Stage primaryStage) throws SQLException {
        // Set the title of the window and connect
        primaryStage.setTitle("Search Menu");
        conn = DriverManager.getConnection(url, user, pass);
        
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
        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);
        
        // Show the maximized window
        primaryStage.show();
        //primaryStage.setMaximized(true);

        publisherBtn.setOnAction(event -> {
            PublisherSearch pubSearch = new PublisherSearch(url,user,pass);
            try {
				pubSearch.start(primaryStage);
			} catch (Exception e) {
				e.printStackTrace();
			}
        });
        journalBtn.setOnAction(event -> {
            JournalSearch journalSearch = new JournalSearch(url,user,pass);
            try {
				journalSearch.start(primaryStage);
			} catch (Exception e) {
				e.printStackTrace();
			}
            
        });
        movieBtn.setOnAction(event -> {
            MovieSearch MovieSearch = new MovieSearch(url,user,pass);
            try {
				MovieSearch.start(primaryStage);
			} catch (Exception e) {
				e.printStackTrace();
			}
        });
        bookBtn.setOnAction(event -> {
        	BookSearch bookSearch = new BookSearch(url,user,pass);
        	try {
				bookSearch.start(primaryStage);
			} catch (Exception e) {
				e.printStackTrace();
			}
        });
        authorBtn.setOnAction(event -> {
        	AuthorSearch authorSearch = new AuthorSearch(url,user,pass);
        	try {
				authorSearch.start(primaryStage);
			} catch (Exception e) {
				e.printStackTrace();
			}
        });
        articleBtn.setOnAction(click -> {
        	ArticleSearch articleSearch = new ArticleSearch(url,user,pass);
        	try {
				articleSearch.start(primaryStage);
			} catch (Exception e) {
				e.printStackTrace();
			}
        });
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
