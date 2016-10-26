package ui;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import base.Folder;
import base.Note;
import base.NoteBook;
import base.TextNote;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 * NoteBook GUI with JAVAFX
 * 
 * COMP 3021
 * 
 * 
 * @author valerio
 *
 */
public class NoteBookWindow extends Application {

	/**
	 * TextArea containing the note
	 */
	final TextArea textAreaNote = new TextArea("");
	/**
	 * list view showing the titles of the current folder
	 */
	final ListView<String> titleslistView = new ListView<String>();
	/**
	 * 
	 * Combobox for selecting the folder
	 * 
	 */
	final ComboBox<String> foldersComboBox = new ComboBox<String>();
	/**
	 * This is our Notebook object
	 */
	NoteBook noteBook = null;
	/**
	 * current folder selected by the user
	 */
	String currentFolder = "";
	/**
	 * current search string
	 */
	String currentSearch = "";
	
	/**
	 * current note selected by the user
	 */
	String currentNote="";

	/**
	 * mainStage for dispaly
	 */
	Stage mainStage;
	
	public static void main(String[] args) {
		launch(NoteBookWindow.class, args);
	}

	@Override
	public void start(Stage stage) {
		loadNoteBook();
		mainStage=stage;
		
		// Use a border pane as the root for scene
		BorderPane border = new BorderPane();
		// add top, left and center
		border.setTop(addHBox());
		border.setLeft(addVBox());
		border.setCenter(addGridPane());
        
		Scene scene = new Scene(border);
		stage.setScene(scene);
		stage.setTitle("NoteBook COMP 3021");
		stage.show();
	}
	
	

