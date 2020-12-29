package packt.taman.jfx8.ch3.controllers.ui;

import java.io.IOException;
import java.net.URL;
import static java.util.Objects.nonNull;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.binding.Bindings;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import static javafx.scene.control.Alert.AlertType.CONFIRMATION;
import javafx.scene.control.ButtonType;
import static javafx.scene.control.ButtonType.OK;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import packt.taman.jfx8.ch3.controllers.BaseController;
import packt.taman.jfx8.ch3.model.Note;
import static packt.taman.jfx8.ch3.ui.FXMLPage.ADD;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 3. Developing a JavaFX desktop and web applications
 *
 * @author Mohamed Taman
 */
public class ListNotesUIController extends BaseController implements Initializable {

    @FXML
    private TableView<Note> notesListTable;

    @FXML
    private TextField searchNotes;

    @FXML
    private Label notesCount;

    @FXML
    private TableColumn titleTc;

    @FXML
    private TableColumn descriptionTc;

    @FXML
    private void newNote(ActionEvent event) throws IOException {
        editNote = null;
        navigate(event, ADD.getPage());
    }

    @FXML
    private void doDelete(ActionEvent event) throws IOException {

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get().equals(OK)) {
            // user chose OK
            data.remove(getItem());
        }
    }

    @FXML
    private void doEdit(ActionEvent event) throws IOException {

        editNote = null;
        editNote = getItem();
        if (nonNull(editNote)) {
            navigate(event, ADD.getPage());
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        titleTc.setCellValueFactory(
                new PropertyValueFactory<>("title"));

        descriptionTc.setCellValueFactory(
                new PropertyValueFactory<>("description"));

        //Settings notes count
        notesCount.textProperty().bind(Bindings.createStringBinding(() -> data.size() + " Note(s)", data));

        //Initializing the alert box
        alert = new Alert(CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText("Look, a delete confirmation");
        alert.setContentText("Are you ok with deleting this note?");

        /*
         * Attach a KeyReleased action to searchNotes field, so when typing to 
         * filter the notes currently attached to the table view.
         */
        FilteredList<Note> filteredData = new FilteredList<>(data, n -> true);
        searchNotes.setOnKeyReleased(e -> {
            filteredData.setPredicate(n -> {
                
                if (searchNotes.getText() == null || searchNotes.getText().isEmpty()) {
                    return true;
                }
                
                return n.getTitle().contains(searchNotes.getText())
                        || n.getDescription().contains(searchNotes.getText());
            });
        });

        notesListTable.setItems(filteredData);

    }

    private Note getItem() {
        return Note.class.cast(notesListTable.getSelectionModel().getSelectedItem());
    }

}
