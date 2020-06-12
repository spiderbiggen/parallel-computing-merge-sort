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
        long start = System.currentTimeMillis();
        try {
            final Destination replyTo = message.getJMSReplyTo();
            if (list.size() <= 1) {
                ObjectMessage msg = connection.session.createObjectMessage(new ListTask<T>(list, parents, id));
                connection.getEmptyProducer().send(replyTo, msg);
                return;
            }
            if (parents.size() >= MAX_DEPTH) {
                List<T> sorted = sort(list);
                ObjectMessage msg = connection.session.createObjectMessage(new ListTask<T>(sorted, parents, id));
                connection.getEmptyProducer().send(replyTo, msg);
                return;
            }
            Map<String, List<T>> lists = split(list);
            UUID childId = UUID.randomUUID();
            List<UUID> childParents = new ArrayList<>(parents);
            childParents.add(id);
            final SplitTask<T> left = new SplitTask<>(lists.get("left"), childParents, childId);
            final SplitTask<T> right = new SplitTask<>(lists.get("right"), childParents, childId);

            ObjectMessage leftMsg = connection.session.createObjectMessage(left);
            ObjectMessage rightMsg = connection.session.createObjectMessage(right);

            leftMsg.setJMSReplyTo(replyTo);
            rightMsg.setJMSReplyTo(replyTo);

            connection.getProducer().send(leftMsg);
            connection.getProducer().send(rightMsg);
        } finally {
            long end = System.currentTimeMillis();
            System.out.printf("SplitTask#process %d took %dms%n", list.size(), end - start);
        }
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
