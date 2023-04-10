
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginApp extends Application {
    
    private static String DB_URL = "jdbc:mysql://35.231.200.99/hles";
    private static String USER = "username";
    private static String PASS = "password";
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library Database Login");
        
        // Create the login form
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Text sceneTitle = new Text("Welcome to the Library Database");
        sceneTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        grid.add(sceneTitle, 0, 0, 2, 1);

        Label userName = new Label("Username:");
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label password = new Label("Password:");
        grid.add(password, 0, 2);

        PasswordField passwordField = new PasswordField();
        grid.add(passwordField, 1, 2);

        Button btn = new Button("Sign in");
        grid.add(btn, 1, 4);

        Text message = new Text();
        grid.add(message, 1, 6);
        
        // Event handler for the login button
        btn.setOnAction(event -> {
            USER = userTextField.getText();
            PASS = passwordField.getText();

            // Authenticate the user against the database
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)){
                message.setText("Your login was successfull");
               
                primaryStage.close();
                SearchMenu searchMenu = new SearchMenu(conn);

                // Call the start() method on the SearchMenu instance
                searchMenu.start(primaryStage);
            } catch (SQLException e) {
            	message.setText("Invalid username or password");
                e.printStackTrace();
            }
        });
        
        Scene scene = new Scene(grid, 400, 275);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}