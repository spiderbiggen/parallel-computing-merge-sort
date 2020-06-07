package computing.parallel.sort.messaging;

import computing.parallel.sort.messaging.tasks.Task;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import java.rmi.RemoteException;

public class Worker extends MqConnection {

    public Worker() throws RemoteException {
    }

    public Worker(String url, String queue) throws RemoteException {
        super(url, queue);
    }

    public void process() throws JMSException {
        connect();
        ObjectMessage message;
        try {
            while ((message = (ObjectMessage) getConsumer().receive()) != null) {
                final Serializable object = message.getObject();
                if (object instanceof Task) {
                    final Task task = (Task) object;
                    task.process(this, message);
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) throws RemoteException, JMSException {
        new Worker().process();
    }
}
