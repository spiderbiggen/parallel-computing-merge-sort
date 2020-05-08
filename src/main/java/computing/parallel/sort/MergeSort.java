package computing.parallel.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeSort<T extends Comparable<T>> implements Sorter<T> {

    @Override
    public List<T> sort(List<T> list) {
        return sort(list, 0);
    }

    public List<T> sort(List<T> list, int depth) {
        if (list.size() <= 1) return list;
        Map<String, List<T>> lists = split(list);
        return merge(sort(lists.get("left")), sort(lists.get("right")));
    }

    Map<String, List<T>> split(List<T> baseList) {
        final int middleIndex = findMiddle(baseList);
        List<T> left = baseList.subList(0, middleIndex);
        List<T> right = baseList.subList(middleIndex, baseList.size());
        HashMap<String, List<T>> lists = new HashMap<>();
        lists.put("left", left);
        lists.put("right", right);
        return lists;
    }

    public int findMiddle(List<T> list) {
        return (list.size() + 1) / 2;
    }

    public List<T> merge(List<T> left, List<T> right) {
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
