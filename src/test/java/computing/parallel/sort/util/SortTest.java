package computing.parallel.sort.util;

import computing.parallel.sort.Sorter;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class SortTest {
    public static <T extends Comparable<T>> void sortTest(List<T> unsorted, Sorter<T> sorter, String parallelType) {
        final long start = System.currentTimeMillis();
        final var result = sorter.sort(unsorted);
        final long end = System.currentTimeMillis();
        System.out.printf("Sorted using %s in %dms%n", parallelType, end - start);
        validateSort(result);
    }

    private static <T extends Comparable<T>> void validateSort(List<T> result) {
        Iterator<T> it = result.iterator();
        T current = it.next();
        while (it.hasNext()) {
            final T next = it.next();
            assertTrue("Current: " + current + ". Next: " + next, current.compareTo(next) <= 0);
            current = next;
        }
    }
}
