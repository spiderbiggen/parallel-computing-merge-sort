package computing.parallel.sort;

import java.util.List;

public interface Sorter<T extends Comparable<T>> {
    List<T> sort(List<T> list);
}
