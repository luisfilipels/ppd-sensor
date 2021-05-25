package utils;

public class SensorSingleton {

    private static SensorSingleton instance;

    private SensorSingleton() {}

    public static SensorSingleton getInstance() {
        if (instance == null) {
            instance = new SensorSingleton();
        }
        return instance;
    }

    private SensorType sensorType = null;

    private Integer maxReading = null;
    private Integer minReading = null;
    private Integer currentReading = null;

    private String sensorID = "";

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

    public boolean setSensorID(String newID) {
        if (!sensorID.equals("")) return false;
        sensorID = newID;
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