	/**
	 * This create the top section
	 * 
	 * @return
	 */
	private HBox addHBox() {

		HBox hbox = new HBox();
		hbox.setPadding(new Insets(15, 12, 15, 12));
		hbox.setSpacing(10); // Gap between nodes

		Button buttonLoad = new Button("Load from File");
		buttonLoad.setPrefSize(100, 20);
		buttonLoad.setDisable(false);
		
		//lab 8
		buttonLoad.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please choose a file which contains a NoteBook object!");
				
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized object File (*.ser)","*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				
				File file = fileChooser.showOpenDialog(mainStage);
				
				if(file!=null){		
					loadNoteBook(file);	
				}
			}
		});
	
		
		
		Button buttonSave = new Button("Save to File");
		buttonSave.setPrefSize(100, 20);
		buttonSave.setDisable(false);

		//lab 8
		buttonSave.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Please choose a file which contains a NoteBook object!");
				
				FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Serialized object File (*.ser)","*.ser");
				fileChooser.getExtensionFilters().add(extFilter);
				
				File file = fileChooser.showOpenDialog(mainStage);
				
				if(noteBook.save(file.getAbsolutePath())){
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Successfully saved");
					alert.setContentText("Your file has been saved to file "+file.getName());
					alert.showAndWait().ifPresent(rs->{
						if(rs == ButtonType.OK){
							System.out.println("Pressed OK.");
						}
					});	
					
				}
				
			}
		});
		
		
		
	    //lab 7
	   //add "search" label/textField and two button (Search & Clear Search) objects
		Label label = new Label("Search:");
		TextField textSearch= new TextField();
		textSearch.setPrefWidth(200);
		//
		
		Button buttonSearch = new Button("Search");
		buttonSearch.setPrefSize(100, 20);
		
		buttonSearch.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
				         
			currentSearch = textSearch.getText();
			 
		    updateListView();
		    
			}	
		});
			
		Button buttonRemove = new Button("Clear Search");
		buttonRemove.setPrefSize(100, 20);
		
		buttonRemove.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent event){
			   
				currentSearch = "";
				textSearch.setText("");
				textAreaNote.setText("");
				updateListView(); 
			}
		});
		
		hbox.getChildren().addAll(buttonLoad, buttonSave,label, textSearch,buttonSearch,buttonRemove);
		
		return hbox;
	}

	/**
	 * this create the section on the left
	 * 
	 * @return
	 */
	private VBox addVBox() {

		VBox vbox = new VBox(); // lab8
		HBox hbox = new HBox();
		vbox.setPadding(new Insets(10)); // Set all sides to 10
		vbox.setSpacing(8); // Gap between nodes
		hbox.setSpacing(10); //lab 8

	 //lab7
	 //  foldersComboBox.getItems().addAll("FOLDER NAME 1", "FOLDER NAME 2", "FOLDER NAME 3");
     //  access the noteBook object and get all folder names 
	   
	   for(Folder f:noteBook.getFolders()){
		   foldersComboBox.getItems().addAll(f.getName());
	   }

		foldersComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null||ov==null||t==null)
					return;
				
				currentFolder = t1.toString();
				 
				// this contains the name of the folder selected
				// TODO update listview
				
				updateListView();

			}

		});

		foldersComboBox.setValue("-----");

		titleslistView.setPrefHeight(100);

		titleslistView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Object>() {
			@Override
			public void changed(ObservableValue ov, Object t, Object t1) {
				if (t1 == null)
					return;
				String title = t1.toString();
				currentNote = t1.toString(); //lab 8
				// This is the selected title
				// TODO load the content of the selected note in
				// textAreNote
				String content = "";
				
				//1. get the notes list of the currentFolder
				//2. search the notes, get the content 
				List<Note> notes = new ArrayList<Note>();
				for(Folder f: noteBook.getFolders()){
					
					if(f.getName()==currentFolder)
						notes= f.getNotes();
				}
				
				for(Note n: notes){
					if(n.getTitle()==title)
						content=((TextNote)n).getContent();
				}
				
				textAreaNote.setText(content);

			}
		});
		
		//lab 8
		Button buttonAddFolder = new Button("Add a Folder");
		buttonAddFolder.setPrefSize(100, 20);
		
		buttonAddFolder.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				
				TextInputDialog dialog = new TextInputDialog("Add a Folder");
				dialog.setTitle("Input");
				dialog.setHeaderText("Add a new folder for your notebook:");
				dialog.setContentText("Please enter the name you want to create:");
				
				//Traditional way to get the response value.
				Optional<String> result = dialog.showAndWait();
				
				if(result.isPresent()){
					//TODO 
					String input = dialog.getEditor().getText();
					if(input.equals("")){
						
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Warning");
						alert.setHeaderText("Warning");
						alert.setContentText("Please input an valid folder name");
						alert.showAndWait().ifPresent(rs->{
							if(rs == ButtonType.OK){
								System.out.println("Pressed OK.");
							}
						});	
					}
					else{
						boolean name_exist=false;
						for(Folder f: noteBook.getFolders()){
							if(input.equals(f.getName()))
								name_exist=true;
						}
						
						if(!name_exist){
							
							noteBook.addFolder(input);
						    foldersComboBox.getItems().add(input);
							
						}
						else{
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Warning");
							alert.setHeaderText("Warning");
							alert.setContentText("You already have a folder named with "+input);
							alert.showAndWait().ifPresent(rs->{
								if(rs == ButtonType.OK){
									System.out.println("Pressed OK.");
								}
							});	
						}
						
						
					}
				}
			}
		});
		
		//lab 8
		Button buttonAddNote = new Button("Add a Note");
		buttonAddNote.setPrefSize(100, 20);
		
		buttonAddNote.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				
				if(currentFolder.equals("-----")){
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setHeaderText("Warning");
					alert.setContentText("Please choose a folder first!");
					alert.showAndWait().ifPresent(rs->{
						if(rs == ButtonType.OK){
							System.out.println("Pressed OK.");
						}
					});	
				}
				else{
					TextInputDialog dialog = new TextInputDialog("Add a Note");
					dialog.setTitle("Input");
					dialog.setHeaderText("Add a new note to current folder");
					dialog.setContentText("Please enter the name of your note:");
					
					//Traditional way to get the response value.
					Optional<String> result = dialog.showAndWait();
					if(result.isPresent()){
						
						String input = dialog.getEditor().getText();
						if(noteBook.createTextNote(currentFolder, input)){
							
							updateListView();
							
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Successful!");
							alert.setHeaderText("Message");
							alert.setContentText("Insert note " + input + " to folder "+currentFolder+ " successfully!");
							alert.showAndWait().ifPresent(rs->{
								if(rs == ButtonType.OK){
									System.out.println("Pressed OK.");
								}
							});	
						}
					}
				}
			}
		});
		
		
		hbox.getChildren().addAll(foldersComboBox,buttonAddFolder);
		vbox.getChildren().add(new Label("Choose folder: "));
		vbox.getChildren().add(hbox);		
		vbox.getChildren().add(new Label("Choose note title"));
		vbox.getChildren().add(titleslistView);
		vbox.getChildren().add(buttonAddNote);

		return vbox;
	}
	
