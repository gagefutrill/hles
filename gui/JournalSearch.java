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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JournalSearch extends Application {

    private Connection conn;
    private String user, pass, url;

    public JournalSearch(String url, String user, String pass) {
        this.user = user;
        this.pass = pass;
        this.url = url;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
    	//Set Window title, start maximized, and connect to DB
        primaryStage.setTitle("Search Journals");
        conn = DriverManager.getConnection(url, user, pass);
        
        // Create labels and text fields for searching
        Label titleLabel = new Label("Title:");
        TextField titleField = new TextField();

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

        //Create Table and its columns
        TableView<Journal> table = new TableView<>();
        table.setEditable(false);
        table.setPrefSize(400, 400);
        TableColumn<Journal, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Journal, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Journal, String> issueCol = new TableColumn<>("Issue");
        issueCol.setCellValueFactory(new PropertyValueFactory<>("issue"));
        TableColumn<Journal, String> volumeCol = new TableColumn<>("Volume");
        volumeCol.setCellValueFactory(new PropertyValueFactory<>("volume"));
        TableColumn<Journal, String> issnCol = new TableColumn<>("ISSN");
        issnCol.setCellValueFactory(new PropertyValueFactory<>("issn"));
        TableColumn<Journal, String> numArtCol = new TableColumn<>("Number of Articles");
        numArtCol.setCellValueFactory(new PropertyValueFactory<>("num_articles"));
        TableColumn<Journal, String> pubDateCol = new TableColumn<>("Publish Date");
        pubDateCol.setCellValueFactory(new PropertyValueFactory<>("pub_date"));
        TableColumn<Journal, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genreName"));
        TableColumn<Journal, String> langCol = new TableColumn<>("Language");
        langCol.setCellValueFactory(new PropertyValueFactory<>("langName"));
        table.getColumns().addAll(idCol,titleCol,issueCol,volumeCol,issnCol,numArtCol,pubDateCol,genreCol,langCol);
        
        // Create search button
        Button searchButton = new Button("Search");
        
     // Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(30);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        grid.add(titleLabel, 0, 0);
        grid.add(titleField, 1, 0);
        
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
        primaryStage.setMaximized(true);
        primaryStage.show();
        
        //Search Button Action
        searchButton.setOnAction(e -> {
            // Get search parameters
            String title = titleField.getText();
            String issue = issueField.getText();
            String volume = volumeField.getText();
            String issn = issnField.getText();
            String numArt = numArtField.getText();
            String pubDate = pubdateField.getText();
            String genre = pubdateField.getText();
            String language = pubdateField.getText();
            
      

            // Construct query
            String query = "SELECT journal.journal_id, journal.title, journal.issue, journal.volume, journal.issn, journal.num_articles, journal.pub_date, genre.name, language.name FROM hles.journal "
            		+ "INNER JOIN genre ON journal.genre_id = genre.genre_id "
            		+ "INNER JOIN language ON journal.language_id = language.language_id WHERE ";
                   query += "journal.title LIKE ? AND journal.issue LIKE ? AND ";
                   query += "journal.volume LIKE ? AND journal.issn LIKE ? AND ";
                   query += "journal.num_articles LIKE ? AND journal.pub_date LIKE ? AND ";
                   query += "genre.name LIKE ? AND language.name LIKE ? ";

            try {
                // Execute query
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "%" + title + "%");
                stmt.setString(2, "%" + issue + "%");
                stmt.setString(3, "%" + volume + "%");
                stmt.setString(4, "%" + issn + "%");
                stmt.setString(5, "%" + numArt + "%");
                stmt.setString(6, "%" + pubDate + "%");
                stmt.setString(7, "%" + genre + "%");
                stmt.setString(8, "%" + language + "%");
                
                ResultSet rs = stmt.executeQuery();
                
                ObservableList<Journal> journals = FXCollections.observableArrayList();
                // Display results in Table
                while (rs.next()) {
                	String id = rs.getString("journal.journal_id");
                    String titleResult = rs.getString("journal.title");
                    String issueResult = rs.getString("journal.issue");
                    String volumeResult = rs.getString("journal.volume");
                    String issnResult = rs.getString("journal.issn");
                    String numArtResult = rs.getString("journal.num_articles");
                    String pubdateResult = rs.getString("journal.pub_date");
                    String genreResult = rs.getString("genre.name");
                    String languageResult = rs.getString("language.name");
                    
                    journals.add(new Journal(id,titleResult,issueResult,volumeResult,issnResult,numArtResult,pubdateResult,genreResult,languageResult));
                }
                vbox.getChildren().addAll(table);
                table.setItems(journals);
            } catch (SQLException ex) {
                System.out.println("Error executing query: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        
    }
    public static class Journal {
    	private SimpleStringProperty id;
    	private SimpleStringProperty title;
    	private SimpleStringProperty issue;
    	private SimpleStringProperty volume;
    	private SimpleStringProperty issn;
    	private SimpleStringProperty num_articles;
    	private SimpleStringProperty pub_date;
    	private SimpleStringProperty genreName;
    	private SimpleStringProperty langName;
    	
    	public Journal (String id, String title, String issue, String volume, String issn, String num_articles, String pub_date, String genreName, String langName) {
    		this.id = new SimpleStringProperty(id);
    		this.title = new SimpleStringProperty(title);
    		this.issue = new SimpleStringProperty(issue);
    		this.volume = new SimpleStringProperty(volume);
    		this.issn = new SimpleStringProperty(issn);
    		this.num_articles = new SimpleStringProperty(num_articles);
    		this.pub_date = new SimpleStringProperty(pub_date);
    		this.genreName = new SimpleStringProperty(genreName);
    		this.langName = new SimpleStringProperty(langName);
    	}
		public String getId() {
			return id.get();
		}
		public String getTitle() {
			return title.get();
		}
		public String getIssue() {
			return issue.get();
		}
		public String getVolume() {
			return volume.get();
		}
		public String getIssn() {
			return issn.get();
		}
		public String getNum_articles() {
			return num_articles.get();
		}
		public String getPub_date() {
			return pub_date.get();
		}
		public String getGenreName() {
			return genreName.get();
		}
		public String getLangName() {
			return langName.get();
		}
    }
}