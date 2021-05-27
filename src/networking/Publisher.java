package networking;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import utils.SensorSingleton;

import javax.jms.*;
import java.util.concurrent.Semaphore;

class Publisher implements Runnable{

    //TODO: Change this
    private String url = ActiveMQConnection.DEFAULT_BROKER_URL;
    private Semaphore s = new Semaphore(0);

    private String messageToSend = "";
    public void setStringToSend(String string) {
        messageToSend = string;
        s.release();
        // Releases the mutex so the thread can continue to run
    }

    public String acquireMessageToSend() {
        String returnString = "";
        try {
            s.acquire();
            returnString = messageToSend;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return returnString;
    }

    @Override
    public void run(){
        try {
            System.out.println(url);
            // TODO: Find out how to format an IP in the local network
            String topicName = SensorSingleton.getInstance().getSensorType().topicName;

            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
            Connection connection = connectionFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            Destination dest = session.createTopic(topicName);

            MessageProducer publisher = session.createProducer(dest);

            while (true) {
                TextMessage message = session.createTextMessage();
                message.setText(acquireMessageToSend());
                publisher.send(message);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
