package computing.parallel.sort.lists;

import computing.parallel.sort.MergeSortBase;
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
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public abstract class IncreasingSizeTest {

    protected Sorter<Runner> sorter;
    private final List<Runner> input;

    @Parameterized.Parameters
    public static List<String> data() {
         return Arrays.asList(
                "/events_001000.csv",
                "/events_002000.csv",
                "/events_004000.csv",
                "/events_008000.csv",
                "/events_016000.csv",
                "/events_032000.csv",
                "/events_064000.csv",
                "/events_128000.csv",
                "/events_256000.csv",
                "/events_512000.csv"
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

    public IncreasingSizeTest(String input) {
        this.input = readResource(input);
    }

    @Test
    public void sort() {
        System.out.printf("Running tests using %d threads%n", MergeSortBase.MAX_THREADS);
        System.out.printf("%d%n", input.size());
        long sum = 0;
        for (int i = 0; i < 10; i++) {
            System.gc();
            final long start = System.currentTimeMillis();
            List<Runner> result = sorter.sort(input);
            final long end = System.currentTimeMillis();
            if(result == null) {
                System.out.println("using result");
            }
            final long totalTime = end - start;
            sum += totalTime;
            System.out.printf("%d%n", totalTime);
        }
        System.out.printf("Average: %.2f, Total: %d%n", sum / 10f, sum);
    }

}
