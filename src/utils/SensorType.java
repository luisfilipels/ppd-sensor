package utils;

public enum SensorType {
    TEMPERATURE("TEMPERATURE", "°C", "temperatura"),
    HUMIDITY("HUMIDITY", "g/m³", "umidade"),
    VELOCITY("VELOCITY", "m/s", "velocidade");

    public final String topicName;
    public final String measurementUnit;
    public final String label;

    private SensorType(String topicName, String measurementUnit, String label) {
        this.measurementUnit = measurementUnit;
        this.topicName = topicName;
        this.label = label;
    }
}
