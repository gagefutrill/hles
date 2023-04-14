import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
        Label birthYearLabel = new Label("Birth Year Range:");
        TextField birthYearMinField = new TextField();
        Label birthDash = new Label(" - ");
        TextField birthYearMaxField = new TextField();
        
        // Create search and back buttons
        Button searchButton = new Button("Search");
        Button backButton = new Button("Back");
        
        //Create table for output       
        TableView<Author> table = new TableView<>();
        table.setEditable(false);
        table.setPrefSize(400, 400);
        TableColumn<Author,String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Author,String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(150);
        TableColumn<Author,String> raceCol = new TableColumn<>("Race");
        raceCol.setCellValueFactory(new PropertyValueFactory<>("race"));
        TableColumn<Author,String> genderCol = new TableColumn<>("Gender");
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        TableColumn<Author,String> birthYearCol = new TableColumn<>("Birth Year");
        birthYearCol.setCellValueFactory(new PropertyValueFactory<>("birthYear"));
        TableColumn<Author,String> booksCol = new TableColumn<>("Books");
        booksCol.setCellValueFactory(new PropertyValueFactory<>("books"));
        booksCol.setPrefWidth(200);
        TableColumn<Author,String> journalsCol = new TableColumn<>("Journals");
        journalsCol.setCellValueFactory(new PropertyValueFactory<>("journals"));
        journalsCol.setPrefWidth(200);
        table.getColumns().addAll(idCol,nameCol,raceCol,genderCol,birthYearCol,booksCol,journalsCol);
        
        //Create text wrapping for large fields
        nameCol.setCellFactory(tc -> {
            TableCell<Author, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(nameCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        journalsCol.setCellFactory(tc -> {
            TableCell<Author, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(journalsCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        booksCol.setCellFactory(tc -> {
            TableCell<Author, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(booksCol.widthProperty());
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
        grid.add(genderLabel, 2, 0);
        grid.add(genderField, 3, 0);
        grid.add(raceLabel, 4, 0);
        grid.add(raceField, 5, 0);
        grid.add(birthYearLabel,0,1);
        grid.add(birthYearMinField, 1, 1);
        grid.add(birthDash, 2, 1);
        grid.add(birthYearMaxField,3,1);
        
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.TOP_CENTER);
        hbox.getChildren().addAll(searchButton,backButton);

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().addAll(grid, hbox);
        
        //Set Scene and Show Window
        Scene scene = new Scene(vbox,1000,600);
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
        
        //Search Button Action
        searchButton.setOnAction(click -> {
        	String name = nameField.getText();
        	String gender = genderField.getText();
        	String race = raceField.getText();
        	String birthYearMin = birthYearMinField.getText();
        	String birthYearMax = birthYearMaxField.getText();
        	
        	//Construct query
        	String query = "SELECT author.author_id, author.name, author.gender, author.race, author.birth_year, GROUP_CONCAT(book.title ORDER BY book.title DESC SEPARATOR ', ') AS 'books', "
        			+ " GROUP_CONCAT(article.title ORDER BY article.title DESC SEPARATOR ', ') AS 'journals' "
        			+ "from author "
        			+ "LEFT JOIN author_of ON author.author_id = author_of.author_id "
        			+ "LEFT JOIN book ON author_of.book_id = book.book_id "
        			+ "LEFT JOIN article ON author_of.article_id = article.article_id WHERE "
        			+ "author.name LIKE ? AND author.gender LIKE ? AND author.race LIKE ? AND author.birth_year BETWEEN ? AND ? "
        			+ "GROUP BY author.author_id, author.name, author.gender, author.race, author.birth_Year; ";
        	
        	try {
        		//Execute Query
				PreparedStatement stmt = conn.prepareStatement(query);
				stmt.setString(1, "%"+name+"%");
				stmt.setString(2, "%"+gender+"%");
				stmt.setString(3, "%"+race+"%");
				if(birthYearMin.isBlank() || birthYearMax.isBlank()) {
					stmt.setString(4, "-2000");
					stmt.setString(5, Integer.toString(Year.now().getValue()));
				}else {
					stmt.setString(4, ""+birthYearMin+"");
					stmt.setString(5, ""+birthYearMax+"");
				}
				
				ResultSet rs = stmt.executeQuery();
				
				//Create List to store entries in 
				ObservableList<Author> authors = FXCollections.observableArrayList();
                //Add results to table
				String[] cols = {"author.author_id","author.name","author.gender","author.race",
								"author.birth_year","books","journals"};
        		String[] res = new String[7];
        		//Display results in Table
        		while(rs.next()) {
        			for(int i = 0; i < 7; i++){
        		        if(rs.getObject(i+1)!=null) {
        		        	res[i] = rs.getString(cols[i]);
        		        }
        		        else {
        		        	res[i] = " ";
        		        }
        		    }      			
            		authors.add(new Author(res[0],res[1],res[2],res[3],res[4],res[5],res[6]));
        		}
        		vbox.getChildren().remove(table);
        		vbox.getChildren().addAll(table);
        		table.setItems(authors);
			} catch (SQLException e) {
                System.out.println("Error executing query: " + e.getMessage());
				e.printStackTrace();
			}
        });
        
        //Action to go back to Search Menu
        backButton.setOnAction(click -> {
        	SearchMenu searchMenu = new SearchMenu(url,user,pass);
        	try {
				searchMenu.start(primaryStage);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
        });
	}
	public static class Author {
		private SimpleIntegerProperty id;
		private SimpleStringProperty name, gender, race, birthYear, books, journals;

		public Author(String id, String name, String gender, String race, String birthYear, String books, String journals) {
			this.id = new SimpleIntegerProperty(Integer.parseInt(id));
			this.name = new SimpleStringProperty(name);
			this.gender = new SimpleStringProperty(gender);
			this.race = new SimpleStringProperty(race);
			this.birthYear = new SimpleStringProperty(birthYear);
			this.books = new SimpleStringProperty(books);
			this.journals = new SimpleStringProperty(journals);
		}
		public int getId() {
			return id.get();
		}
		public String getName() {
			return name.get();
		}
		public String getGender() {
			return gender.get();
		}
		public String getRace() {
			return race.get();
		}
		public String getBirthYear() {
			return birthYear.get();
		}
		public String getBooks() {
			return books.get();
		}
		public String getJournals() {
			return journals.get();
		}
	}
}
