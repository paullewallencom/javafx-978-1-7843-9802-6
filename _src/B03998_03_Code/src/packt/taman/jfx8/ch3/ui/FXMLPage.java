package packt.taman.jfx8.ch3.ui;

import java.net.URL;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 3. Developing a JavaFX desktop and web application
 * @author Mohamed Taman
 */
public enum FXMLPage {

    LIST("/packt/taman/jfx8/ch3/ui/fxml/ListNotesUI.fxml"),
    ADD("/packt/taman/jfx8/ch3/ui/fxml/AddEditUI.fxml");

    private final String location;

    FXMLPage(String location) {
        this.location = location;
    }

    public URL getPage() {
        return  getClass().getResource(location) ;
       
    }

}
