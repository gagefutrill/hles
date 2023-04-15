import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
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
		        
        //Create search and back buttons
        Button searchButton = new Button("Search");
        Button backButton = new Button("Back");
        
      //Create Table and Columns
        TableView<Book> table = new TableView<>();
        table.setEditable(false);
        table.setPrefSize(400, 400);
        TableColumn<Book,String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Book,String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);
        TableColumn<Book,String> editionCol = new TableColumn<>("Edition");
        editionCol.setCellValueFactory(new PropertyValueFactory<>("edition"));
        TableColumn<Book,String> ISBNCol = new TableColumn<>("ISBN");
        ISBNCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        TableColumn<Book,String> authorsCol = new TableColumn<>("Authors");
        authorsCol.setCellValueFactory(new PropertyValueFactory<>("authors"));
        authorsCol.setPrefWidth(150);
        TableColumn<Book,String> pubCol = new TableColumn<>("Publisher");
        pubCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        pubCol.setPrefWidth(150);
        TableColumn<Book,String> pubYearCol = new TableColumn<>("Publish Year");
        pubYearCol.setCellValueFactory(new PropertyValueFactory<>("publish_year"));
        TableColumn<Book, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genreName"));
        TableColumn<Book, String> langCol = new TableColumn<>("Language");
        langCol.setCellValueFactory(new PropertyValueFactory<>("langName"));
        TableColumn<Book,String> availCol = new TableColumn<>("Available");
        availCol.setCellValueFactory(new PropertyValueFactory<>("avail"));
        table.getColumns().addAll(idCol,titleCol,editionCol,ISBNCol,authorsCol,pubCol,pubYearCol,genreCol,langCol,availCol);
        
        //Create text wrapping for large fields
        titleCol.setCellFactory(tc -> {
            TableCell<Book, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(titleCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        authorsCol.setCellFactory(tc -> {
            TableCell<Book, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(authorsCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        pubCol.setCellFactory(tc -> {
            TableCell<Book, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(pubCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        genreCol.setCellFactory(tc -> {
            TableCell<Book, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(genreCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        
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
        hbox.getChildren().addAll(searchButton,backButton);
        
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().addAll(grid,hbox);
        
        //set scene and show window
        Scene scene = new Scene(vbox, 800, 400);
        primaryStage.setMaximized(true);
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
        	String query = "SELECT Book.book_id,Book.title,Book.edition,Book.isbn, GROUP_CONCAT(Author.name ORDER BY Author.name DESC SEPARATOR ', '),"
        			+ 	   "Publisher.name,Book.publish_year,Genre.name,Language.name,Copy_of.available "
        			+ "FROM hles.Book "
        			+ "INNER Join Author_of ON Book.book_id = Author_of.book_id "
        			+ "INNER join Author ON Author_of.author_id = Author.author_id "
        			+ "INNER JOIN Genre ON Book.genre_id = Genre.genre_id "
            		+ "INNER JOIN Language ON Book.language_id = Language.Language_id "
            		+ "INNER JOIN Publisher_of ON Book.book_id = Publisher_of.book_id "
            		+ "INNER JOIN Publisher ON Publisher_of.publisher_id = Publisher.publisher_id "
            		+ "INNER JOIN Copy_of ON Book.book_id = Copy_of.book_id WHERE "
            		+ "Book.title LIKE ? AND Book.edition LIKE ? AND Book.isbn LIKE ? AND Author.name LIKE ? AND "
            		+ "Publisher.name LIKE ? AND Book.publish_year LIKE ? AND Genre.name LIKE ? AND Language.name LIKE ? "
        			+ "Group by Book.book_id,Book.title,Book.edition,Book.isbn,Publisher.name,Book.publish_year,Genre.name,Language.name,Copy_of.available ; ";
        	
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
        				"publisher.name","book.publish_year","genre.name","language.name","copy_of.available"};
        		String[] res = new String[10];
        		//Display results in Table
        		while(rs.next()) {
        			for(int i = 0; i < 10; i++){
        		        if(rs.getObject(i+1)!=null) {
        		        	res[i] = rs.getString(cols[i]);
        		        }
        		        else {
        		        	res[i] = " ";
        		        }
        		    }      			
            		books.add(new Book(res[0],res[1],res[2],res[3],res[4],res[5],res[6],res[7],res[8],res[9]));
        		}
        		//Remove any existing table and add new Results table to window
        		vbox.getChildren().remove(table);
        		vbox.getChildren().addAll(table);
        		table.setItems(books);
        	} catch (SQLException ex) {
        		System.out.println("Error executing query: " + ex.getMessage());
        	}
        });
        backButton.setOnAction(click -> {
        	SearchMenu searchMenu = new SearchMenu(url,user,pass);
        	try {
				searchMenu.start(primaryStage);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
        });
	}
	
	public static class Book {
		private SimpleIntegerProperty id;
		private SimpleStringProperty title, edition, isbn, authors, publisher, publish_year, genreName, langName, avail;
    	
		public Book(String id, String title, String edition, String isbn, String authors, String publisherName, String publish_year, String genreName, String langName, String available) {
			this.id = new SimpleIntegerProperty(Integer.parseInt(id));
			this.title = new SimpleStringProperty(title);
			this.edition = new SimpleStringProperty(edition);
			this.isbn = new SimpleStringProperty(isbn);
			this.authors = new SimpleStringProperty(authors);
			this.publisher = new SimpleStringProperty(publisherName);
			this.publish_year = new SimpleStringProperty(publish_year);
			this.genreName = new SimpleStringProperty(genreName);
			this.langName = new SimpleStringProperty(langName);
			if(available.equals("1"))
				this.avail = new SimpleStringProperty("Yes");
			else 
				this.avail = new SimpleStringProperty("No");
		}
		public int getId() {
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
		public String getAvail() {
			return avail.get();
		}
	}
	
}
