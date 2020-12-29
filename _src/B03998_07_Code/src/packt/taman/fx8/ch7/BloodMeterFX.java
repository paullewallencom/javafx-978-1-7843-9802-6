package packt.taman.fx8.ch7;

import static java.lang.Float.parseFloat;
import static java.lang.System.currentTimeMillis;
import java.text.SimpleDateFormat;
import java.util.Date;
import javafx.application.Application;
import static javafx.application.Platform.runLater;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import static javafx.scene.chart.XYChart.Data;
import static javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.ToolBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import packt.taman.fx8.ch7.serial.Serial;

/**
 * Packt Book: JavaFX 8 essentials 
 * Chapter 7. Monitoring & Controlling Arduino with JavaFX
 * @author Mohamed Taman
 */
public class BloodMeterFX extends Application {

    /* Check in the Arduino IDE the port used by Arduino or leave it 
     * empty if you don't know it: */
    private final Serial serial = new Serial();
    private ChangeListener<String> listener;

    private final BooleanProperty connection = new SimpleBooleanProperty(false);
    private final FloatProperty bloodTemp = new SimpleFloatProperty(0);
    private final FloatProperty volts = new SimpleFloatProperty(0);
    private final FloatProperty sensorVal = new SimpleFloatProperty(0);

    private final float baselineTemp = 25.0f;

    @Override
    public void start(Stage stage) throws Exception {

        Scene scene = new Scene(loadMainUI(), 660, 510);
        stage.setTitle("Blood Meter v1.5");
        stage.setScene(scene);
        stage.show();

        //Connect to Arduino port and start listening
        connectArduino();
    }

    @Override
    public void stop() {
        System.out.println("Serial port is closing now...");
        serial.getLine().removeListener(listener);
        if (connection.get()) {
            serial.disconnect();
            connection.set(false);
        }
    }

    public static void main(String[] args) {

        launch(args);
    }

    private LineChart<Number, Number> createBloodChart() {
        final NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Temperature Time");
        xAxis.setAutoRanging(true);
        xAxis.setForceZeroInRange(false);
        xAxis.setTickLabelFormatter(new StringConverter<Number>() {
            @Override
            public String toString(Number t) {
                return new SimpleDateFormat("HH:mm:ss").format(new Date(t.longValue()));
            }

            @Override
            public Number fromString(String string) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        });

        final NumberAxis yAxis = new NumberAxis("Temperature value", baselineTemp - 10, 40.0, 10);
        final LineChart<Number, Number> bc
                = new LineChart<>(xAxis, yAxis);

        bc.setTitle("Blood temperature vs time");
        bc.setLegendVisible(false);

        Series series = new Series();
        series.getData().add(new Data(currentTimeMillis(), baselineTemp));
        bc.getData().add(series);

        listener = (ov, t, t1) -> {
            runLater(() -> {
                String[] values = t1.split(",");
                if (values.length == 3) {
                    sensorVal.set(parseFloat(values[0].split(":")[1].trim()));
                    volts.set(parseFloat(values[1].split(":")[1].trim()));
                    bloodTemp.set(parseFloat(values[2].split(":")[1].trim()));

                    series.getData().add(new Data(currentTimeMillis(),
                            bloodTemp.getValue()));

                    if (series.getData().size() > 40) {
                        series.getData().remove(0);
                    }
                }

            });
        };
        serial.getLine().addListener(listener);

        return bc;
    }

    private void connectArduino() {
        //Connect to Arduino port and start listining
        serial.connect();
        connection.set(!serial.getPortName().isEmpty());
    }

