package networking;

public class NetworkHandlerSingleton {

    private Publisher runner;
    private Thread t;

    private NetworkHandlerSingleton() {
        runner = new Publisher();
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
