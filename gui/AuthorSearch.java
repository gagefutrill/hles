import java.sql.Connection;
import java.sql.DriverManager;

import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthorSearch extends Application{

	private Connection conn;
    private String user, pass, url;
    
    public AuthorSearch(String url,String user, String pass) {
        this.user = user;
        this.pass = pass;
        this.url = url;
    }
	@Override
	public void start(Stage primaryStage) throws Exception {
		// Set Window Title and create connection
        primaryStage.setTitle("Search Authors");
        conn = DriverManager.getConnection(url, user, pass);
        
        // Create labels and text fields for searching
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();

        Label genderLabel = new Label("Gender:");
        TextField genderField = new TextField();
        
        Label raceLabel = new Label("Race: ");
        TextField raceField = new TextField();
        
        Label birthYearLabel = new Label("Birth Year:");
        TextField birthYearField = new TextField();
        
        
	}

}
