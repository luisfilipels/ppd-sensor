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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import networking.NetworkHandlerSingleton;
import utils.SensorSingleton;
import utils.SensorType;

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

    @FXML
    private TextField brokerAddressField;

    @FXML
    private Text emptyIDText;

    @FXML
    private Text invalidLimitsText;

    @FXML
    private Text invalidIPText;

    @FXML
    private Text invalidMinimumLimitText;

    @FXML
    private Text invalidMaximumLimitText;

    private static final String PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    // Got this function from StackOverFlow
    // Checks if the string is a valid IP, such as 192.168.0.1
    public static boolean isValid(final String ip){
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
        initialMaxValueField.setText("100");
        initialMinValueField.setText("0");
        emptyIDText.setOpacity(0);
        invalidLimitsText.setOpacity(0);
        invalidIPText.setOpacity(0);
        invalidMaximumLimitText.setOpacity(0);
        invalidMinimumLimitText.setOpacity(0);
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

    private boolean isValidIP() {
        String ip = brokerAddressField.getText().trim();
        if (!ip.equals("") && !isValid(ip)) {
            invalidIPText.setOpacity(1);
            return false;
        } else {
            invalidIPText.setOpacity(0);
            return true;
        }
    }

    private boolean isValidSensorID() {
        String id = sensorIDField.getText();
        if (id.equals("")) {
            emptyIDText.setOpacity(1);
            return false;
        } else {
            emptyIDText.setOpacity(0);
            return true;
        }
    }

    private boolean limitsAreValid() {
        boolean successfulConversion = true;
        int initialMin = 0;
        int initialMax = 100;
        try {
            initialMin = Integer.parseInt(initialMinValueField.getText());
        } catch (NumberFormatException e) {
            // A non-numerical value was inserted
            invalidMinimumLimitText.setText("Valor inválido!");
            invalidMinimumLimitText.setOpacity(1);
            successfulConversion = false;
        }
        try {
            initialMax = Integer.parseInt(initialMaxValueField.getText());
        } catch (NumberFormatException e) {
            invalidMaximumLimitText.setText("Valor inválido!");
            invalidMaximumLimitText.setOpacity(1);
            successfulConversion = false;
        }
        if (!successfulConversion) {
            // Some non-numerical value was inserted as a limit
            // as such, the error message that says the minimum is greater
            // than the maximum should be hidden
            invalidLimitsText.setOpacity(0);
            return false;
        }
        invalidMaximumLimitText.setOpacity(0);
        invalidMinimumLimitText.setOpacity(0);  // The limits are valid

        if (initialMax <= initialMin) {
            invalidLimitsText.setOpacity(1);
            return false;
        }
        return true;
    }

    private boolean areValidReadValues() {
        boolean isValidIP = isValidIP();
        boolean isValidSensorID = isValidSensorID();
        boolean limitsAreValid = limitsAreValid();
        return isValidIP && isValidSensorID && limitsAreValid;
    }

    private void closeWindow(ActionEvent event) {
        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void openMainWindow() {
        try {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("main.fxml"));
            primaryStage.setTitle("Sensor de " + type.label + " " + sensorIDField.getText());
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onConfirmButton(ActionEvent event) {
        if (!areValidReadValues()) {
            return;
        }

        String ip = brokerAddressField.getText().trim();
        String sensorId = sensorIDField.getText();
        int initialMin = Integer.parseInt(initialMinValueField.getText());
        int initialMax = Integer.parseInt(initialMaxValueField.getText());

        SensorSingleton sensor = SensorSingleton.getInstance();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                sensor.setMaxReadingLimit(initialMax);
                sensor.setMinReadingLimit(initialMin);
                sensor.setNewReading((initialMax + initialMin)/2);
                sensor.setSensorType(type);
                sensor.setSensorID(sensorId);
                sensor.setBrokerIP(ip);

                NetworkHandlerSingleton.getInstance().initialize();
            }
        });

        closeWindow(event);
        openMainWindow();
    }

}
