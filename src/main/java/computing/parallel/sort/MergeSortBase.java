package computing.parallel.sort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MergeSortBase<T extends Comparable<T>> {
    public static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    //    public static final int MAX_THREADS = 4;
    public static final int MAX_DEPTH = (int) Math.ceil(Math.log(MAX_THREADS) / Math.log(2));

    protected Map<String, List<T>> split(List<T> baseList) {
        final int middleIndex = (baseList.size() + 1) / 2;
        HashMap<String, List<T>> lists = new HashMap<>();
        lists.put("left", new ArrayList<>(baseList.subList(0, middleIndex)));
        lists.put("right", new ArrayList<>(baseList.subList(middleIndex, baseList.size())));
        return lists;
    }

    protected List<T> merge(List<T> left, List<T> right) {
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
