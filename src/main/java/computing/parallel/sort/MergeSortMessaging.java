package computing.parallel.sort;

import computing.parallel.sort.messaging.Master;

import javax.jms.JMSException;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class MergeSortMessaging<T extends Comparable<T> & Serializable> extends MergeSortBase<T> implements Sorter<T> {
    private final Master<T> masterService;

    public MergeSortMessaging() throws IOException, JMSException {
        this.masterService = new Master<>();
        this.masterService.connect();
        this.masterService.createWorkers();
    }

    @Override
    public List<T> sort(List<T> list) {
        try {
            return masterService.sort(list);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void finalize() throws Throwable {
        masterService.stopWorkers();
        super.finalize();
    }
}
