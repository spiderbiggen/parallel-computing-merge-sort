package computing.parallel.sort.lists;

import computing.parallel.sort.MergeSort;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class SequentialIncreasing extends IncreasingSizeTest {

    public SequentialIncreasing(String input) {
        super(input);
        sorter = new MergeSort<>();
    }
}
