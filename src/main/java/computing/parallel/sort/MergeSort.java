package computing.parallel.sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MergeSort<T extends Comparable<T>> implements Sorter<T> {

    @Override
    public List<T> sort(List<T> list) {
        return sort(list, 0);
    }

    public List<T> sort(List<T> list, int depth) {
        if (list.size() <= 1) return list;
        final var middleIndex = findMiddle(list);
        var left = list.subList(0, middleIndex);
        var right = list.subList(middleIndex, list.size());
        var sortedLeft = sort(left, depth + 1);
        var sortedRight = sort(right, depth + 1);
        return merge(sortedLeft, sortedRight);
    }

    public int findMiddle(List<T> list) {
        return (list.size() + 1) / 2;
    }

    public List<T> merge(List<T> left, List<T> right) {
        final var result = new ArrayList<T>(left.size() + right.size());
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            final T leftEl = left.get(i);
            final T rightEl = right.get(j);
            var compare = leftEl.compareTo(rightEl);
            if (compare <= 0) {
                i++;
                result.add(leftEl);
            } else {
                j++;
                result.add(rightEl);
            }
        }
        while (i < left.size()) {
            result.add(left.get(i++));
        }
        while (j < right.size()) {
            result.add(right.get(j++));
        }
        return result;
    }
}
