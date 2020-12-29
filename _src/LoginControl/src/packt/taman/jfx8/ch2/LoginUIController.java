package packt.taman.jfx8.ch2;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 2. Creating custom UI and some essentials
 * @author Mohamed Taman
 */
public class LoginUIController extends Pane {

    public LoginUIController() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("LoginUI.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
            fxmlLoader.load(); 
    }

    @FXML
    private Button resetBtn;

    @FXML
    private Button loginBtn;

    @FXML 
    private TextField usernameTxt;

    @FXML 
    private CheckBox rememberMeChk;

    @FXML 
    private Label errorLbl;

    @FXML 
    private PasswordField passwordTxt;

    @FXML
    void doReset(ActionEvent event) {
        usernameTxt.setText("");
        passwordTxt.setText("");
        errorLbl.setText("");
        rememberMeChk.setSelected(true);
    }

    @FXML
    void doLogin(ActionEvent event) {

        if (usernameTxt.getText().equalsIgnoreCase("tamanm")
                && passwordTxt.getText().equals("Tamanm")) {
            errorLbl.setText("Valid Credintials");
            errorLbl.setTextFill(Paint.valueOf("Green"));
        } else {
            errorLbl.setText("Invalid Credintials, try again!!");
            errorLbl.setTextFill(Paint.valueOf("Red"));
        }
    }
    
    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert resetBtn != null : "fx:id=\"resetBtn\" was not injected: check your FXML file 'LoginUI.fxml'.";
        assert loginBtn != null : "fx:id=\"loginBtn\" was not injected: check your FXML file 'LoginUI.fxml'.";
        assert usernameTxt != null : "fx:id=\"usernameTxt\" was not injected: check your FXML file 'LoginUI.fxml'.";
        assert rememberMeChk != null : "fx:id=\"rememberMeChk\" was not injected: check your FXML file 'LoginUI.fxml'.";
        assert errorLbl != null : "fx:id=\"errorLbl\" was not injected: check your FXML file 'LoginUI.fxml'.";
        assert passwordTxt != null : "fx:id=\"passwordTxt\" was not injected: check your FXML file 'LoginUI.fxml'.";
        
        rememberMeChk.setSelected(true);

    }
}
