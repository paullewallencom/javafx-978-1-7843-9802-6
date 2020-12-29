package packt.taman.jfx8.ch4;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 4,5. Developing a JavaFX application for Android, iOS
 * @author Mohamed Taman
 */
public class DialPad2 extends Application {

    @Override
    public void start(Stage stage) {
        BorderPane root = new BorderPane();
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        Scene scene = new Scene(root, bounds.getWidth(), bounds.getHeight());
        scene.getStylesheets().add(getClass().getResource("ui/Mobile_UI."+PlatformFactory.getName()+".css").toExternalForm());
        TextField output = new TextField("");
        output.setDisable(true);

        root.setTop(output);
        String[] keys = {"1", "2", "3",
                         "4", "5", "6",
                         "7", "8", "9",
                         "*", "0", "#"};

        GridPane numPad = new GridPane();
        numPad.setAlignment(Pos.CENTER);
        numPad.getStyleClass().add("num-pad");
        for (int i = 0; i < keys.length; i++) {
            Button button = new Button(keys[i]);
            button.getStyleClass().add("dial-num-btn");
            button.setOnAction(e -> output.setText(output.getText().
                    concat(Button.class.
                            cast(e.getSource()).getText())));
            numPad.add(button, i % 3, (int) Math.ceil(i / 3));
        }
        // Call button
        Button call = new Button("Call");
        call.setOnAction(e->PlatformFactory.getPlatform().callNumber(output.getText()));
        call.setId("call-btn");
        call.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        numPad.add(call, 0, 4);
        GridPane.setColumnSpan(call, 3);
        GridPane.setHgrow(call, Priority.ALWAYS);
        root.setCenter(numPad);
        
        //Stage setup
        stage.setScene(scene);
        stage.setTitle("Phone Dial v2.0");
        stage.show();
    }

}
