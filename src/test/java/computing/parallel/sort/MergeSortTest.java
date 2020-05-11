package computing.parallel.sort;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MergeSortTest {

    private static List<Runner> unsorted;
    private static List<Runner> sorted;

    @BeforeClass
    public static void beforeClass() throws Exception {
        unsorted = CSVParser.parse(MergeSortTest.class.getResourceAsStream("/csv/events/events_758674.csv"), Runner::new);
        sorted = CSVParser.parse(MergeSortTest.class.getResourceAsStream("/csv/events/events_758674_sorted.csv"), Runner::new);
        assert unsorted != null;
        assert sorted != null;
    }

    @Before
    public void setUp() throws Exception {
        System.gc();
    }


    @Test
    public void sort() {
        Sorter<Runner> sorter = new MergeSort<>();
        final long start = System.nanoTime();
        final var result = sorter.sort(unsorted);
        final long end = System.nanoTime();
        System.out.printf("Sorted in %dns%n", end - start);
        assertEquals(result.size(), sorted.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), sorted.get(i));
        }
    }

    @Test
    public void sortAsync() {
        Sorter<Runner> sorter = new MergeSortTL<>();
        final long start = System.nanoTime();
        final var result = sorter.sort(unsorted);
        final long end = System.nanoTime();
        System.out.printf("Sorted in %dns%n", end - start);
        assertEquals(result, sorted);
    }

    @Test
    public void findMiddle() {
    }

    @Test
    public void merge() {
    }
}