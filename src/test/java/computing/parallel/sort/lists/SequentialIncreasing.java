package computing.parallel.sort.lists;

import computing.parallel.sort.MergeSort;
import computing.parallel.sort.Runner;
import computing.parallel.sort.Sorter;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class SequentialIncreasing extends IncreasingSizeTest {

    public SequentialIncreasing(List<Runner> input, List<Runner> expected) {
        super(input, expected);
        sorter = new MergeSort<>();
    }
}
