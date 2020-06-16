package computing.parallel.sort.messaging.tasks;

import computing.parallel.sort.messaging.MqConnection;

import javax.jms.JMSException;
import javax.jms.Message;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;


public interface Task extends Serializable {
    void process(MqConnection connection, Message message) throws JMSException;

    List<UUID> getParents();

    UUID getId();
}
