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
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
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
	String location = "";
	HBox sourcehbox = new HBox();
	
	GridPane discardpile = new GridPane();
	GridPane hand = new GridPane();
	GridPane inplay = new GridPane();
	GridPane prizes = new GridPane();
	GridPane deck = new GridPane();
	
	DragDropHandler ddh = new DragDropHandler();
	
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
		scene.getStylesheets().add("application/styles.css");
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
	
	public void setupGestureSource(Node n){
		//System.out.println("1");
		n.setOnDragDetected(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {

				String cname = "";
				String cnum = "";
				boolean found = false;
				//is it from the decklist?
				/*for (int d = 0; d < decklist.getChildren().size(); d++){
					if (arg0.getSource().equals(decklist.getChildren().get(d))){//was found
						found = true;
						Label ttl = (Label)((HBox)(decklist.getChildren().get(d))).getChildren().get(0);
						cname = ttl.getText();
						Label nmb = (Label)((HBox)(decklist.getChildren().get(d))).getChildren().get(1);
						cnum = nmb.getText();
						location = "decklist";
						break;
					}
				}
				*/
				//is it hand?
				if (!found){
					//TODO
					for (int d = 0; d < hand.getChildren().size(); d++){
						if (arg0.getSource() instanceof Label || arg0.getSource() instanceof GridPane || arg0.getSource() instanceof HBox){
							//System.out.println("MIMEIFE");
						}
						if (/*sa.equals(hand.getChildren().get(d)) &&*/ arg0.getSource() instanceof GridPane){//was found
							System.out.println("STUFF");
							found = true;
							Label ttl = (Label)(hand.getChildren().get(d));
							System.out.println("lbl" + ttl);
							cname = ttl.getText();
							Label nmb = (Label)(hand.getChildren().get(d));
							cnum = nmb.getText();
							location = "hand";
							System.out.println(	location	);
							sourcehbox = new HBox(new Label(((Label)((GridPane)arg0.getSource()).getChildren().get(d)).getText()));
							break;
						}
					}
				}
				//is it prizes?
				if (!found){
					for (int d = 0; d < prizes.getChildren().size(); d++){
						if (arg0.getSource().equals(prizes.getChildren().get(d))){//was found
							found = true;
							Label ttl = (Label)((HBox)(prizes.getChildren().get(d))).getChildren().get(0);
							cname = ttl.getText();
							Label nmb = (Label)((HBox)(prizes.getChildren().get(d))).getChildren().get(1);
							cnum = nmb.getText();
							location = "prizes";
							sourcehbox = ((HBox)arg0.getSource());
							break;
						}
					}
				}
				//is it deck?
				if (!found){
					for (int d = 1; d < deck.getChildren().size(); d++){
						if (arg0.getSource().equals(deck.getChildren().get(d))){//was found
							found = true;
							Label ttl = (Label)((HBox)(deck.getChildren().get(d))).getChildren().get(0);
							cname = ttl.getText();
							Label nmb = (Label)((HBox)(deck.getChildren().get(d))).getChildren().get(1);
							cnum = nmb.getText();
							location = "deck";
							
							sourcehbox = ((HBox)arg0.getSource());
							break;
						}
					}
				}
				//is it in play?
				if (!found){
					for (int d = 0; d < inplay.getChildren().size(); d++){
						if (arg0.getSource().equals(inplay.getChildren().get(d))){//was found
							found = true;
							Label ttl = (Label)((HBox)(inplay.getChildren().get(d))).getChildren().get(0);
							cname = ttl.getText();
							Label nmb = (Label)((HBox)(inplay.getChildren().get(d))).getChildren().get(1);
							cnum = nmb.getText();
							location = "inplay";
							sourcehbox = ((HBox)arg0.getSource());
							break;
						}
					}
				}
				//is it discard?
				if (!found){
					for (int d = 0; d < discardpile.getChildren().size(); d++){
						if (arg0.getSource().equals(discardpile.getChildren().get(d))){//was found
							found = true;
							Label ttl = (Label)((HBox)(discardpile.getChildren().get(d))).getChildren().get(0);
							cname = ttl.getText();
							Label nmb = (Label)((HBox)(discardpile.getChildren().get(d))).getChildren().get(1);
							cnum = nmb.getText();
							location = "discardpile";
							sourcehbox = ((HBox)arg0.getSource());
							break;
						}
					}
				}
				found = false;
				
				//cnum = (	(Label)arg0.getSource() ).getText();
				//System.out.println(cname + " -> " + cnum);
				Dragboard db = n.startDragAndDrop(TransferMode.ANY);//HBox
				
				/* put a string on dragboard */
                ClipboardContent content = new ClipboardContent();
                content.putString(cname + "^" + cnum);
                db.setContent(content);
                
                arg0.consume();
				
				
			}
			
		});
		
		n.setOnDragDone(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture ended */
                //System.out.println("onDragDone");
                /* if the data was successfully moved, clear it */
                if (event.getTransferMode() == TransferMode.MOVE) {
                	/*if (location.equals("decklist")){
                		//find it in decklist and remove it
                		for (int d = 0; d < decklist.getChildren().size(); d++){
                			if (((HBox)event.getSource()).equals(decklist.getChildren().get(d))){
                				decklist.getChildren().remove(d);
                			}
                		}
                	} else */if (location.equals("deck")){
                		//find it in deck and remove it
                		for (int d = 0; d < deck.getChildren().size(); d++){
                			if (((HBox)event.getSource()).equals(deck.getChildren().get(d))){
                				String cnumb = ((Label)((HBox)deck.getChildren().get(d)).getChildren().get(1)).getText();
                				if (Integer.parseInt(cnumb) > 1){
                				int q = Integer.parseInt(cnumb) - 1;
                				((Label)((HBox)deck.getChildren().get(d)).getChildren().get(1)).setText("" + q);
                				} else {
                					deck.getChildren().remove(d);
                				}
                				
                				//System.out.println(cnumb);
                			}
                		}
                	}else if (location.equals("hand")){
                		//find it in hand and remove it
                		//System.out.println("DEBUG");
                		for (int d = 0; d < hand.getChildren().size(); d++){
                			if (((HBox)event.getSource()).equals(hand.getChildren().get(d))){
                				
                				//String cnumb = ((Label)((HBox)deck.getChildren().get(d)).getChildren().get(1)).getText();
                				
                					//deck.getChildren().remove(d);
                				
                				hand.getChildren().remove(d);
                			}
                		}
                	}else if (location.equals("discardpile")){
                		//find it in discardpile and remove it
                		for (int d = 0; d < discardpile.getChildren().size(); d++){
                			if (((HBox)event.getSource()).equals(discardpile.getChildren().get(d))){
                				discardpile.getChildren().remove(d);
                			}
                		}
                	}else if (location.equals("prizes")){
                		//find it in prizes and remove it
                		for (int d = 0; d < prizes.getChildren().size(); d++){
                			if (((HBox)event.getSource()).equals(prizes.getChildren().get(d))){
                				prizes.getChildren().remove(d);
                			}
                		}
                	}else if (location.equals("inplay")){
                		//find it in play and remove it
                		for (int d = 0; d < inplay.getChildren().size(); d++){
                			if (((HBox)event.getSource()).equals(inplay.getChildren().get(d))){
                				inplay.getChildren().remove(d);
                			}
                		}
                	}
                }
                location = "";
                event.consume();
            }
        });
	}
	
	public void setupGestureTarget(GridPane target){
		target.setOnDragOver(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data is dragged over the target */
               // System.out.println("onDragOver");
                
                /* accept it only if it is  not dragged from the same node 
                 * and if it has a string data */
                if (event.getGestureSource() != target &&
                        event.getDragboard().hasString()) {
                    /* allow for both copying and moving, whatever user chooses */
                    event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                }
                
                event.consume();
            }
        });

        target.setOnDragEntered(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* the drag-and-drop gesture entered the target */
               // System.out.println("onDragEntered");
                /* show to the user that it is an actual gesture target */
                if (event.getGestureSource() != target &&
                        event.getDragboard().hasString()) {
                   // target.setFill(Color.GREEN);
                }
                
                event.consume();
            }
        });

        target.setOnDragExited(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* mouse moved away, remove the graphical cues */
                //target.setFill(Color.BLACK);
                
                event.consume();
            }
        });
        
        target.setOnDragDropped(new EventHandler <DragEvent>() {
            public void handle(DragEvent event) {
                /* data dropped */
            //    System.out.println("onDragDropped");
                /* if there is a string data on dragboard, read it and use it */
                Dragboard db = event.getDragboard();
                boolean success = false;
                if (db.hasString()) {
                   // target.setText(db.getString());
                    success = true;
                    HBox n = new HBox();
                    n.getChildren().add(new Label(((Label)sourcehbox.getChildren().get(0)).getText()));
                   ((GridPane)target).add(n,0,((GridPane)target).getChildren().size());
                   //((GridPane)target).getChildren().add((HBox)((GridPane)event.getSource()));
                }
                /* let the source know whether the string was successfully 
                 * transferred and used */
                event.setDropCompleted(success);
                //System.out.println(((Label)sourcehbox.getChildren().get(0)).getText());
                event.consume();
            }
        });

        
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
		setupGestureTarget(inplay);
		setupGestureSource(inplay);
	}
	
	public void createHand(){
		Label l = new Label("Hand");
		l.setPrefWidth(300);
		l.getStyleClass().addAll("pane", "zonelabel");
		hand.add(l,0,0,2,1);
		hand.setPrefSize(300,400);
		hand.getStyleClass().addAll("pane", "hand");
		setupGestureTarget(hand);
		for (int i = 1; i < hand.getChildren().size(); i++){
			setupGestureSource(hand);
		}
		
	}

	public void createDiscardPile(){
		Label l = new Label("DiscardPile");
		l.setPrefWidth(300);
		l.getStyleClass().addAll("pane", "zonelabel");
		discardpile.add(l,0,0,2,1);
		discardpile.setPrefSize(300,400);
		discardpile.getStyleClass().addAll("pane", "discardpile");
		setupGestureTarget(discardpile);
		setupGestureSource(discardpile);
	}

	public void createDeck(){
		Label l = new Label("Deck");
		l.setPrefWidth(300);
		l.getStyleClass().addAll("pane", "zonelabel");
		HBox labelbox = new HBox();
		labelbox.getChildren().add(l);
		deck.add(labelbox,0,0,2,1);
		deck.setPrefSize(300,400);
		deck.getStyleClass().addAll("pane", "deck");
		
		
		HBox hb;
		Label newnameLabel;
		Label newnumLabel;
		for (int i = 1; i < decklist.getChildren().size(); i++){
			//HBox sourcebox = (HBox)decklist.getChildren().get(i);
			hb = new HBox();
			newnameLabel = new Label(((Label)((HBox)decklist.getChildren().get(i)).getChildren().get(0)).getText());
			

			
			HBox.setHgrow(newnameLabel, Priority.ALWAYS);
			newnameLabel.setAlignment(Pos.CENTER_LEFT);
	    	newnameLabel.setMaxWidth(Double.MAX_VALUE);
	    	newnameLabel.setPrefWidth(280);
			newnumLabel = new Label(((Label)((HBox)decklist.getChildren().get(i)).getChildren().get(1)).getText());
			//System.out.println(newnameLabel.getText());
			//HBox hb1 = new HBox();
			HBox.setHgrow(newnumLabel, Priority.NEVER);
			
	    	
	    	hb.getChildren().add(newnameLabel);
			hb.getChildren().add(newnumLabel);
			newnumLabel.setMaxWidth(Double.MAX_VALUE);

			newnumLabel.setStyle("-fx-background-color: #6699CC;");
	    	newnumLabel.setAlignment(Pos.CENTER_RIGHT);

			deck.add(hb,0,i);
			setupGestureSource(deck.getChildren().get(i));
		}
		
	}

	public void createPrizes(){
		Label l = new Label("Prizes");
		l.setPrefWidth(300);
		l.getStyleClass().addAll("pane", "zonelabel");
		prizes.add(l,0,0,2,1);
		prizes.setPrefSize(300,400);
		prizes.getStyleClass().addAll("pane", "prizes");
		setupGestureTarget(prizes);
		setupGestureSource(prizes);
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
	    
	    /*for(int i = 0; i < decklist.getChildren().size(); i++){
			setupGestureSource(decklist.getChildren().get(i));
		};*/
		
	    g.getStyleClass().addAll("pane", "gpane");
	    
	}
	
 public static void main(String[] args) {
        launch(args);
    }
}//judged

