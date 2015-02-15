package dragndropv2;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 
public class dragndrop extends Application {
 
    int posx = 60;
    int posy = 60;
    double width = 900;
    double height = 500;
    ImageView iv;
    int nombreLeft = 10; 
    int nombreRight = 0; 
    int curseur = 0 ;
     
    Text text1;
    Text text2;
     
    Group root;
     
    public static void main(String[] args) {
        launch(args);
    }
      
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TEST FX");
        root = new Group();
//        Scene scene = new Scene(root, width, height, Color.WHITE);
        Scene scene = new Scene(root, 800, 200, Color.WHITE);
 
        HBox hBox1 = new HBox();
        hBox1.setPrefWidth(400);
        hBox1.setPrefHeight(45);
        hBox1.setStyle("-fx-border-color: red;"
              + "-fx-border-width: 1;"
              + "-fx-border-style: solid;");
        
        HBox hBox2 = new HBox();
        hBox2.setPrefWidth(400);
        hBox2.setPrefHeight(45);
        hBox2.setStyle("-fx-border-color: blue;"
              + "-fx-border-width: 1;"
              + "-fx-border-style: solid;");
         
        HBox hBox3 = new HBox();
        hBox3.setPrefWidth(400);
        hBox3.setPrefHeight(80);
        hBox3.setStyle("-fx-border-color: black;"
              + "-fx-border-width: 1;"
              + "-fx-border-style: solid;");
        hBox3.setTranslateX(200);
        hBox3.setTranslateY(100);
         
        text2 = new  Text("Ballon a droite : "+ nombreRight);
        text2.setTranslateX(220);
        text2.setTranslateY(130);
         
        text1 = new  Text("Ballon a gauche : "+ nombreLeft);
        text1.setTranslateX(220);
        text1.setTranslateY(160);
 
        insertImage(new Image("ress/logo-ballon.jpg"), hBox1);
        insertImage(new Image("ress/logo-ballon.jpg"), hBox1);
        insertImage(new Image("ress/logo-ballon.jpg"), hBox1);
        insertImage(new Image("ress/logo-ballon.jpg"), hBox1);
        insertImage(new Image("ress/logo-ballon.jpg"), hBox1);
        insertImage(new Image("ress/logo-ballon.jpg"), hBox1);
        insertImage(new Image("ress/logo-ballon.jpg"), hBox1);
        insertImage(new Image("ress/logo-ballon.jpg"), hBox1);
        insertImage(new Image("ress/logo-ballon.jpg"), hBox1);
        insertImage(new Image("ress/logo-ballon.jpg"), hBox1);
 
        setupGestureTarget(hBox1);
        setupGestureTarget(hBox2);
          
        HBox vBox = new HBox();
        vBox.getChildren().addAll(hBox1, hBox2);
        root.getChildren().addAll(vBox);
        root.getChildren().add(hBox3);
        root.getChildren().add(text1);
        root.getChildren().add(text2);
 
        primaryStage.setScene(scene);
        primaryStage.show();
 
    }
     
    void insertImage(Image i, HBox hb1){
          
        iv = new ImageView();
        iv.setImage(i);
          
        setupGestureSource(iv);
 
        hb1.getChildren().add(iv);
    }
 
    void setupGestureSource(final ImageView source){
          
        source.setOnDragDetected(new EventHandler <MouseEvent>() {
 
           @Override
           public void handle(MouseEvent event) {
 
               /* allow any transfer mode */
               Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
                 
               /* put a image on dragboard */
               ClipboardContent content = new ClipboardContent();
                 
               Image sourceImage = source.getImage();
               content.putImage(sourceImage);
               db.setContent(content);
 
 
                iv = source ;
                
                 
               event.consume();
           }
       });
         
            source.setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent e) {
                    source.setCursor(Cursor.HAND);
//                    System.out.println("e...: "+e.getSceneX());
                    curseur = (int) e.getSceneX();
                }
            });
    }
     
    void setupGestureTarget(final HBox targetBox){
          
        targetBox.setOnDragOver(new EventHandler <DragEvent>() {
            @Override
            public void handle(DragEvent event) {
  
                Dragboard db = event.getDragboard();
                  
                if(db.hasImage()){
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                  
                event.consume();
            }
        });
  
        targetBox.setOnDragDropped(new EventHandler <DragEvent>(){
            @Override
            public void handle(DragEvent event) {
   
                Dragboard db = event.getDragboard();
  
                if(db.hasImage()){
  
                    iv.setImage(db.getImage());
 
                    Point2D localPoint = targetBox.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY()));
 
//                    System.out.println("event.getSceneX : "+event.getSceneX());
//                    System.out.println("localPoint.getX : "+localPoint.getX());
//                    System.out.println("********");
  
                    targetBox.getChildren().remove(iv);
 
                    iv.setX((int)(localPoint.getX() - iv.getBoundsInLocal().getWidth()  / 2)  );
                    iv.setY((int)(localPoint.getY() - iv.getBoundsInLocal().getHeight() / 2) );
 
                    targetBox.getChildren().add(iv);
                     
                    if(curseur < 400 && event.getSceneX() < 400){
                       nombreLeft = nombreLeft+0;  
                    }else if(curseur < 400 && event.getSceneX() > 400){
                       nombreLeft--;
                       nombreRight++;
                       actualiser();
                    }else if(curseur > 400 && event.getSceneX() > 400){
                        nombreRight = nombreRight +0; 
                    }else if(curseur > 400 && event.getSceneX() < 400){
                       nombreLeft++;
                       nombreRight--;
                       actualiser();
                    }   
                    event.setDropCompleted(true);
                }else{
                    event.setDropCompleted(false);
                }
  
                event.consume();
            }
        });
          
    }
     
    public void actualiser(){
         
        text1.setText("Ballon a gauche : "+ nombreLeft);
        text2.setText("Ballon a droite : "+ nombreRight);
        System.out.println(nombreLeft+ " " + nombreRight);
        System.out.println(text1.getText()+ " " + text2.getText());
        root.getChildren().remove(text1);
        root.getChildren().remove(text2);
        root.getChildren().add(text1);
        root.getChildren().add(text2);
    }
     
     
 
}
