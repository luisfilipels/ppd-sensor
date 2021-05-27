package networking;

import utils.SensorSingleton;

public class NetworkHandlerSingleton {

    private Publisher runner;
    private Thread t;

    private NetworkHandlerSingleton() {}

    private static NetworkHandlerSingleton instance;

    public void sendMessage(String message) {
        runner.setStringToSend(message);
    }

    public static NetworkHandlerSingleton getInstance() {
        if (instance == null) {
            instance = new NetworkHandlerSingleton();
        }
        return instance;
    }

    public void initialize() {
        runner = new Publisher(SensorSingleton.getInstance().getBrokerIP());

        t = new Thread(runner);
        t.start();
    }

}