    private BorderPane loadMainUI() {
        // Create main layout handler & add chart to its center position
        BorderPane root = new BorderPane(createBloodChart());
        // Create a connection status label
        Label conStatusLbl = new Label("Not Connected");
        // Add label to bottom of the root
        root.setBottom(conStatusLbl);

        // Create top root layout content
        HBox topHB = new HBox(10);
        topHB.setAlignment(Pos.CENTER);
        topHB.setPrefHeight(65);
        // Create application logo
        ImageView logo = new ImageView("/packt/taman/fx8/ch7/assets/Meter.png");
        logo.fitHeightProperty().set(60.0);
        logo.fitWidthProperty().set(40);
        //Create application title
        Label appTitle = new Label("Blood Temperature Meter v1.5");
        appTitle.setFont(new Font(20.0));
        //Add label and logo to top its container
        topHB.getChildren().addAll(logo, appTitle);

        // Add label and logo to the top of the root
        root.setTop(topHB);

        // Create right root layout content
        VBox rightVB = new VBox(10);
        rightVB.setAlignment(Pos.TOP_CENTER);
        rightVB.setPrefWidth(100);

        Label indicator = new Label("Blood Level Indicator");
        indicator.setAlignment(Pos.CENTER);
        indicator.setPrefHeight(39.0);
        indicator.setPrefWidth(96.0);
        indicator.setWrapText(true);
        indicator.setTextAlignment(TextAlignment.CENTER);

        Separator sep = new Separator(Orientation.HORIZONTAL);
        sep.setPrefHeight(8.0);
        sep.setPrefWidth(56.0);

        // Creating indicators
        Circle IndictorLevel1 = new Circle(26.0, Paint.valueOf("Black"));
        Circle IndictorLevel2 = new Circle(26.0, Paint.valueOf("Black"));
        Circle IndictorLevel3 = new Circle(26.0, Paint.valueOf("Black"));

        rightVB.getChildren().addAll(indicator,sep,IndictorLevel3,
                                     IndictorLevel2, IndictorLevel1);
        
        // Add label and logo to the top of the root
        root.setRight(rightVB);

        // Create bottom root layout content
        ToolBar infoTB = new ToolBar();
        infoTB.setPrefHeight(30);

        //Create information labels
        Label connStatusLbl = new Label("Not connected!");
        Separator sep1 = new Separator(Orientation.VERTICAL);
        sep1.setPrefWidth(4.0);
        Separator sep2 = new Separator(Orientation.VERTICAL);
        sep2.setPrefWidth(4.0);
        Separator sep3 = new Separator(Orientation.VERTICAL);
        sep3.setPrefWidth(4.0);

        connection.addListener((ol, ov, nv) -> connStatusLbl.setText(nv
                ? "Connected on port "
                .concat(serial.getPortName())
                : "Not connected!"));

        Label sensorValueLbl = new Label("Sensor Value: ");
        Label vlotsLbl = new Label("Volts: ");
        Label tempLbl = new Label("Degrees C: ");

        // Adding Listeners
        sensorVal.addListener((ol, ov, nv)
                -> sensorValueLbl.setText("Sensor Value: ".concat(nv.toString())));
        volts.addListener((ol, ov, nv)
                -> vlotsLbl.setText("Volts: ".concat(nv.toString())));
        
        //Add labels to bottom container
        infoTB.getItems().addAll(connStatusLbl,sep1,sensorValueLbl,sep2,vlotsLbl,sep3,tempLbl);
        infoTB.setStyle("-fx-background-color: lightgray");
        // Add info toolbar to the bottom of the root
        root.setBottom(infoTB);

        /*
         Listen to any change for tempreature to change 
         indicator fill and show tempreature
         */
        bloodTemp.addListener((ol, ov, nv) -> {

            tempLbl.setText("Degrees C: ".concat(nv.toString()));

            // if the current temperature is lower than the baseline turn off all LEDs
            if (nv.floatValue() < baselineTemp + 2) {
                IndictorLevel1.setFill(Paint.valueOf("Black"));
                IndictorLevel2.setFill(Paint.valueOf("Black"));
                IndictorLevel3.setFill(Paint.valueOf("Black"));
               
            } // if the temperature rises 1-3 degrees, turn an LED on
            else if (nv.floatValue() >= baselineTemp + 1 && nv.floatValue() < baselineTemp + 3) {
                IndictorLevel1.setFill(Paint.valueOf("RED"));
                IndictorLevel2.setFill(Paint.valueOf("Black"));
                IndictorLevel3.setFill(Paint.valueOf("Black"));
               
            } // if the temperature rises 3-5 degrees, turn a second LED on
            else if (nv.floatValue() >= baselineTemp + 3 && nv.floatValue() < baselineTemp + 5) {
                IndictorLevel1.setFill(Paint.valueOf("RED"));
                IndictorLevel2.setFill(Paint.valueOf("RED"));
                IndictorLevel3.setFill(Paint.valueOf("Black"));

            } // if the temperature rises more than 6 degrees, turn all LEDs on
            else if (nv.floatValue() >= baselineTemp + 6) {
                IndictorLevel1.setFill(Paint.valueOf("RED"));
                IndictorLevel2.setFill(Paint.valueOf("RED"));
                IndictorLevel3.setFill(Paint.valueOf("RED"));   
            }
        });

        return root;
    }
}
