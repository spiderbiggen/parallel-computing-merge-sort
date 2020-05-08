package computing.parallel.sort;

import java.util.Collection;
import java.util.List;

public interface Sorter<T extends Comparable<T>> {
    List<T> sort(Collection<T>list);
}
