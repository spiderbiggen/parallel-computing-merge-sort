package computing.parallel.sort.lists;

import computing.parallel.sort.MergeSortTL;
import computing.parallel.sort.Runner;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class ThreadsAndLocksIncreasing extends IncreasingSizeTest {

    public ThreadsAndLocksIncreasing(String input) {
        super(input);
        sorter = new MergeSortTL<>();
    }
}
