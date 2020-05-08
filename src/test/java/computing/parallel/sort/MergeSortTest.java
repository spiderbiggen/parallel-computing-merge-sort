package computing.parallel.sort;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MergeSortTest {

    private List<Runner> unsorted;
    private List<Runner> sorted;
    private Sorter<Runner> sorter;

    @Before
    public void setUp() throws Exception {
        sorter = new MergeSort<>();
        unsorted = CSVParser.parse(758683, MergeSortTest.class.getResourceAsStream("/csv/events/events_758683.csv"), Runner::new);
        sorted = CSVParser.parse(758683, MergeSortTest.class.getResourceAsStream("/csv/events/events_758683_sorted.csv"), Runner::new);
        assert unsorted != null;
        assert sorted != null;
    }

    @Test
    public void sort() {
        final var result = sorter.sort(unsorted);
        assertEquals(sorted, result);
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