//lab 7
private void updateListView() {
		
	ArrayList<String> list = new ArrayList<String>();

		// TODO populate the list object with all the TextNote titles of the
		// currentFolder
		
		
	if(currentSearch==""){
		
		for(Folder f: noteBook.getFolders()){
			
			if(f.getName()==currentFolder)
				for(Note n:f.getNotes()){
					list.add(n.getTitle());
				}
		}
	}
	else{
		     textAreaNote.setText("");
	         List<Note> notes=noteBook.searchNotes(currentSearch);
	  
	         for(Folder f:noteBook.getFolders()){
		
		       if(f.getName()==currentFolder)
			     for(Note n: notes){
				   if(f.getNotes().contains(n))
					  list.add(n.getTitle());
			       }
	         }
	}
		
		
		ObservableList<String> combox2 = FXCollections.observableArrayList(list);
		titleslistView.setItems(combox2);
		textAreaNote.setText("");
}

	/*
	 * Creates a grid for the center region with four columns and three rows
	 */
	private GridPane addGridPane() {

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10, 10, 10, 10));
		textAreaNote.setEditable(true);
		textAreaNote.setMaxSize(450, 400);
		textAreaNote.setWrapText(true);
		textAreaNote.setPrefWidth(450);
		textAreaNote.setPrefHeight(400);
		// 0 0 is the position in the grid
		
		//lab 8
		HBox hbox = new HBox();
		hbox.setSpacing(8);
		
		ImageView saveView = new ImageView(new Image(new File("save.png").toURI().toString()));
		saveView.setFitHeight(18);
		saveView.setFitWidth(18);
		saveView.setPreserveRatio(true);
		
		Button buttonSaveNote = new Button("Save Note");
		buttonSaveNote.setPrefSize(100, 20);
		
		buttonSaveNote.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				
			  if(currentFolder.equals("-----")||currentNote==""){
				  
				  Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Warning");
					alert.setHeaderText("Warning");
					alert.setContentText("Please select a folder and a note");
					alert.showAndWait().ifPresent(rs->{
						if(rs == ButtonType.OK){
							System.out.println("Pressed OK.");
						}
					});	
			  }
			  else{
				    List<Note> notes = new ArrayList<Note>();
					for(Folder f: noteBook.getFolders()){
						
						if(f.getName()==currentFolder)
							notes= f.getNotes();
					}
					
					for(Note n: notes){
						if(n.getTitle()==currentNote)
							((TextNote)n).setContent(textAreaNote.getText());
					}
			  }
			}
		});
		
		
		
		ImageView deleteView = new ImageView(new Image(new File("delete.png").toURI().toString()));
		deleteView.setFitHeight(18);
		deleteView.setFitWidth(18);
		deleteView.setPreserveRatio(true);
		
		Button buttonDeleteNote = new Button("Delete Note");
		buttonDeleteNote.setPrefSize(100, 20);
		
		buttonDeleteNote.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e){
				
				if(currentFolder.equals("-----")||currentNote==""){
					  
					    Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Warning");
						alert.setHeaderText("Warning");
						alert.setContentText("Please select a folder and a note");
						alert.showAndWait().ifPresent(rs->{
							if(rs == ButtonType.OK){
								System.out.println("Pressed OK.");
							}
						});	
				  }
				else{
					   for(Folder f:noteBook.getFolders()){
						   
						   if(f.getName().equals(currentFolder)){
							   
				           	   f.removeNotes(currentNote);
				           	   updateListView();
				           	   
				               Alert alert = new Alert(AlertType.INFORMATION);
							   alert.setTitle("Succeed");
							   alert.setHeaderText("Confirmation");
							   alert.setContentText("Your note has been successfully removed");
							   alert.showAndWait().ifPresent(rs->{
								if(rs == ButtonType.OK){
									System.out.println("Pressed OK.");
								}
							});	
						   }
					   }
				}
			}
		});
		
		hbox.getChildren().addAll(saveView,buttonSaveNote,deleteView,buttonDeleteNote);
		grid.add(hbox,0, 0);
		grid.add(textAreaNote, 0, 1);

		return grid;
	}

	private void loadNoteBook() {
		NoteBook nb = new NoteBook();
		nb.createTextNote("COMP3031", "COMP3021 syllabus", "Be able to implement object-oriented concepts in Java.");
		nb.createTextNote("COMP3031", "course information",
				"Introduction to Java Programming. Fundamentals include language syntax, object-oriented programming, inheritance, interface, polymorphism, exception handling, multithreading and lambdas.");
		nb.createTextNote("COMP3031", "Lab requirement",
				"Each lab has 2 credits, 1 for attendence and the other is based the completeness of your lab.");

		nb.createTextNote("Books", "The Throwback Special: A Novel",
				"Here is the absorbing story of twenty-two men who gather every fall to painstakingly reenact what ESPN called �he most shocking play in NFL history�� and the Washington Redskins dubbed the Throwback Special: the November 1985 play in which the Redskins�� Joe Theismann had his leg horribly broken by Lawrence Taylor of the New York Giants live on Monday Night Football. With wit and great empathy, Chris Bachelder introduces us to Charles, a psychologist whose expertise is in high demand; George, a garrulous public librarian; Fat Michael, envied and despised by the others for being exquisitely fit; Jeff, a recently divorced man who has become a theorist of marriage; and many more. Over the course of a weekend, the men reveal their secret hopes, fears, and passions as they choose roles, spend a long night of the soul preparing for the play, and finally enact their bizarre ritual for what may be the last time. Along the way, mishaps, misunderstandings, and grievances pile up, and the comforting traditions holding the group together threaten to give way. The Throwback Special is a moving and comic tale filled with pitch-perfect observations about manhood, marriage, middle age, and the rituals we all enact as part of being alive.");
		nb.createTextNote("Books", "Another Brooklyn: A Novel",
				"The acclaimed New York Times bestselling and National Book Award�inning author of Brown Girl Dreaming delivers her first adult novel in twenty years. Running into a long-ago friend sets memory from the 1970s in motion for August, transporting her to a time and a place where friendship was everything�ntil it wasn�. For August and her girls, sharing confidences as they ambled through neighborhood streets, Brooklyn was a place where they believed that they were beautiful, talented, brilliant� part of a future that belonged to them. But beneath the hopeful veneer, there was another Brooklyn, a dangerous place where grown men reached for innocent girls in dark hallways, where ghosts haunted the night, where mothers disappeared. A world where madness was just a sunset away and fathers found hope in religion. Like Louise Meriwether� Daddy Was a Number Runner and Dorothy Allison� Bastard Out of Carolina, Jacqueline Woodson� Another Brooklyn heartbreakingly illuminates the formative time when childhood gives way to adulthood�he promise and peril of growing up�nd exquisitely renders a powerful, indelible, and fleeting friendship that united four young lives.");

		nb.createTextNote("Holiday", "Vietnam",
				"What I should Bring? When I should go? Ask Romina if she wants to come");
		nb.createTextNote("Holiday", "Los Angeles", "Peter said he wants to go next Agugust");
		nb.createTextNote("Holiday", "Christmas", "Possible destinations : Home, New York or Rome");
		noteBook = nb;

	}
	
	private void loadNoteBook(File file){
		
		noteBook = new NoteBook(file.getAbsolutePath());
		foldersComboBox.getItems().clear();
		for(Folder f:noteBook.getFolders()){
			foldersComboBox.getItems().addAll(f.getName());
		 }	
		foldersComboBox.setValue("-----");
	}

}
