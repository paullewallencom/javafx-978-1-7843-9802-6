package packt.taman.jfx8.ch3.controllers;

import java.io.IOException;
import java.net.URL;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import packt.taman.jfx8.ch3.model.Note;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 3. Developing a JavaFX desktop and web applications
 * @author Mohamed Taman
 */
public class BaseController {

    protected Alert alert;
    
    protected static ObservableList<Note> data = FXCollections.<Note>observableArrayList(
            new Note("Note 1", "Description of note 41"),
            new Note("Note 2", "Description of note 32"),
            new Note("Note 3", "Description of note 23"),
            new Note("Note 4", "Description of note 14"));

    protected static Note editNote = null;

    /**
     * method used for main system navigation
     *
     * @param event
     * @param fxmlDocName
     * @throws IOException
     */
    protected void navigate(Event event, URL fxmlDocName) throws IOException {
        //Loading new fxml UI document
        Parent pageParent = FXMLLoader.load(fxmlDocName);
        //Creating new scene
        Scene scene = new Scene(pageParent);
        //get current stage
        Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        //Hide old stage
        appStage.hide(); // Optional
        //Set stage with new Scene
        appStage.setScene(scene);
        //Show up the stage
        appStage.show();
    }

}
