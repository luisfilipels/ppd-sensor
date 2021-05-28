package main;

import javafx.application.Platform;
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
    private Text invalidReadingText;
    @FXML
    private Text invalidMinimumText;
    @FXML
    private Text invalidMaximumText;
    @FXML
    private Text invalidMinMaxText;

    @FXML
    void initialize() {
        invalidReadingText.setOpacity(0);
        invalidMinimumText.setOpacity(0);
        invalidMaximumText.setOpacity(0);
        invalidMinMaxText.setOpacity(0);
        refreshView();
    }

    public void refreshView() {
        var sensorData = SensorSingleton.getInstance();
        if (sensorData.getCurrentReading() == null) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    refreshView();
                }
            });
        } else {
            readingExhibit.setText(
                    sensorData.getCurrentReading() + sensorData.getSensorType().measurementUnit
            );
            minExhibit.setText(Integer.toString(sensorData.getMinReadingLimit()));
            maxExhibit.setText(Integer.toString(sensorData.getMaxReadingLimit()));
        }
    }

    @FXML
    private void handleNewReadingButton() {
        SensorSingleton sensorData = SensorSingleton.getInstance();
        int newReading = 0;
        try {
            newReading = Integer.parseInt(setReadingField.getText());
        } catch (Exception e) {
            invalidReadingText.setOpacity(1);
            System.out.println("Couldn't get new reading!");
            return;
        }
        boolean success = sensorData.setNewReading(newReading);
        if (success) {
            setReadingField.clear();
            invalidReadingText.setOpacity(0);
            refreshView();
        }
    }

    @FXML
    private void handleNewMaxValueButton() {
        SensorSingleton sensorData = SensorSingleton.getInstance();
        int newMax = 0;
        try {
            newMax = Integer.parseInt(setMaxValueField.getText());
        } catch (Exception e) {
            invalidMaximumText.setOpacity(1);
            System.out.println("Couldn't set new max value!");
            return;
        }
        boolean success = sensorData.setMaxReadingLimit(newMax);
        if (success) {
            setMaxValueField.clear();
            invalidMinMaxText.setOpacity(0);
            invalidMaximumText.setOpacity(0);
            refreshView();
        } else {
            invalidMinMaxText.setOpacity(1);
        }
    }

    @FXML
    private void handleNewMinValueButton() {
        SensorSingleton sensorData = SensorSingleton.getInstance();
        int newMin = 0;
        try {
            newMin = Integer.parseInt(setMinValueField.getText());
        } catch (Exception e) {
            invalidMinimumText.setOpacity(1);
            System.out.println("Couldn't new min value!");
            return;
        }
        boolean success = sensorData.setMinReadingLimit(newMin);
        if (success) {
            setMinValueField.clear();
            invalidMinMaxText.setOpacity(0);
            invalidMinimumText.setOpacity(0);
            refreshView();
        } else {
            invalidMinMaxText.setOpacity(1);
        }
    }


}
