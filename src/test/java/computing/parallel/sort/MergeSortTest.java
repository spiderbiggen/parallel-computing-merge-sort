package computing.parallel.sort;

import computing.parallel.sort.util.CSVParser;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.util.List;

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
        sortTest(sorter);
    }

    @Test
    public void sortTL() {
        System.gc();
        Sorter<Runner> sorter = new MergeSortTL<>();
        sortTest(sorter);
    }

    @Test
    public void sortExecutor() {
        System.gc();
        Sorter<Runner> sorter = new MergeSortExecutor<>();
        sortTest(sorter);
    }

    @Test
    public void sortForkJoin() {
        System.gc();
        Sorter<Runner> sorter = new MergeSortForkJoin<>();
        sortTest(sorter);
    }

    private void sortTest(Sorter<Runner> sorter) {
        final long start = System.currentTimeMillis();
        final var result = sorter.sort(unsorted);
        final long end = System.currentTimeMillis();
        System.out.printf("Sorted Sequential in %dms%n", end - start);
        validateSort(result);
    }

    private void validateSort(List<Runner> result) {
        assertEquals(result.size(), unsorted.size());
        for (int i = 0; i < result.size() - 1; i++) {
            final Runner current = result.get(i);
            final Runner next = result.get(i + 1);
            assertTrue("Current: " + current + ". Next: " + next, current.compareTo(next) <= 0);
        }
    }
}