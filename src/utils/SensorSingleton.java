package utils;

import networking.NetworkHandlerSingleton;

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

    private void sendUpdateIfNeeded() {
        if (currentReading == null || maxReading == null || minReading == null) return;
        NetworkHandlerSingleton handler = NetworkHandlerSingleton.getInstance();
        if (currentReading >= maxReading) {
            handler.sendMessage(sensorID + ": Alerta! " + sensorType.label + " passou do limite máximo de "
                    + maxReading + "! Valor lido: " + currentReading);
        } else if (currentReading <= minReading) {
            handler.sendMessage(sensorID + ": Alerta! " + sensorType.label + " passou do limite mínimo de "
                    + minReading + "! Valor lido: " + currentReading);
        } else {
            handler.sendMessage(sensorID + ": " + sensorType.label + " voltou para um valor aceitável.");
        }
    }

    public boolean setMaxReading(int newMax) {
        if (minReading != null && newMax <= minReading) return false;
        maxReading = newMax;
        sendUpdateIfNeeded();
        return true;
    }

    public boolean setMinReading(int newMin) {
        if (maxReading != null && newMin >= maxReading) return false;
        minReading = newMin;
        sendUpdateIfNeeded();
        return true;
    }

    public boolean setNewReading(int newReading) {
        if (currentReading == null) {
            currentReading = newReading;
            return true;
        }
        int oldReading = currentReading;
        currentReading = newReading;
        if ((currentReading >= maxReading && oldReading < maxReading) ||
                (currentReading <= minReading && oldReading > maxReading) ||
                (currentReading < maxReading && oldReading >= maxReading) ||
                (currentReading > minReading && oldReading <= minReading)
        ) {
            sendUpdateIfNeeded();
        }
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

    public Integer getCurrentReading() {
        return currentReading;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

}
