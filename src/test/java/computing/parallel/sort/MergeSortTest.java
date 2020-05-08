package computing.parallel.sort;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MergeSortTest {

    private static final int INITIAL_CAPACITY = 20;
    private List<Integer> unsorted;
    private List<Integer> sorted;
    private Sorter<Integer> sorter;
    @Before
    public void setUp() throws Exception {
        sorter = new MergeSort<>();
        var random = new Random();
        unsorted = new ArrayList<>(INITIAL_CAPACITY);
        for (int i = 0; i < INITIAL_CAPACITY; i++) {
            unsorted.add(random.nextInt());
        }
        sorted = new ArrayList<>(unsorted);
        Collections.sort(sorted);
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