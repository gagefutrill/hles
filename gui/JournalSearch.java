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
        
        Label pubLabel = new Label("Publsher: ");
        TextField pubField = new TextField();
        
        Label pubdateLabel = new Label("Publish Date:");
        TextField pubdateField = new TextField();
        
        Label genreLabel = new Label("Genre:");
        TextField genreField = new TextField();
        
        Label langLabel = new Label("Language:");
        TextField langField = new TextField();

        //Create Table and its columns
        TableView<Journal> table = new TableView<>();
        table.setEditable(false);
        table.setPrefSize(400, 400);
        TableColumn<Journal, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Journal, String> titleCol = new TableColumn<>("Title");
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        titleCol.setPrefWidth(150);
        TableColumn<Journal, String> issueCol = new TableColumn<>("Issue");
        issueCol.setCellValueFactory(new PropertyValueFactory<>("issue"));
        TableColumn<Journal, String> volumeCol = new TableColumn<>("Volume");
        volumeCol.setCellValueFactory(new PropertyValueFactory<>("volume"));
        TableColumn<Journal, String> issnCol = new TableColumn<>("ISSN");
        issnCol.setCellValueFactory(new PropertyValueFactory<>("issn"));
        TableColumn<Journal, String> numArtCol = new TableColumn<>("Number of Articles");
        numArtCol.setCellValueFactory(new PropertyValueFactory<>("num_articles"));
        TableColumn<Journal, String> pubCol = new TableColumn<>("Publisher");
        pubCol.setCellValueFactory(new PropertyValueFactory<>("publisher"));
        pubCol.setPrefWidth(150);
        TableColumn<Journal, String> pubDateCol = new TableColumn<>("Publish Date");
        pubDateCol.setCellValueFactory(new PropertyValueFactory<>("pub_date"));
        TableColumn<Journal, String> genreCol = new TableColumn<>("Genre");
        genreCol.setCellValueFactory(new PropertyValueFactory<>("genreName"));
        TableColumn<Journal, String> langCol = new TableColumn<>("Language");
        langCol.setCellValueFactory(new PropertyValueFactory<>("langName"));
        TableColumn<Journal, String> availCol = new TableColumn<>("Available");
        availCol.setCellValueFactory(new PropertyValueFactory<>("avail"));
        table.getColumns().addAll(idCol,titleCol,issueCol,volumeCol,issnCol,numArtCol,pubCol,pubDateCol,genreCol,langCol,availCol);
        
        //Set text wrapping for larger fields
        titleCol.setCellFactory(tc -> {
            TableCell<Journal, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(titleCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        pubCol.setCellFactory(tc -> {
            TableCell<Journal, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(pubCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        
        // Create search and back buttons
        Button searchButton = new Button("Search");
        Button backButton = new Button("Back");
        
        // Create layout
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(30);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.add(titleLabel, 0, 0);
        grid.add(titleField, 1, 0);
        grid.add(issnLabel, 2, 0);
        grid.add(issnField, 3, 0);
        grid.add(volumeLabel, 0, 1);
        grid.add(volumeField, 1, 1);
        grid.add(issueLabel, 2, 1);
        grid.add(issueField, 3, 1);
        grid.add(genreLabel, 0, 2);
        grid.add(genreField, 1, 2);
        grid.add(langLabel, 2, 2);
        grid.add(langField, 3, 2);
        grid.add(pubLabel, 0, 3);
        grid.add(pubField, 1, 3);
        grid.add(pubdateLabel, 2, 3);
        grid.add(pubdateField, 3, 3);
        
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(searchButton,backButton);

        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.TOP_CENTER);
        vbox.getChildren().addAll(grid, hbox);

        // Set scene and show window
        Scene scene = new Scene(vbox, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
        //primaryStage.setMaximized(true);

        //Search Button Action
        searchButton.setOnAction(e -> {
            // Get search parameters
            String title = titleField.getText();
            String issue = issueField.getText();
            String volume = volumeField.getText();
            String issn = issnField.getText();
            String publisher = pubField.getText();
            String pubDate = pubdateField.getText();
            String genre = genreField.getText();
            String language = langField.getText();
            
      

            // Construct query
            String query = "SELECT journal.journal_id, journal.title, journal.issue, journal.volume, journal.issn, "
            		+ "journal.num_articles, publisher.name, journal.pub_date, genre.name, language.name, copy_of.available "
            		+ "FROM hles.journal "
            		+ "INNER JOIN genre ON journal.genre_id = genre.genre_id "
            		+ "INNER JOIN language ON journal.language_id = language.language_id "
            		+ "INNER JOIN publisher_of ON journal.journal_id = publisher_of.journal_id "
            		+ "INNER JOIN publisher ON publisher_of.publisher_id = publisher.publisher_id "
            		+ "INNER JOIN copy_of ON journal.journal_id = copy_of.journal_id WHERE ";
                   query += "journal.title LIKE ? AND journal.issue LIKE ? AND ";
                   query += "journal.volume LIKE ? AND journal.issn LIKE ? AND ";
                   query += "publisher.name LIKE ? AND journal.pub_date LIKE ? AND ";
                   query += "genre.name LIKE ? AND language.name LIKE ? ";

            try {
                // Execute query
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, "%" + title + "%");
                stmt.setString(2, "%" + issue + "%");
                stmt.setString(3, "%" + volume + "%");
                stmt.setString(4, "%" + issn + "%");
                stmt.setString(5, "%" + publisher + "%");
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
                    String pubResult = rs.getString("publisher.name");
                    String pubdateResult = rs.getString("journal.pub_date");
                    String genreResult = rs.getString("genre.name");
                    String languageResult = rs.getString("language.name");
                    String availResult = rs.getString("copy_of.available");
                    
                    journals.add(new Journal(id,titleResult,issueResult,volumeResult,issnResult,numArtResult,pubResult,pubdateResult,genreResult,languageResult,availResult));
                }
                //Remove any existing table and add new Results table to window
        		vbox.getChildren().remove(table);
                vbox.getChildren().addAll(table);
                table.setItems(journals);
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
    public static class Journal {
		private SimpleIntegerProperty id;
    	private SimpleStringProperty title, issue, volume, issn, num_articles, publisher, pub_date, genreName, langName, avail;
    	
    	public Journal (String id, String title, String issue, String volume, String issn, String num_articles, String publisher, String pub_date, String genreName, String langName, String avail) {
    		this.id = new SimpleIntegerProperty(Integer.parseInt(id));
    		this.title = new SimpleStringProperty(title);
    		this.issue = new SimpleStringProperty(issue);
    		this.volume = new SimpleStringProperty(volume);
    		this.issn = new SimpleStringProperty(issn);
    		this.num_articles = new SimpleStringProperty(num_articles);
    		this.publisher = new SimpleStringProperty(publisher);
    		this.pub_date = new SimpleStringProperty(pub_date);
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
		public String getIssue() {
			return issue.get();
		}
		public String getVolume() {
			return volume.get();
		}
		public String getIssn() {
			return String.format("%08d", Integer.parseInt(issn.get()));
		}
		public String getNum_articles() {
			return num_articles.get();
		}
		public String getPublisher() {
			return publisher.get();
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
		public String getAvail() {
			return avail.get();
		}
    }
}