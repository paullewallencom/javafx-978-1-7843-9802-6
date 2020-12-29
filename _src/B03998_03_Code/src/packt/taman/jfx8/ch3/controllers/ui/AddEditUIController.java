package packt.taman.jfx8.ch3.controllers.ui;

import java.io.IOException;
import java.net.URL;
import static java.util.Objects.nonNull;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.WARNING;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import packt.taman.jfx8.ch3.controllers.BaseController;
import packt.taman.jfx8.ch3.model.Note;
import static packt.taman.jfx8.ch3.ui.FXMLPage.LIST;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 3. Developing a JavaFX desktop and web applications
 * @author Mohamed Taman
 */
public class AddEditUIController extends BaseController implements Initializable {

    @FXML
    private TextField titleTxtFld;

    @FXML
    private TextArea descriptionTxtArea;

    @FXML
    private Button saveBtn;

    @FXML
    private void doBack(ActionEvent event) throws IOException {

        navigate(event, LIST.getPage());
    }

    @FXML
    private void doSave(ActionEvent event) throws IOException {
        
        if (nonNull(editNote)) 
            data.remove(editNote);
        
        if (titleTxtFld.getText().trim().equals("")
                || descriptionTxtArea.getText().trim().equals("")) {
            alert.showAndWait();
            return;
        }

        data.add(new Note(titleTxtFld.getText(), descriptionTxtArea.getText()));
        navigate(event, LIST.getPage());
    }

    @FXML
    private void doClear(ActionEvent event) throws IOException {

        titleTxtFld.clear();
        descriptionTxtArea.clear();
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        alert = new Alert(WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Invalid data to save or update!");
        alert.setContentText("Note title or description can not be empty!");

        if (nonNull(editNote)) {
            titleTxtFld.setText(editNote.getTitle());
            descriptionTxtArea.setText(editNote.getDescription());
            saveBtn.setText("Update");
        }
    }
}