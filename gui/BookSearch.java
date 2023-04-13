import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BookSearch extends Application {
	private Connection conn;
	private String user, pass, url;
	
	public BookSearch(String url, String user, String pass) {
		this.url = url;
		this.pass = pass;
		this.user = user;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Search Books");
		conn = DriverManager.getConnection(url,user,pass);
		
		// Create labels and text fields for searching
        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();

        Label editionLabel = new Label("Edition:");
        TextField editionField = new TextField();
        
        Label isbnLabel = new Label("ISBN:");
        TextField isbnField = new TextField();
        
        Label authorLabel = new Label("Authors:");
        TextField authorField = new TextField();
        
        Label pubLabel = new Label("Publsher: ");
        TextField pubField = new TextField();
        
        Label pubYearLabel = new Label("Publish Date:");
        TextField pubYearField = new TextField();
        
        Label genreLabel = new Label("Genre:");
        TextField genreField = new TextField();
        
        Label langLabel = new Label("Language:");
        TextField langField = new TextField();
		
        //Create Table and Columns
        TableView<Book> table = new TableView<>();
        table.setEditable(false);
        table.setPrefSize(400, 400);
        TableColumn<Book,String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Book,String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Book,String> editionCol = new TableColumn<>("Edition");
        editionCol.setCellValueFactory(new PropertyValueFactory<>("edition"));
        TableColumn<Book,String> ISBNCol = new TableColumn<>("ISBN");
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        TableColumn<Book,String> authorsCol = new TableColumn<>("Authors");
        authorsCol.setCellValueFactory(new PropertyValueFactory<>("authors"));
        TableColumn<Book,String> pubCol = new TableColumn<>("Publisher");
        pubCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        TableColumn<Book,String> pubYearCol = new TableColumn<>("Publish Year");
        pubYearCol.setCellValueFactory(new PropertyValueFactory<>("publish_year"));
        TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genreName"));
        TableColumn<Book, String> langCol = new TableColumn<>("Language");
        langCol.setCellValueFactory(new PropertyValueFactory<>("langName"));
        table.getColumns().addAll(idCol,titleCol,editionCol,ISBNCol,authorsCol,pubCol,pubYearCol,genreCol,langCol);
        
        //Create search button
        Button searchButton = new Button("Search");
        
        //Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(30);
        grid.setPadding(new Insets(25,25,25,25));
        
        grid.add(titleLabel, 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(editionLabel, 2, 0);
        grid.add(editionField, 3, 0);
        
        grid.add(isbnLabel, 0, 1);
        grid.add(isbnField, 1, 1);
        grid.add(authorLabel, 2, 1);
        grid.add(authorField, 3, 1);
        
        grid.add(pubLabel,0,2);
        grid.add(pubField, 1, 2);
        grid.add(pubYearLabel, 2, 2);
        grid.add(pubYearField, 3, 2);
        
        grid.add(genreLabel, 0, 3);
        grid.add(genreField, 1, 3);
        grid.add(langLabel, 2, 3);
        grid.add(langField, 3, 3);
        
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(searchButton);
        
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().addAll(grid,hbox);
        
        //set scene and show window
        Scene scene = new Scene(vbox, 800, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //Search Button Action
        searchButton.setOnAction(e -> {
        	//Get search parameters
        	String title = titleField.getText();
        	String edition = editionField.getText();
        	String isbn = isbnField.getText();
        	String author = authorField.getText();
        	String pub = pubField.getText();
        	String pubYear = pubYearField.getText();
        	String genre = genreField.getText();
        	String lang = langField.getText();
        	
        	//Construct query 
        	String query = "SELECT book.book_id,book.title,book.edition,book.isbn, GROUP_CONCAT(author.name ORDER BY author.name DESC SEPARATOR ', '),"
        			+ 	   "publisher.name,book.publish_year,genre.name,language.name "
        			+ "FROM book "
        			+ "INNER Join author_of ON book.book_id = author_of.book_id "
        			+ "INNER join author ON author_of.author_id = author.author_id "
        			+ "INNER JOIN genre ON book.genre_id = genre.genre_id "
            		+ "INNER JOIN language ON book.language_id = language.language_id "
            		+ "INNER JOIN publisher_of ON book.book_id = publisher_of.book_id "
            		+ "INNER JOIN publisher ON publisher_of.publisher_id = publisher.publisher_id WHERE "
            		+ "book.title LIKE ? AND book.edition LIKE ? AND book.isbn LIKE ? AND author.name LIKE ? AND "
            		+ "publisher.name LIKE ? AND book.publish_year LIKE ? AND genre.name LIKE ? AND language.name LIKE ? "
        			+ "Group by book.book_id,book.title,book.edition,book.isbn,publisher.name,book.publish_year,genre.name,language.name ; ";
        	
        	try { 
        		//Execute query
        		PreparedStatement stmt = conn.prepareStatement(query);
        		stmt.setString(1, "%"+title+"%");
        		stmt.setString(2, "%"+edition+"%");
        		stmt.setString(3, "%"+isbn+"%");
        		stmt.setString(4, "%"+author+"%");
        		stmt.setString(5, "%"+pub+"%");
        		stmt.setString(6, "%"+pubYear+"%");
        		stmt.setString(7, "%"+genre+"%");
        		stmt.setString(8, "%"+lang+"%");
        		ResultSet rs = stmt.executeQuery();
        		ObservableList<Book> books = FXCollections.observableArrayList();
        		String[] cols = {"book.book_id","book.title","book.edition","book.isbn",
        				"GROUP_CONCAT(author.name ORDER BY author.name DESC SEPARATOR ', ')",
        				"publisher.name","book.publish_year","genre.name","language.name"};
        		String[] res = new String[9];
        		//Display results in Table
        		while(rs.next()) {
        			for(int i = 0; i < 9; i++){
        		        if(rs.getObject(i+1)!=null) {
        		        	res[i] = rs.getString(cols[i]);
        		        }
        		        else {
        		        	res[i] = " ";
        		        }
        		    }
            		
            		//System.out.println("ID: "+id+" Title: " +titleRes+" Edition: " +editionRes);
            		books.add(new Book(res[0],res[1],res[2],res[3],res[4],res[5],res[6],res[7],res[8]));
        		}
        		//Remove any existing table and add new Results table to window
        		vbox.getChildren().remove(table);
        		vbox.getChildren().addAll(table);
        		table.setItems(books);
        	} catch (SQLException ex) {
        		System.out.println("Error executing query: " + ex.getMessage());
        	}
        });
        
	}
	
	public static class Book {
		private SimpleStringProperty id;
		private SimpleStringProperty title;
    	private SimpleStringProperty edition;
    	private SimpleStringProperty isbn;
    	private SimpleStringProperty authors;
    	private SimpleStringProperty publisher;
    	private SimpleStringProperty publish_year;
    	private SimpleStringProperty genreName;
    	private SimpleStringProperty langName;
    	
		public Book(String id, String title, String edition, String isbn, String authors, String publisherName, String publish_year, String genreName, String langName) {
			this.id = new SimpleStringProperty(id);
			this.title = new SimpleStringProperty(title);
			this.edition = new SimpleStringProperty(edition);
			this.isbn = new SimpleStringProperty(isbn);
			this.authors = new SimpleStringProperty(authors);
			this.publisher = new SimpleStringProperty(publisherName);
			this.publish_year = new SimpleStringProperty(publish_year);
			this.genreName = new SimpleStringProperty(genreName);
			this.langName = new SimpleStringProperty(langName);
		}
		public String getId() {
			return id.get();
		}
		public String getTitle() {
			return title.get();
		}
		public String getEdition() {
			return edition.get();
		}
		public String getIsbn() {
			return isbn.get();
		}
		public String getAuthors() {
			return authors.get();
		}
		public String getPublisher() {
			return publisher.get();
		}
		public String getPublish_year() {
			return publish_year.get();
		}
		public String getGenreName() {
			return genreName.get();
		}
		public String getLangName() {
			return langName.get();
		}
	}
}