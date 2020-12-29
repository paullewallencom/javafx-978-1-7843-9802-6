package packt.taman.jfx8.ch3.model;

import javafx.beans.property.SimpleStringProperty;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 3. Developing a JavaFX desktop and web applications
 * @author Mohamed Taman
 */
public class Note {

    private final SimpleStringProperty title;
    private final SimpleStringProperty description;

    public Note(String title, String description) {
        this.title = new SimpleStringProperty(title);
        this.description = new SimpleStringProperty(description);

    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    public String toString() {
        return "Note{" + "title=" + title + ", description=" + description + '}';
    }
    
}
