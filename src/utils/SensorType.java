package utils;

public enum SensorType {
    TEMPERATURE("TEMPERATURE"), HUMIDITY("HUMIDITY"), VELOCITY("VELOCITY");

    public final String label;

    private SensorType(String label) {
        this.label = label;
    }
}
