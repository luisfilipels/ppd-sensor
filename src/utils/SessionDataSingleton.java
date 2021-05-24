package utils;

public class SessionDataSingleton {
    // TODO: Set to private and create getters/setters
    public SensorType sensorType;

    private static SessionDataSingleton instance;

    private SessionDataSingleton() {}

    public static SessionDataSingleton getInstance() {
        if (instance == null) {
            instance = new SessionDataSingleton();
        }
        return instance;
    }
}
