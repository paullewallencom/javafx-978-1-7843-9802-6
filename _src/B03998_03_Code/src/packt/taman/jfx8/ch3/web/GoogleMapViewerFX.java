package packt.taman.jfx8.ch3.web;

import java.net.URL;
import javafx.application.Application;
import static javafx.geometry.Pos.CENTER;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 3. Developing a JavaFX desktop and web applications
 * @author Mohamed Taman
 */
public class GoogleMapViewerFX extends Application {
    
    private MyBrowser myBrowser;
 
 
    @Override
    public void start(Stage stage) throws Exception {
        
        myBrowser = new MyBrowser();

        stage.setScene(new Scene(myBrowser));
        stage.setWidth(800);
        stage.setHeight(650);
        stage.show();
    }
 
    class MyBrowser extends Pane {
 
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();
 
        public MyBrowser() {
 
            webEngine.load(getClass().getResource("map.html").toExternalForm());
            
            webEngine.setOnAlert(e -> {
                System.out.println(e.toString());
            });
 
            final TextField latitude = new TextField("" + 29.8770037);
            final TextField longitude = new TextField("" + 31.3154412);
            
            Button update = new Button("Update");
            
            update.setOnAction(evt -> {
                double lat = Double.parseDouble(latitude.getText());
                double lon = Double.parseDouble(longitude.getText());
                
                webEngine.executeScript("" +
                        "window.lat = " + lat + ";" +
                        "window.lon = " + lon + ";" +
                        "document.goToLocation(window.lat, window.lon);"
                );
            });
 
            HBox toolbar  = new HBox();
            toolbar.setSpacing(5);
            toolbar.setAlignment(CENTER);
            toolbar.getChildren().addAll(new Label("Latitude: "),latitude,new Label("Longitude: "), longitude, update);
 
            VBox content  = new VBox();
            content.getChildren().addAll(toolbar,webView);
            getChildren().add(content);
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
