package computing.parallel.sort.lists;

import computing.parallel.sort.MergeSortTest;
import computing.parallel.sort.Runner;
import computing.parallel.sort.Sorter;
import computing.parallel.sort.util.CSVParser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;


public abstract class IncreasingSizeTest {

    protected Sorter<Runner> sorter;
    private final List<Runner> input;
    private final List<Runner> expected;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {readResource("/events_001000.csv"), readResource("/events_001000_sorted.csv")},
                {readResource("/events_002000.csv"), readResource("/events_002000_sorted.csv")},
                {readResource("/events_004000.csv"), readResource("/events_004000_sorted.csv")},
                {readResource("/events_008000.csv"), readResource("/events_008000_sorted.csv")},
                {readResource("/events_016000.csv"), readResource("/events_016000_sorted.csv")},
                {readResource("/events_032000.csv"), readResource("/events_032000_sorted.csv")},
                {readResource("/events_064000.csv"), readResource("/events_064000_sorted.csv")},
                {readResource("/events_128000.csv"), readResource("/events_128000_sorted.csv")},
                {readResource("/events_256000.csv"), readResource("/events_256000_sorted.csv")},
                {readResource("/events_512000.csv"), readResource("/events_512000_sorted.csv")},
        });
    }

    private static List<Runner> readResource(String filename) {
        try (InputStream in = MergeSortTest.class.getResourceAsStream(filename)) {
            return CSVParser.parse(in, Runner::new);
        } catch (IOException e) {
            System.out.println("Test is broken 🤔");
            return null;
        }
    }

    public IncreasingSizeTest(List<Runner> input, List<Runner> expected) {
        this.input = input;
        this.expected = expected;
    }

    @Test
    public void sort() {
        System.gc();
        final long start = System.currentTimeMillis();
        List<Runner> result = sorter.sort(input);
        final long end = System.currentTimeMillis();
        assertEquals(result, expected);
        System.out.printf("%d;%d%n", input.size(), end - start);
    }
}
