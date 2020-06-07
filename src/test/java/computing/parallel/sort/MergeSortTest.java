package computing.parallel.sort;

import computing.parallel.models.Runner;
import computing.parallel.sort.util.CSVParser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static computing.parallel.sort.util.SortTest.sortTest;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MergeSortTest {

    private static List<Runner> unsorted;

    @BeforeClass
    public static void beforeClass() throws Exception {
        unsorted = CSVParser.parse(MergeSortTest.class.getResourceAsStream("/events_758675.csv"), Runner::new);
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
}