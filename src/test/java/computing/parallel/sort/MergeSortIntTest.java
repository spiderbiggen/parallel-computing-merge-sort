package computing.parallel.sort;

import org.junit.BeforeClass;
import org.junit.Test;

import javax.jms.JMSException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static computing.parallel.sort.util.SortTest.sortTest;

public class MergeSortIntTest {

    private static List<Integer> unsorted;

    @BeforeClass
    public static void setUp() throws Exception {
        Random random = new Random(5454);
        final Integer[] ints = new Integer[1_024_000];
        Arrays.parallelSetAll(ints, operand -> random.nextInt());
        System.out.printf("Sorting %d%n elements", ints.length);
        unsorted = Arrays.asList(ints);
    }

    @Test
    public void sort() {
        System.gc();
        Sorter<Integer> sorter = new MergeSort<>();
        sortTest(unsorted, sorter, "Sequential");
    }

    @Test
    public void sortTL() {
        System.gc();
        Sorter<Integer> sorter = new MergeSortTL<>();
        sortTest(unsorted, sorter, "Threads and Locks");
    }

    @Test
    public void sortExecutor() {
        System.gc();
        Sorter<Integer> sorter = new MergeSortExecutor<>();
        sortTest(unsorted, sorter, "Executor");
    }

    @Test
    public void sortForkJoin() {
        System.gc();
        Sorter<Integer> sorter = new MergeSortForkJoin<>();
        sortTest(unsorted, sorter, "Fork Join");
    }

    @Test
    public void sortMessaging() throws IOException, JMSException {
        System.gc();
        MergeSortMessaging<Integer> sorter = new MergeSortMessaging<>();
        sortTest(unsorted, sorter, "Messaging");
    }
}