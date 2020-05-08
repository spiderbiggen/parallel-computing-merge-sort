package computing.parallel.sort;

import java.util.List;
import java.util.Map;

public class MergeSort<T extends Comparable<T>> extends MergeSortBase<T> implements Sorter<T>  {

    @Override
    public List<T> sort(List<T> list) {
        return sort(list, 0);
    }

    public List<T> sort(List<T> list, int depth) {
        if (list.size() <= 1) return list;
        Map<String, List<T>> lists = split(list);
        return merge(sort(lists.get("left")), sort(lists.get("right")));
    }
}
