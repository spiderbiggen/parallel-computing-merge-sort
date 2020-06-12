package computing.parallel.sort.messaging;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import java.io.Closeable;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class MqConnection extends UnicastRemoteObject implements AutoCloseable, Closeable {
    private static final String DEFAULT_URL = "tcp://localhost:61616?jms.useAsyncSend=true";
    private static final String DEFAULT_QUEUE = "queue";
    protected Connection connection;
    public Session session;
    protected Destination destination;
    private MessageProducer producer;
    private MessageProducer emptyProducer;
    private MessageConsumer consumer;
    private final String url;
    private final String queue;

    public MqConnection() throws RemoteException {
        this(DEFAULT_URL, DEFAULT_QUEUE);
    }

    public MqConnection(String url, String queue) throws RemoteException {
        super();
        this.url = url;
        this.queue = queue;
    }

    public void connect() throws JMSException {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        connectionFactory.setTrustAllPackages(true);
        connection = connectionFactory.createConnection();
        connection.start();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue(queue);
    }

    @Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                connection.close();
            } catch (JMSException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
                throw new IOException(e);
            }
        }
    }

    public MessageProducer getProducer() throws JMSException {
        if (producer == null) {
            producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        }
        return producer;
    }

    public MessageProducer getEmptyProducer() throws JMSException {
        if (emptyProducer == null) {
            emptyProducer = session.createProducer(null);
            emptyProducer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        }
        return emptyProducer;
    }

    public MessageConsumer getConsumer() throws JMSException {
        if (consumer == null) {
            consumer = session.createConsumer(destination);
        }
        return consumer;
    }
}
