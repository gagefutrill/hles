
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MovieSearch extends Application {

    private Connection conn;
    private String user, pass, url;
    
    public MovieSearch(String url,String user, String pass) {
        this.user = user;
        this.pass = pass;
        this.url = url;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Search Movies");
        conn = DriverManager.getConnection(url, user, pass);
        // Create labels and text fields for searching
        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();

        Label editionLabel = new Label("Edition:");
        TextField editionField = new TextField();
        
        Label directorLabel = new Label("Director:");
        TextField directorField = new TextField();
        
        Label isbnLabel = new Label("ISBN:");
        TextField isbnField = new TextField();
        
        Label pubLabel = new Label("Publisher:");
        TextField pubField = new TextField();
        
        Label pubYearLabel = new Label("Publish Year:");
        TextField pubYearField = new TextField();

        Label genreLabel = new Label("Genre:");
        TextField genreField = new TextField();
        
        Label languageLabel = new Label("Language:");
        TextField languageField = new TextField();

        // Create search button
        Button searchButton = new Button("Search");
        Button backButton = new Button("Back");
        
        //Create Table and Columns
        TableView<Movie> table = new TableView<>();
        table.setEditable(false);
        table.setPrefSize(400, 400);
        TableColumn<Movie,String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Movie,String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(150);
        TableColumn<Movie,String> editionCol = new TableColumn<>("Edition");
        editionCol.setCellValueFactory(new PropertyValueFactory<>("edition"));
        TableColumn<Movie,String> directorCol = new TableColumn<>("Director");
        directorCol.setCellValueFactory(new PropertyValueFactory<>("director"));
        directorCol.setPrefWidth(150);
        TableColumn<Movie,String> isbnCol = new TableColumn<>("ISBN");
        isbnCol.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        TableColumn<Movie,String> pubCol = new TableColumn<>("Publisher");
        pubCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        pubCol.setPrefWidth(150);
        TableColumn<Movie,String> pubDateCol = new TableColumn<>("Publish Date");
        pubDateCol.setCellValueFactory(new PropertyValueFactory<>("publish_date"));
        TableColumn<Movie,String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genreName"));
        TableColumn<Movie,String> langCol = new TableColumn<>("Language");
        langCol.setCellValueFactory(new PropertyValueFactory<>("langName"));
        TableColumn<Movie,String> availCol = new TableColumn<>("Available");
        availCol.setCellValueFactory(new PropertyValueFactory<>("avail"));
        table.getColumns().addAll(idCol,titleCol,editionCol,directorCol,isbnCol,pubCol,pubDateCol,genreCol,langCol,availCol);
        
      //Create text wrapping for large fields
        titleCol.setCellFactory(tc -> {
            TableCell<Movie, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(titleCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        editionCol.setCellFactory(tc -> {
            TableCell<Movie, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(editionCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        directorCol.setCellFactory(tc -> {
            TableCell<Movie, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(directorCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        pubCol.setCellFactory(tc -> {
            TableCell<Movie, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(pubCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        genreCol.setCellFactory(tc -> {
            TableCell<Movie, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(genreCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        // Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(30);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        grid.add(titleLabel, 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(isbnLabel, 2, 0);
        grid.add(isbnField, 3, 0);
        grid.add(directorLabel, 0, 1);
        grid.add(directorField, 1, 1);
        grid.add(editionLabel, 2, 1);
        grid.add(editionField, 3, 1);
        grid.add(pubLabel, 0, 3);
        grid.add(pubField, 1, 3);
        grid.add(pubYearLabel, 2, 3);
        grid.add(pubYearField, 3, 3);
        grid.add(genreLabel, 0, 2);
        grid.add(genreField, 1, 2);
        grid.add(languageLabel, 2, 2);
        grid.add(languageField, 3, 2);
        

        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(searchButton,backButton);

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.TOP_LEFT);
        vbox.getChildren().addAll(grid, hbox);

        // Set scene and show window
        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);
        
        //Search Action
        searchButton.setOnAction(e -> {
            // Get search parameters
            String title = titleField.getText();
            String edition = editionField.getText();
            String director = directorField.getText();
            String isbn = isbnField.getText();
            String pub = pubField.getText();
            String pubYear = pubYearField.getText();
            String genre = genreField.getText();
            String language = languageField.getText();
          
            // Construct query
            String query = "SELECT Movie.movie_id,Movie.title,Movie.edition,Movie.director,Movie.isbn,Publisher.name,Movie.publish_date,Genre.name,Language.name,Copy_of.available "
            		+ "FROM hles.Movie "
            		+ "INNER JOIN Genre ON Movie.genre_id = Genre.genre_id "
            		+ "INNER JOIN Language ON Movie.language_id = Language.language_id "
            		+ "INNER JOIN Publisher_of ON Movie.movie_id = Publisher_of.movie_id "
            		+ "INNER JOIN Publisher ON Publisher_of.publisher_id = Publisher.publisher_id "
            		+ "INNER JOIN Copy_of ON Movie.movie_id = Copy_of.movie_id WHERE ";
                   query += "Movie.title LIKE ? AND Movie.edition LIKE ? AND ";
                   query += "Movie.director LIKE ? AND Movie.isbn LIKE ? AND Publisher.name LIKE ? AND ";
                   query += "Movie.publish_date LIKE ? AND Genre.name LIKE ? AND Language.name LIKE ?";
                   
                  
            try {
                // Execute query
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "%" + title + "%");
                stmt.setString(2, "%" + edition + "%");
                stmt.setString(3, "%" + director + "%");
                stmt.setString(4, "%" + isbn + "%");
                stmt.setString(5, "%" + pub + "%");
                stmt.setString(6, "%" + pubYear + "%");
                stmt.setString(7, "%" + genre + "%");
                stmt.setString(8, "%" + language + "%");
                
                ResultSet rs = stmt.executeQuery();

                ObservableList<Movie> movies = FXCollections.observableArrayList();
                // Display results in console
                while (rs.next()) {
                    String id = rs.getString("Movie.movie_id");
                    String titleResult = rs.getString("Movie.title");
                    String editionResult = rs.getString("Movie.edition");
                    String directorResult = rs.getString("Movie.director");
                    String isbnResult = rs.getString("Movie.isbn");
                    String pubResult = rs.getString("Publisher.name");
                    String pubYearResult = rs.getString("Movie.publish_date");
                    String genreResult = rs.getString("Genre.name");
                    String languageResult = rs.getString("Language.name");
                    String availResult = rs.getString("Copy_of.available");
              
                    movies.add(new Movie(id,titleResult,editionResult,directorResult,isbnResult,pubResult,pubYearResult,genreResult,languageResult,availResult));
                }
                //Remove any existing table and add new Results table to window
                vbox.getChildren().remove(table);
                vbox.getChildren().addAll(table);
                table.setItems(movies);
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
    public static class Movie {
		private SimpleIntegerProperty id;
		private SimpleStringProperty title, edition, director, isbn, publisher, publish_date, genreName, langName, avail;
    	
    	public Movie (String id, String title, String edition, String director, String isbn, String publisher, String pub_date, String genreName, String langName, String avail) {
    		this.id = new SimpleIntegerProperty(Integer.parseInt(id));
    		this.title = new SimpleStringProperty(title);
    		this.edition = new SimpleStringProperty(edition);
    		this.director = new SimpleStringProperty(director);
    		this.isbn = new SimpleStringProperty(isbn);
    		this.publisher = new SimpleStringProperty(publisher);
    		this.publish_date = new SimpleStringProperty(pub_date);
    		this.genreName = new SimpleStringProperty(genreName);
    		this.langName = new SimpleStringProperty(langName);
    		if(avail.equals("1"))
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
		public String getDirector() {
			return director.get();
		}
		public String getIsbn() {
			return isbn.get();
		}
		public String getPublisher() {
			return publisher.get();
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