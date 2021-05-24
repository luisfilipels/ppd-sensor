package utils;

public class SensorDataSingleton {

    private static SensorDataSingleton instance;

    private SensorDataSingleton() {}

    public static SensorDataSingleton getInstance() {
        if (instance == null) {
            instance = new SensorDataSingleton();
        }
        return instance;
    }

    private SensorType sensorType = null;

    private Integer maxReading = null;
    private Integer minReading = null;
    private Integer currentReading = null;

    public boolean setMaxReading(int newMax) {
        if (minReading != null && newMax <= minReading) return false;
        maxReading = newMax;
        return true;
    }

    public boolean setMinReading(int newMin) {
        if (maxReading != null && newMin >= maxReading) return false;
        minReading = newMin;
        return true;
    }

    public boolean setNewReading(int newReading) {
        currentReading = newReading;
        return true;
    }

    public boolean setSensorType(SensorType newType) {
        if (sensorType != null) return false;
        sensorType = newType;
        return true;
    }

    public int getMaxReading() {
        return maxReading;
    }

    public int getMinReading() {
        return minReading;
    }

    public int getCurrentReading() {
        return currentReading;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

}
