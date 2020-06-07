package computing.parallel.sort;

import computing.parallel.sort.messaging.Master;

import javax.jms.JMSException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

public class MergeSortMessaging<T extends Comparable<T> & Serializable> extends MergeSortBase<T> implements Sorter<T> {
    private Master<T> masterService;

    public MergeSortMessaging() throws RemoteException, JMSException {
        this.masterService = new Master<>();
        this.masterService.connect();
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
}
