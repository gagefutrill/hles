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

public class ArticleSearch extends Application{

	private Connection conn;
	private String url,user,pass;
	
	public ArticleSearch(String url, String user, String pass) {
		this.url = url;
		this.user = user;
		this.pass = pass;
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("Search Articles");
		conn = DriverManager.getConnection(url,user,pass);
		
		// Create labels and text fields for searching
        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();
        Label doiLabel = new Label("doi:");
        TextField doiField = new TextField();
        
        Label authorLabel = new Label("Authors:");
        TextField authorField = new TextField();
        Label journalLabel = new Label("Journal: ");
        TextField journalField = new TextField();
        
        Label pubDateLabel = new Label("Publish Date:");
        TextField pubDateField = new TextField();
        
        Label genreLabel = new Label("Genre:");
        TextField genreField = new TextField();
        Label langLabel = new Label("Language:");
        TextField langField = new TextField();
		        
        //Create search and back buttons
        Button searchButton = new Button("Search");
        Button backButton = new Button("Back");
        
      //Create Table and Columns
        TableView<Article> table = new TableView<>();
        table.setEditable(false);
        table.setPrefSize(400, 400);
        TableColumn<Article,String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Article,String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(200);
        TableColumn<Article,String> doiCol = new TableColumn<>("DOI");
        doiCol.setCellValueFactory(new PropertyValueFactory<>("doi"));
        TableColumn<Article,String> authorsCol = new TableColumn<>("Authors");
        authorsCol.setCellValueFactory(new PropertyValueFactory<>("authors"));
        authorsCol.setPrefWidth(150);
        TableColumn<Article,String> journalCol = new TableColumn<>("Journal");
        journalCol.setCellValueFactory(new PropertyValueFactory<>("journal"));
        journalCol.setPrefWidth(150);
        TableColumn<Article,String> pubYearCol = new TableColumn<>("Publish Date");
        pubYearCol.setCellValueFactory(new PropertyValueFactory<>("publish_date"));
        TableColumn<Article, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genreName"));
        TableColumn<Article, String> langCol = new TableColumn<>("Language");
        langCol.setCellValueFactory(new PropertyValueFactory<>("langName"));
        TableColumn<Article,String> availCol = new TableColumn<>("Available");
        availCol.setCellValueFactory(new PropertyValueFactory<>("avail"));
        table.getColumns().addAll(idCol,titleCol,doiCol,authorsCol,journalCol,pubYearCol,genreCol,langCol,availCol);

        //Create text wrapping for large fields
        titleCol.setCellFactory(tc -> {
            TableCell<Article, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(titleCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        authorsCol.setCellFactory(tc -> {
            TableCell<Article, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(authorsCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        journalCol.setCellFactory(tc -> {
            TableCell<Article, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(journalCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        doiCol.setCellFactory(tc -> {
            TableCell<Article, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(doiCol.widthProperty());
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
        grid.add(doiLabel, 2, 0);
        grid.add(doiField, 3, 0);
        grid.add(journalLabel,0,1);
        grid.add(journalField, 1, 1);
        grid.add(authorLabel, 2, 1);
        grid.add(authorField, 3, 1);
        grid.add(genreLabel, 0, 2);
        grid.add(genreField, 1, 2);
        grid.add(langLabel, 2, 2);
        grid.add(langField, 3, 2);
        grid.add(pubDateLabel, 0, 3);
        grid.add(pubDateField, 1, 3);
        
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
        	String doi = doiField.getText();
        	String authors = authorField.getText();
        	String journal = journalField.getText();
        	String pubDate = pubDateField.getText();
        	String genre = genreField.getText();
        	String lang = langField.getText();
        	
        	//Construct query 
        	String query = "SELECT Article.article_id,Article.title,Article.doi, GROUP_CONCAT(Author.name ORDER BY Author.name DESC SEPARATOR ', ') AS 'authors',"
        			+ 	   "Journal.title,Article.publish_date,Genre.name,Language.name,Copy_of.available "
        			+ "FROM hles.Article "
        			+ "INNER Join Author_of ON Article.article_id = Author_of.article_id "
        			+ "INNER join Author ON Author_of.author_id = Author.author_id "
        			+ "INNER JOIN Genre ON Article.genre_id = Genre.genre_id "
            		+ "INNER JOIN Language ON Article.language_id = Language.language_id "
            		+ "INNER JOIN Article_in ON Article.article_id = Article_in.article_id "
            		+ "INNER JOIN Journal ON Article_in.journal_id = Journal.journal_id "
            		+ "INNER JOIN Copy_of ON Journal.journal_id = Copy_of.journal_id WHERE "
            		+ "Article.title LIKE ? AND Article.doi LIKE ? AND Author.name LIKE ? AND "
            		+ "Journal.title LIKE ?  AND Genre.name LIKE ? AND Language.name LIKE ? AND Article.publish_date LIKE ? "
        			+ "GROUP BY Article.article_id,Article.title,Article.doi,Journal.title,Article.publish_date,Genre.name,Language.name,Copy_of.available;";
        	
        	try { 
        		//Execute query
        		PreparedStatement stmt = conn.prepareStatement(query);
        		stmt.setString(1, "%"+title+"%");
        		stmt.setString(2, "%"+doi+"%");
        		stmt.setString(3, "%"+authors+"%");
        		stmt.setString(4, "%"+journal+"%");
        		stmt.setString(7, "%"+pubDate+"%");
        		stmt.setString(5, "%"+genre+"%");
        		stmt.setString(6, "%"+lang+"%");
        		ResultSet rs = stmt.executeQuery();
        		
        		ObservableList<Article> articles = FXCollections.observableArrayList();
        		String[] cols = {"Article.article_id","Article.title","Article.doi","authors",
        				"Journal.title","Article.publish_date","Genre.name","Language.name","Copy_of.available"};
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
            		articles.add(new Article(res[0],res[1],res[2],res[3],res[4],res[5],res[6],res[7],res[8]));
        		}
        		//Remove any existing table and add new Results table to window
        		vbox.getChildren().remove(table);
        		vbox.getChildren().addAll(table);
        		table.setItems(articles);
        	} catch (SQLException ex) {
        		System.out.println("Error executing query: " + ex.getMessage());
        		ex.printStackTrace();
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
	
	public static class Article {
		private SimpleIntegerProperty id;
		private SimpleStringProperty title, doi, authors, journal, publish_date, genreName, langName, avail;
    	
		public Article(String id, String title, String doi, String authors, String journal, String publish_date, String genreName, String langName, String available) {
			this.id = new SimpleIntegerProperty(Integer.parseInt(id));
			this.title = new SimpleStringProperty(title);
			this.doi = new SimpleStringProperty(doi);
			this.authors = new SimpleStringProperty(authors);
			this.journal = new SimpleStringProperty(journal);
			this.publish_date = new SimpleStringProperty(publish_date);
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
		public String getDoi() {
			return doi.get();
		}
		public String getAuthors() {
			return authors.get();
		}
		public String getJournal() {
			return journal.get();
		}
		public String getPublish_date() {
			return publish_date.get();
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
