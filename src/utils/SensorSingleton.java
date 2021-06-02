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

    private Integer maxReadingLimit = null;
    private Integer minReadingLimit = null;
    private Integer currentReading = null;

    private String sensorID = "";
    private String brokerIP = "";

    private void sendUpdateOfNewReading() {
        if (currentReading == null || maxReadingLimit == null || minReadingLimit == null) return;
        NetworkHandlerSingleton handler = NetworkHandlerSingleton.getInstance();
        if (currentReading >= maxReadingLimit) {
            handler.sendMessage(sensorID + ": Alerta! " + sensorType.label + " >= "
                    + maxReadingLimit + "! Valor lido: " + currentReading);
        } else if (currentReading <= minReadingLimit) {
            handler.sendMessage(sensorID + ": Alerta! " + sensorType.label + " <= "
                    + minReadingLimit + "! Valor lido: " + currentReading);
        } else {
            handler.sendMessage(sensorID + ": " + sensorType.label +
                    " voltou para um valor aceitável de "
                    + minReadingLimit + "<" + currentReading + "<" + maxReadingLimit);
        }
    }

    private void sendMaxLimitMessageFromOldMax(int oldMax) {
        if (currentReading == null || maxReadingLimit == null) return;
        NetworkHandlerSingleton handler = NetworkHandlerSingleton.getInstance();
        if (currentReading < maxReadingLimit && currentReading >= oldMax) {
            handler.sendMessage(sensorID + ": " + sensorType.label +
                    " voltou para um valor aceitável de "
                    + minReadingLimit + "<" + currentReading + "<" + maxReadingLimit);
        } else if (currentReading >= maxReadingLimit) {
            handler.sendMessage(sensorID + ": Alerta! " + sensorType.label + " >= "
                    + maxReadingLimit + "! Valor lido: " + currentReading);
        }
    }

    private void sendMinLimitMessageFromOldMin(int oldMin) {
        if (currentReading == null || minReadingLimit == null) return;
        NetworkHandlerSingleton handler = NetworkHandlerSingleton.getInstance();
        if (currentReading > minReadingLimit && currentReading <= oldMin) {
            handler.sendMessage(sensorID + ": " + sensorType.label +
                    " voltou para um valor aceitável de "
                    + minReadingLimit + "<" + currentReading + "<" + maxReadingLimit);
        } else if (currentReading <= minReadingLimit) {
            handler.sendMessage(sensorID + ": Alerta! " + sensorType.label + " <= "
                    + minReadingLimit + "! Valor lido: " + currentReading);
        }
    }

    private boolean temperatureExceedsLimitsWithOldReadingOf(int oldReading) {
        return (currentReading >= maxReadingLimit && oldReading < maxReadingLimit) ||
                (currentReading <= minReadingLimit && oldReading > minReadingLimit) ||
                (currentReading < maxReadingLimit && oldReading >= maxReadingLimit) ||
                (currentReading > minReadingLimit && oldReading <= minReadingLimit);
    }

    public boolean setMaxReadingLimit(int newMax) {
        if (minReadingLimit != null && newMax <= minReadingLimit) return false;
        int oldMax = maxReadingLimit == null ? Integer.MAX_VALUE : maxReadingLimit;
        maxReadingLimit = newMax;
        sendMaxLimitMessageFromOldMax(oldMax);
        return true;
    }

    public boolean setMinReadingLimit(int newMin) {
        if (maxReadingLimit != null && newMin >= maxReadingLimit) return false;
        int oldMin = minReadingLimit == null ? Integer.MIN_VALUE : minReadingLimit;
        minReadingLimit = newMin;
        sendMinLimitMessageFromOldMin(oldMin);
        return true;
    }

    public boolean setNewReading(int newReading) {
        if (currentReading == null) {
            currentReading = newReading;
            return true;
        }
        int oldReading = currentReading;
        currentReading = newReading;
        if (temperatureExceedsLimitsWithOldReadingOf(oldReading)) {
            sendUpdateOfNewReading();
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

    public boolean setBrokerIP(String ip) {
        brokerIP = ip;
        return true;
    }

    public int getMaxReadingLimit() {
        return maxReadingLimit;
    }

    public int getMinReadingLimit() {
        return minReadingLimit;
    }

    public Integer getCurrentReading() {
        return currentReading;
    }

    public SensorType getSensorType() {
        return sensorType;
    }

    public String getBrokerIP() {
        return brokerIP;
    }

}
