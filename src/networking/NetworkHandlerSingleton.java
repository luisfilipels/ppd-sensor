package networking;

import org.apache.activemq.ActiveMQConnection;

public class NetworkHandlerSingleton {

    private ActiveMQRunner runner;
    private Thread t;

    private NetworkHandlerSingleton() {
        runner = new ActiveMQRunner();
    }

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
        t = new Thread(runner);
        t.start();
    }

}
