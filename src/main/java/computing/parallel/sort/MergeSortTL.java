package computing.parallel.sort;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MergeSortTL<T extends Comparable<T>> extends MergeSortBase<T> implements Sorter<T> {
    public static final int MAX_THREADS = Runtime.getRuntime().availableProcessors();
    //    public static final int MAX_THREADS = 4;
    public static final int MAX_DEPTH = (int) Math.ceil(Math.log(MAX_THREADS) / Math.log(2));
    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public List<T> sort(List<T> list) {
        try {
            return sort(list, 0);
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    private List<T> sort(List<T> list, int depth) throws ExecutionException, InterruptedException {
        if (list.size() <= 1) return list;
        Map<String, List<T>> lists = split(list);
        if (depth <= MAX_DEPTH) {
            Future<List<T>> left = executor.submit(() -> sort(lists.get("left"), depth + 1));
            Future<List<T>> right = executor.submit(() -> sort(lists.get("right"), depth + 1));
            return merge(left.get(), right.get());
        }
        List<T> left = sort(lists.get("left"), depth + 1);
        List<T> right = sort(lists.get("right"), depth + 1);
        return merge(left, right);
    }
}
