package computing.parallel.sort;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MergeSortIntTest {

    private static List<Integer> unsorted;

    @BeforeClass
    public static void setUp() throws Exception {
        Random random = new Random(5454);
        final Integer[] ints = new Integer[1024000];
        Arrays.parallelSetAll(ints, operand -> random.nextInt());
        System.out.printf("Sorting %d%n", ints.length);
        unsorted = Arrays.asList(ints);
    }

    @Test
    public void sort() {
        System.gc();
        Sorter<Integer> sorter = new MergeSort<>();
        sortTest(sorter);
    }

    @Test
    public void sortTL() {
        System.gc();
        Sorter<Integer> sorter = new MergeSortTL<>();
        sortTest(sorter);
    }

    @Test
    public void sortExecutor() {
        System.gc();
        Sorter<Integer> sorter = new MergeSortExecutor<>();
        sortTest(sorter);
    }

    @Test
    public void sortForkJoin() {
        System.gc();
        Sorter<Integer> sorter = new MergeSortForkJoin<>();
        sortTest(sorter);
    }

    private void sortTest(Sorter<Integer> sorter) {
        final long start = System.currentTimeMillis();
        final var result = sorter.sort(unsorted);
        final long end = System.currentTimeMillis();
        System.out.printf("Sorted Sequential in %dms%n", end - start);
        validateSort(result);
    }

    private void validateSort(List<Integer> result) {
        assertEquals(result.size(), unsorted.size());
        for (int i = 0; i < result.size() - 1; i++) {
            final Integer current = result.get(i);
            final Integer next = result.get(i + 1);
            assertTrue("Current: " + current + ". Next: " + next, current <= next);
        }
    }
}