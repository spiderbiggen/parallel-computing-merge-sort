package computing.parallel.sort.lists;

import computing.parallel.sort.MergeSortTL;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ThreadsAndLocksIncreasing extends IncreasingSizeTest {

    public ThreadsAndLocksIncreasing(String input) {
        super(input);
        sorter = new MergeSortTL<>();
    }
}
