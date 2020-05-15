package computing.parallel.sort;

import computing.parallel.sort.util.CSVParser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MergeSortTest {

    private static List<Runner> unsorted;
    private static List<Runner> sorted;

    @BeforeClass
    public static void beforeClass() throws Exception {
        unsorted = CSVParser.parse(MergeSortTest.class.getResourceAsStream("/events_758675.csv"), Runner::new);
        sorted = CSVParser.parse(MergeSortTest.class.getResourceAsStream("/events_758675_sorted.csv"), Runner::new);
        assert unsorted != null;
        assert sorted != null;
    }

    @Test
    public void sort() {
        System.gc();
        Sorter<Runner> sorter = new MergeSort<>();
        final long start = System.currentTimeMillis();
        final var result = sorter.sort(unsorted);
        final long end = System.currentTimeMillis();
        System.out.printf("Sorted sequential in %dms%n", end - start);
        assertEquals(result.size(), sorted.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), sorted.get(i));
        }
    }

    @Test
    public void sortTL() {
        System.gc();
        Sorter<Runner> sorter = new MergeSortTL<>();
        final long start = System.currentTimeMillis();
        final var result = sorter.sort(unsorted);
        final long end = System.currentTimeMillis();
        System.out.printf("Sorted threads in %dms%n", end - start);
        assertEquals(result, sorted);
    }

    @Test
    public void sortExecutor() {
        System.gc();
        Sorter<Runner> sorter = new MergeSortExecutor<>();
        final long start = System.currentTimeMillis();
        final var result = sorter.sort(unsorted);
        final long end = System.currentTimeMillis();
        System.out.printf("Sorted executor in %dms%n", end - start);
        assertEquals(result, sorted);
    }

    @Test
    public void merge() {
    }
}