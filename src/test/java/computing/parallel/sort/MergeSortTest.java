package computing.parallel.sort;

import computing.parallel.models.Runner;
import computing.parallel.sort.util.CSVParser;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.List;

import static computing.parallel.sort.util.SortTest.sortTest;

public class MergeSortTest {

    private static List<Runner> unsorted;

    @BeforeClass
    public static void beforeClass() throws Exception {
        unsorted = CSVParser.parse(MergeSortTest.class.getResourceAsStream("/events_512000.csv"), Runner::new);
        assert unsorted != null;
    }

    @Test
    public void sort() {
        System.gc();
        Sorter<Runner> sorter = new MergeSort<>();
        sortTest(unsorted, sorter, "Sequential");
    }

    @Test
    public void sortTL() {
        System.gc();
        Sorter<Runner> sorter = new MergeSortTL<>();
        sortTest(unsorted, sorter, "Threads and Locks");
    }

    @Test
    public void sortExecutor() {
        System.gc();
        Sorter<Runner> sorter = new MergeSortExecutor<>();
        sortTest(unsorted, sorter, "Executor");
    }

    @Test
    public void sortForkJoin() {
        System.gc();
        Sorter<Runner> sorter = new MergeSortForkJoin<>();
        sortTest(unsorted, sorter, "Fork Join");
    }

    @Test
    public void sortMessaging() throws IOException, JMSException {
        System.gc();
        Sorter<Runner> sorter = new MergeSortMessaging<>();
        sortTest(unsorted, sorter, "Messaging");
    }
}