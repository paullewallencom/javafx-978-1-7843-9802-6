package packt.taman.fx8.ch6;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import static com.pi4j.io.gpio.RaspiPin.GPIO_01;
import com.pi4j.system.SystemInfo;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import static javafx.scene.paint.Color.BLACK;
import static javafx.scene.paint.Color.RED;
import javafx.scene.shape.Circle;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 6. Running JavaFX application on the Raspberry Pi
 * @author Mohamed Taman
 */
public class SwitchUIController implements Initializable {

    private GpioController gpio;
    private GpioPinDigitalOutput pin;
      
    @FXML
    private ToggleButton switchTgl;

     @FXML
    private Label boardTypeLbl;
     
    @FXML
    private Label ProcessorLbl;

    @FXML
    private Label CPUModelNameLbl;

    @FXML
    private Label HardwareRevisionLbl;

    @FXML
    private Label OSNameLbl;

    @FXML
    private Label JavaVersionLbl;

    @FXML
    private Circle led;

    @FXML
    private void doExit(ActionEvent event) {
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        gpio.shutdown();
        Platform.exit();
    }

    @FXML
    private void doOnOff(ActionEvent event) {

        if (switchTgl.isSelected()) {
            pin.high();
            led.setFill(RED);
            switchTgl.setText("OFF");
            System.out.println("Switch is On");
        } else {
            pin.low();
            led.setFill(BLACK);
            switchTgl.setText("ON");
            System.out.println("Switch is Off");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            // create gpio controller instance
            gpio = GpioFactory.getInstance();

            // provision gpio pin #01 as an output pin and turn on
            pin = gpio.provisionDigitalOutputPin(GPIO_01);
        
            boardTypeLbl.setText(SystemInfo.BoardType.ModelB_Plus_Rev1.name());
            ProcessorLbl.setText(SystemInfo.getProcessor());
            CPUModelNameLbl.setText(SystemInfo.getModelName());
            HardwareRevisionLbl.setText(SystemInfo.getHardware());
            OSNameLbl.setText(SystemInfo.getOsName());
            JavaVersionLbl.setText(SystemInfo.getJavaVersion());
            
//        boardTypeLbl.setText("Model B+");    
//        ProcessorLbl.setText("ARMv6-compatible processor rev 7 (v6l)");
//            CPUModelNameLbl.setText("7");
//            HardwareRevisionLbl.setText("2");
//            OSNameLbl.setText("Raspian Wheezy");
//            JavaVersionLbl.setText("1.8.0-b132");
        } catch (IOException | InterruptedException ex) {
            Logger.getLogger(SwitchUIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
