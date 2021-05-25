package main;

import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import utils.SensorSingleton;

public class MainViewController {

    @FXML
    private Text readingExhibit;
    @FXML
    private Text maxExhibit;
    @FXML
    private Text minExhibit;

    @FXML
    private TextField setReadingField;
    @FXML
    private Button confirmNewReading;

    @FXML
    private TextField setMaxValueField;
    @FXML
    private Button confirmNewMaxValue;

    @FXML
    private TextField setMinValueField;
    @FXML
    private Button confirmNewMinValue;

    @FXML
    void initialize() {
        refreshView();
    }

    public void refreshView() {
        var sensorData = SensorSingleton.getInstance();
        readingExhibit.setText(Integer.toString(sensorData.getCurrentReading()));
        minExhibit.setText(Integer.toString(sensorData.getMinReading()));
        maxExhibit.setText(Integer.toString(sensorData.getMaxReading()));
    }

    @FXML
    private void handleNewReadingButton() {
        SensorSingleton sensorData = SensorSingleton.getInstance();
        int newReading = 0;
        try {
            newReading = Integer.parseInt(setReadingField.getText());
        } catch (Exception e) {
            // TODO: Show message on UI
            System.out.println("Couldn't get new reading!");
            return;
        }
        boolean success = sensorData.setNewReading(newReading);
        if (success) {
            refreshView();
        } else {
            // TODO: Show message on UI
        }
    }

    @FXML
    private void handleNewMaxValueButton() {
        SensorSingleton sensorData = SensorSingleton.getInstance();
        int newMax = 0;
        try {
            newMax = Integer.parseInt(setMaxValueField.getText());
        } catch (Exception e) {
            // TODO: Show message on UI
            System.out.println("Couldn't set new max value!");
            return;
        }
        boolean success = sensorData.setMaxReading(newMax);
        if (success) {
            refreshView();
        } else {
            // TODO: Show message on UI
        }
    }

    @FXML
    private void handleNewMinValueButton() {
        SensorSingleton sensorData = SensorSingleton.getInstance();
        int newMin = 0;
        try {
            newMin = Integer.parseInt(setMinValueField.getText());
        } catch (Exception e) {
            // TODO: Show message on UI
            System.out.println("Couldn't new min value!");
            return;
        }
        boolean success = sensorData.setMinReading(newMin);
        if (success) {
            refreshView();
        } else {
            // TODO: Show message on UI
        }
    }


}
