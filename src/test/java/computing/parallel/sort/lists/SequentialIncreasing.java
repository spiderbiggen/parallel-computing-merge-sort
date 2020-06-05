package computing.parallel.sort.lists;

import computing.parallel.sort.MergeSort;
import computing.parallel.sort.Runner;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.List;

@RunWith(Parameterized.class)
public class SequentialIncreasing extends IncreasingSizeTest {

    public SequentialIncreasing(List<Runner> input) {
        super(input);
        sorter = new MergeSort<>();
    }
}
