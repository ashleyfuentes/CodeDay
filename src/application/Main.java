package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.*;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;

public class Main extends Application {
	
	StringProperty playername = new SimpleStringProperty("");
	String deckname = "DEFAULT";
	Scanner scanner;
	boolean reset = false;
	File decklistfile;
	FileChooser fileChooser = new FileChooser();
	Button filechooserbutton = new Button("Open Decklist");
	StackPane decklistpanel = new StackPane();
	GridPane decklist = new GridPane();
	BorderPane rootpane = new BorderPane();
	GridPane centerpane = new GridPane();
	
	GridPane discardpile = new GridPane();
	GridPane hand = new GridPane();
	GridPane inplay = new GridPane();
	GridPane prizes = new GridPane();
	GridPane deck = new GridPane();
	
	ArrayList<String> cardsinlist = new ArrayList<String>();
	ArrayList<String> numsinlist = new ArrayList<String>();
	
	@Override
    public void start(Stage primaryStage) {
		primaryStage.setTitle("Pokemon CCG Commentator Tool");
		
		rootpane.setPrefWidth(1200.0);
		rootpane.setPrefHeight(800.0);
		rootpane.setTop(createTopLabel());
		decklistpanel.getChildren().add(decklist);
		decklist.getStyleClass().addAll("pane", "gpane");
		Scene scene= new Scene(rootpane);
		scene.getStylesheets().add("styles/style.css");
		rootpane.setLeft(decklistpanel);
		
		filechooserbutton.setOnAction(
	            new EventHandler<ActionEvent>() {
	                @Override
	                public void handle(final ActionEvent e) {
	                    decklistfile = fileChooser.showOpenDialog(primaryStage);
	                    parseFile(decklistfile);

	                    rootpane.setCenter(createcenterpane());
	                    centerpane.getStyleClass().addAll("pane", "centerpanel");
	                }
	            });
		rootpane.setCenter(filechooserbutton);
		Button resetButton = createResetButton();
		rootpane.setBottom(resetButton);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
	
	public GridPane createcenterpane(){
		createInplay();
		centerpane.add(inplay,0,0);
		
		createPrizes();
		centerpane.add(prizes,0,1);
		
		createHand();
		centerpane.add(hand,1,0);
		
		createDiscardPile();
		centerpane.add(discardpile,1,1);
		
		createDeck();
		centerpane.add(deck,2,0,1,2);
		
		return centerpane;
	}
	
	public void createInplay(){
		Label l = new Label("In Play");
		l.setPrefWidth(300);
		l.getStyleClass().addAll("pane", "zonelabel");
		inplay.add(l,0,0,2, 1);
		inplay.setPrefSize(300,400);
		inplay.getStyleClass().addAll("pane", "inplay");
	}
	
	public void createHand(){
		Label l = new Label("Hand");
		l.setPrefWidth(300);
		l.getStyleClass().addAll("pane", "zonelabel");
		hand.add(l,0,0,2,1);
		hand.setPrefSize(300,400);
		hand.getStyleClass().addAll("pane", "hand");
	}

	public void createDiscardPile(){
		Label l = new Label("DiscardPile");
		l.setPrefWidth(300);
		l.getStyleClass().addAll("pane", "zonelabel");
		discardpile.add(l,0,0,2,1);
		discardpile.setPrefSize(300,400);
		discardpile.getStyleClass().addAll("pane", "discardpile");
	}

	public void createDeck(){
		Label l = new Label("Deck");
		l.setPrefWidth(300);
		l.getStyleClass().addAll("pane", "zonelabel");
		deck.add(l,0,0,2,1);
		deck.setPrefSize(300,400);
		deck.getStyleClass().addAll("pane", "deck");
	}

	public void createPrizes(){
		Label l = new Label("Prizes");
		l.setPrefWidth(300);
		l.getStyleClass().addAll("pane", "zonelabel");
		prizes.add(l,0,0,2,1);
		prizes.setPrefSize(300,400);
		prizes.getStyleClass().addAll("pane", "prizes");
	}
	
	public void parseFile(File f){
		try{
			scanner = new Scanner(f);
		} catch(FileNotFoundException fnf){
			System.out.println(fnf.getMessage());
		}
		String a = scanner.nextLine();
		playername.set(a.substring(12));
		String b = scanner.nextLine();
		deckname = b.substring(10);
		deckname = deckname.replaceAll("\"", "");
		scanner.nextLine();
		while (scanner.hasNext()){
			scanner.useDelimiter(",");
			String tmp = scanner.nextLine();
			String[] s = tmp.split(",");
			cardsinlist.add(s[0]);
			numsinlist.add(s[1]);
		}
		populateDeckList(decklist);
	}
	
	public Label createTopLabel(){
		 Label l = new Label("");
		    l.setStyle("-fx-background-color: #6699CC;");
		    l.setPrefWidth(1200.0);
		    l.setPrefHeight(50.0);
		    l.getStyleClass().add("hbox");
		    Font f = Font.font("Arial", FontWeight.BOLD, 20);
		    l.setFont(f);
		    l.alignmentProperty().set(Pos.CENTER);
		    l.getStyleClass().addAll("pane", "vbox");
		    l.textProperty().bind(playername);
		    return l;
	}
	
	public Button createResetButton(){
		Button b = new Button("Reset");
		b.setOnAction(
	            new EventHandler<ActionEvent>() {
	                @Override
	                public void handle(final ActionEvent e) {
	                    	decklistfile = null;
	                    	decklist.getChildren().clear();
	                    	reset = !reset;
	                		cardsinlist.clear();
	                		numsinlist.clear();
	                    deck.getChildren().clear();
	                    prizes.getChildren().clear();
	                    hand.getChildren().clear();
	                    discardpile.getChildren().clear();
	                    inplay.getChildren().clear();
	                    	centerpane = new GridPane();
	                    	playername.set("");
	                    	rootpane.setCenter(filechooserbutton);
	                	
	                    //decklists.getChildren().add(decklists.getChildren().remove(0));
	                }
	            });
		return b;
	}
	
	public void populateDeckList(GridPane g) {
		Label lbl = new Label();
	    lbl.setText(playername.getValue() + "'s Decklist");
	    lbl.setFont(new Font("Arial", 22));
	    g.getChildren().add(lbl);
	    for (int i=0; i < cardsinlist.size(); i++){
	    	HBox h = new HBox();
	    	Label card = new Label(cardsinlist.get(i));
	    	HBox.setHgrow(card, Priority.NEVER);
	    	h.getChildren().add(card);
	    	
	    	Label num = new Label(numsinlist.get(i));
	    	HBox.setHgrow(num, Priority.ALWAYS);
	    	num.setMaxWidth(Double.MAX_VALUE);
	    	num.setAlignment(Pos.CENTER_RIGHT);
	    	h.getChildren().add(num);
	    	g.add(h,0,i+1);
	    }
	    g.getStyleClass().addAll("pane", "gpane");
	    
	}
	
 public static void main(String[] args) {
        launch(args);
    }
}