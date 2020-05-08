package computing.parallel.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeSortBase<T extends Comparable<T>> {
    Map<String, List<T>> split(List<T> baseList) {
        final int middleIndex = findMiddle(baseList);
        HashMap<String, List<T>> lists = new HashMap<>();
        lists.put("left", baseList.subList(0, middleIndex));
        lists.put("right", baseList.subList(middleIndex, baseList.size()));
        return lists;
    }

    int findMiddle(List<T> list) {
        return (list.size() + 1) / 2;
    }

    List<T> merge(List<T> left, List<T> right) {
        final ArrayList<T> result = new ArrayList<T>(left.size() + right.size());
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            final T leftEl = left.get(i);
            final T rightEl = right.get(j);
            int compare = leftEl.compareTo(rightEl);
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
