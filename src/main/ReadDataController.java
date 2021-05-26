package main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import networking.NetworkHandlerSingleton;
import utils.SensorSingleton;
import utils.SensorType;

import java.io.IOException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadDataController {

    private SensorType type;

    @FXML
    private Button confirmButton;

    @FXML
    private RadioButton temperatureButton;

    @FXML
    private RadioButton velocityButton;

    @FXML
    private RadioButton humidityButton;

    @FXML
    private TextField initialMaxValueField;

    @FXML
    private TextField initialMinValueField;

    @FXML
    private Button randomIDButton;

    @FXML
    private TextField sensorIDField;

    private static final String PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    // Got this function from StackOverFlow
    public static boolean validate(final String ip){
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    // Got this function from Baeldung
    public String generateRandomID() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedString;
    }

    @FXML
    public void onRandomIDButton(ActionEvent event) {
        sensorIDField.setText(generateRandomID());
    }

    @FXML
    public void initialize() {
        temperatureButton.setSelected(true);
        type = SensorType.TEMPERATURE;
    }


    @FXML
    public void onRadioButtonClick(ActionEvent event) {
        RadioButton pressedButton = (RadioButton) event.getSource();
        pressedButton.setSelected(true);
        if (pressedButton == temperatureButton) {
            type = SensorType.TEMPERATURE;
            velocityButton.setSelected(false);
            humidityButton.setSelected(false);
        } else if (pressedButton == velocityButton) {
            type = SensorType.VELOCITY;
            temperatureButton.setSelected(false);
            humidityButton.setSelected(false);
        } else {
            type = SensorType.HUMIDITY;
            velocityButton.setSelected(false);
            temperatureButton.setSelected(false);
        }
    }

    @FXML
    public void onConfirmButton(ActionEvent event) {
        // TODO: Select server IP
        // TODO: Validate values

        int initialMax = Integer.parseInt(initialMaxValueField.getText());
        int initialMin = Integer.parseInt(initialMinValueField.getText());

        SensorSingleton sensor = SensorSingleton.getInstance();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                sensor.setMaxReading(initialMax);
                sensor.setMinReading(initialMin);
                sensor.setNewReading((initialMax + initialMin)/2);
                sensor.setSensorType(type);
                sensor.setSensorID(sensorIDField.getText());

                NetworkHandlerSingleton.getInstance().initialize();
            }
        });

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            primaryStage.setTitle("Sensor");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
