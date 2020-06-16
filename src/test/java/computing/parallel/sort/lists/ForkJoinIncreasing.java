package computing.parallel.sort.lists;

import computing.parallel.sort.MergeSortForkJoin;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class ForkJoinIncreasing extends IncreasingSizeTest {

    public ForkJoinIncreasing(String input) {
        super(input);
        sorter = new MergeSortForkJoin<>();
    }
}
