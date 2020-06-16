package computing.parallel.sort.messaging.tasks;

import computing.parallel.sort.MergeSortBase;
import computing.parallel.sort.messaging.MqConnection;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MergeTask<T extends Comparable<T> & Serializable> extends MergeSortBase<T> implements Task, Serializable {

    static final long serialVersionUID = 44L;

    private final List<T> left;
    private final List<T> right;
    private final List<UUID> parents;

    public MergeTask(List<T> left, List<T> right, List<UUID> parents) {
        this.left = left;
        this.right = right;
        this.parents = parents;
    }

    @Override
    public void process(MqConnection connection, Message message) throws JMSException {
        long start = System.currentTimeMillis();
        try {
            List<T> result = this.merge();
            ObjectMessage msg = connection.session.createObjectMessage(new ListTask<T>(result, getParents(), getId()));
            connection.getEmptyProducer().send(message.getJMSReplyTo(), msg);
        } finally {
            long end = System.currentTimeMillis();
            System.out.printf("Merge#process %d + %d took %dms%n", left.size(), right.size(), end - start);
        }
    }

    public List<T> merge() {
        return super.merge(left, right);
    }

    @Override
    public List<UUID> getParents() {
        return parents.size() > 1 ? new ArrayList<>(parents.subList(0, parents.size() - 1)) : new ArrayList<>();
    }

    @Override
    public UUID getId() {
        return parents.size() >= 1 ? parents.get(parents.size() - 1) : null;
    }

    @Override
    public String toString() {
        return "MergeTask{" +
                "left=" + left +
                ", right=" + right +
                ", parents=" + parents +
                '}';
    }
}
