package computing.parallel.sort.messaging.tasks;

import computing.parallel.sort.MergeSortBase;
import computing.parallel.sort.messaging.MqConnection;

import javax.jms.Message;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListTask<T extends Comparable<T> & Serializable> extends MergeSortBase<T> implements Task, Serializable {

    static final long serialVersionUID = 50L;

    private final List<T> list;
    private final List<UUID> parents;
    private final UUID id;

    public ListTask(List<T> list, List<UUID> parents, UUID id) {
        this.list = list;
        this.parents = parents;
        this.id = id;
    }

    @Override
    public void process(MqConnection connection, Message message) {
        // Do nothing
    }

    public List<T> getList() {
        return list;
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
        return "ListTask{" +
                "list=" + list +
                ", parents=" + parents +
                ", id=" + id +
                '}';
    }
}
