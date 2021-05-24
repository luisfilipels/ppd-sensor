package readData;

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
import utils.SensorDataSingleton;
import utils.SensorType;
import utils.SessionDataSingleton;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

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
    public void handleConfirmButton (ActionEvent event) {
        // TODO: Select server IP
        // TODO: Validate values

        int initialMax = Integer.parseInt(initialMaxValueField.getText());
        int initialMin = Integer.parseInt(initialMinValueField.getText());

        SensorDataSingleton sensor = SensorDataSingleton.getInstance();
        sensor.setMaxReading(initialMax);
        sensor.setMinReading(initialMin);
        sensor.setNewReading((initialMax + initialMin)/2);
        sensor.setSensorType(type);

        NetworkHandlerSingleton.getInstance().initialize();

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();

        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("main.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Stage mainStage = new Stage();
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("main.fxml")
        );
        // TODO: Fix this
        Parent main = loader.load();
        mainStage.setTitle("Sensor");
        mainStage.setScene(new Scene(root, 600, 400));
        mainStage.setResizable(false);
        mainStage.show();
    }

}
