package packt.taman.jfx8.ch2;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 2. Creating custom UI and some essentials
 * @author Mohamed Taman
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        LoginUIController loginPane = new LoginUIController();

        stage.setScene(new Scene(loginPane));
        stage.setTitle("Login Dialog Control");
        stage.setWidth(500);
        stage.setHeight(220);
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
