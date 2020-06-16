package computing.parallel.sort.messaging.tasks;

import computing.parallel.sort.MergeSort;
import computing.parallel.sort.messaging.MqConnection;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SplitTask<T extends Comparable<T> & Serializable> extends MergeSort<T> implements Task, Serializable {

    static final long serialVersionUID = 42L;

    private final List<T> list;
    private final List<UUID> parents;
    private final UUID id;

    public SplitTask(List<T> list, List<UUID> parents, UUID id) {
        this.list = list;
        this.parents = parents;
        this.id = id;
    }

    @Override
    public void process(MqConnection connection, Message message) throws JMSException {
        final Destination replyTo = message.getJMSReplyTo();
        sequentialSplit(list, parents, id, connection, replyTo);
    }

    public void sequentialSplit(List<T> localList, List<UUID> localParent, UUID localId, MqConnection connection, Destination replyTo) throws JMSException {
        if (localList.size() <= 1) {
            ObjectMessage msg = connection.session.createObjectMessage(new ListTask<T>(localList, localParent, localId));
            connection.getEmptyProducer().send(replyTo, msg);
            return;
        }
        if (localParent.size() > MAX_DEPTH) {
            List<T> sorted = sort(localList);
            ObjectMessage msg = connection.session.createObjectMessage(new ListTask<T>(sorted, localParent, localId));
            connection.getEmptyProducer().send(replyTo, msg);
            return;
        }
        Map<String, List<T>> lists = split(localList);
        UUID childId = UUID.randomUUID();
        List<UUID> childParents = new ArrayList<>(localParent);
        childParents.add(localId);
        sequentialSplit(lists.get("left"), childParents, childId, connection, replyTo);
        sequentialSplit(lists.get("right"), childParents, childId, connection, replyTo);
    }

    @Override
    public List<UUID> getParents() {
        return parents;
    }

    @Override
    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "SplitTask{" +
                "list=" + list +
                ", parents=" + parents +
                ", id=" + id +
                '}';
    }
}
