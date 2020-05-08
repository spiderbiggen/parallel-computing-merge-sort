package computing.parallel.sort;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import static org.hamcrest.CoreMatchers.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MergeSortTest {

    private List<Runner> unsorted;
    private List<Runner> sorted;
    private Sorter<Runner> sorter;

    @Before
    public void setUp() throws Exception {
        sorter = new MergeSort<>();
        unsorted = CSVParser.parse(MergeSortTest.class.getResourceAsStream("/csv/events/events_758674.csv"), Runner::new);
        sorted = CSVParser.parse(MergeSortTest.class.getResourceAsStream("/csv/events/events_758674_sorted.csv"), Runner::new);
        assert unsorted != null;
        assert sorted != null;
    }

    @Test
    public void sort() {
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
    public void testSort() {
    }

    @Test
    public void findMiddle() {
    }

    @Test
    public void merge() {
    }
}