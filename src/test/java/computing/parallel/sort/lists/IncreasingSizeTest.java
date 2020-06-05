package computing.parallel.sort.lists;

import computing.parallel.sort.MergeSortTest;
import computing.parallel.sort.Runner;
import computing.parallel.sort.Sorter;
import computing.parallel.sort.util.CSVParser;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public abstract class IncreasingSizeTest {

    protected Sorter<Runner> sorter;
    private final List<Runner> input;

    @Parameterized.Parameters
    public static List<List<Runner>> data() {
         return Arrays.asList(
                readResource("/events_001000.csv"),
                readResource("/events_002000.csv"),
                readResource("/events_004000.csv"),
                readResource("/events_008000.csv"),
                readResource("/events_016000.csv"),
                readResource("/events_032000.csv"),
                readResource("/events_064000.csv"),
                readResource("/events_128000.csv"),
                readResource("/events_256000.csv"),
                readResource("/events_512000.csv")
        );
    }

    private static List<Runner> readResource(String filename) {
        try (InputStream in = MergeSortTest.class.getResourceAsStream(filename)) {
            return CSVParser.parse(in, Runner::new);
        } catch (IOException e) {
            System.out.println("Test is broken 🤔");
            return null;
        }
    }

    public IncreasingSizeTest(List<Runner> input) {
        this.input = input;
    }

    @Test
    public void sort() {
        System.out.printf("%d%n", input.size());
        long sum = 0;
        for (int i = 0; i < 10; i++) {
            System.gc();
            final long start = System.currentTimeMillis();
            List<Runner> result = sorter.sort(input);
            final long end = System.currentTimeMillis();
            final long totalTime = end - start;
            validateSort(result);
            sum += totalTime;
            System.out.printf("%d%n", totalTime);
        }
        System.out.printf("Average: %.2f, Total: %d%n", sum / 10f, sum);
    }

    private void validateSort(List<Runner> result) {
        for (int i = 0; i < result.size() - 1; i++) {
            final Runner current = result.get(i);
            final Runner next = result.get(i + 1);
            assertTrue("Current: " + current + ". Next: " + next, current.compareTo(next) <= 0);
        }
    }
}
