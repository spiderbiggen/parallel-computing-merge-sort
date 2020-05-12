package computing.parallel.sort;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MergeSortIntTest {

    private static List<Integer> unsorted;
    private static List<Integer> sorted;

    @BeforeClass
    public static void setUp() throws Exception {
        Random random = new Random(5454);
        final Integer[] ints = new Integer[1024000];
        Arrays.parallelSetAll(ints, operand -> random.nextInt());
        System.out.printf("Sorting %d%n", ints.length);
        unsorted = Arrays.asList(ints);
        sorted = new ArrayList<>(unsorted);
        Collections.sort(sorted);
    }

    @Test
    public void sort() {
        System.gc();
        Sorter<Integer> sorter = new MergeSort<>();
        final long start = System.currentTimeMillis();
        final var result = sorter.sort(unsorted);
        final long end = System.currentTimeMillis();
        System.out.printf("Sorted Sequential in %dms%n", end - start);
        assertEquals(result.size(), sorted.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), sorted.get(i));
        }
    }

    @Test
    public void sortTL() {
        System.gc();
        Sorter<Integer> sorter = new MergeSortTL<>();
        final long start = System.currentTimeMillis();
        final var result = sorter.sort(unsorted);
        final long end = System.currentTimeMillis();
        System.out.printf("Sorted threads in %dms%n", end - start);
        assertEquals(result.size(), sorted.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i), sorted.get(i));
        }
    }

    @Test
    public void sortExecutor() {
        System.gc();
        Sorter<Integer> sorter = new MergeSortExecutor<>();
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