package computing.parallel.sort.lists;

import computing.parallel.sort.MergeSortMessaging;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import javax.jms.JMSException;
import java.io.IOException;

@RunWith(Parameterized.class)
public class MessagingIncreasing extends IncreasingSizeTest {

    public MessagingIncreasing(String input) throws IOException, JMSException {
        super(input);
        sorter = new MergeSortMessaging<>();
    }
}
