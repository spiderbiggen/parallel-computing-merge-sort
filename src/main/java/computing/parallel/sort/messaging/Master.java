package computing.parallel.sort.messaging;

import computing.parallel.sort.messaging.tasks.ListTask;
import computing.parallel.sort.messaging.tasks.MergeTask;
import computing.parallel.sort.messaging.tasks.SplitTask;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Master<T extends Comparable<T> & Serializable> extends MqConnection {
    public Master() throws RemoteException {
    }

    public Master(String url, String queue) throws RemoteException {
        super(url, queue);
    }

    public List<T> sort(List<T> list) throws JMSException {
        Map<UUID, ListTask<T>> a = new ConcurrentHashMap<>();
        Queue tempQueue = session.createTemporaryQueue();
        Queue finalQueue = session.createTemporaryQueue();
        SplitTask<T> initialTask = new SplitTask<T>(list, new ArrayList<>(), UUID.randomUUID());
        ObjectMessage message = session.createObjectMessage(initialTask);
        message.setJMSReplyTo(tempQueue);
        getProducer().send(message);

        MessageConsumer tempConsumer = session.createConsumer(tempQueue);
        tempConsumer.setMessageListener(tempMessage -> {
            try {
                ObjectMessage objectMessage = (ObjectMessage) tempMessage;
                if (objectMessage.getObject() instanceof ListTask) {
                    ListTask<T> task = (ListTask<T>) objectMessage.getObject();
                    final UUID taskId = task.getId();
                    if (task.getList().size() == list.size()) {
                        getEmptyProducer().send(finalQueue, tempMessage);
                    } else if (a.containsKey(taskId)) {
                        MergeTask<T> mergeTask = new MergeTask<>(a.remove(taskId).getList(), task.getList(), task.getParents());
                        Message msg = session.createObjectMessage(mergeTask);
                        msg.setJMSReplyTo(tempQueue);
                        getProducer().send(msg);
                    } else {
                        a.put(taskId, task);
                    }
                } else {
                    System.out.println("stuk");
                }
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });

        Session finalSession = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageConsumer finalConsumer = finalSession.createConsumer(finalQueue);
        finalConsumer.setMessageListener(null);
        ObjectMessage objectMessage = (ObjectMessage) finalConsumer.receive();
        if (objectMessage.getObject() instanceof ListTask) {
            ListTask<T> task = (ListTask<T>) objectMessage.getObject();
            return task.getList();
        } else {
            return list;
        }
    }
}
