package packt.taman.jfx8.ch1;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import static javafx.geometry.Pos.CENTER;
import javafx.scene.layout.VBox;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 1. Getting started with JavaFX 8
 * @author Mohamed Taman
 */
public class HelloJavaFX extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        Button btn = new Button();
        Text message = new Text();
        
        btn.setText("Say 'Hello World'");
        
        btn.setOnAction(event -> {
            message.setText("Hello World! JavaFX style :)");
        });
        
        VBox root = new VBox(10,btn,message);
        root.setAlignment(CENTER);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello JavaFX 8 World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
