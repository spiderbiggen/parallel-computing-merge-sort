package computing.parallel.sort;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class MergeSortTL<T extends Comparable<T>> extends MergeSortBase<T> implements Sorter<T> {
    @Override
    public List<T> sort(List<T> list) {
        try {
            return sort(list, 1);
        } catch (ExecutionException | InterruptedException e) {
            return null;
        }
    }

    private List<T> sort(List<T> list, int depth) throws ExecutionException, InterruptedException {
        if (list.size() <= 1) return list;
        Map<String, List<T>> lists = split(list);
        if (depth <= MAX_DEPTH) {
            SortRunnable sr = new SortRunnable(lists.get("left"), depth + 1);
            Thread t1 = new Thread(sr);
            t1.start();
            List<T> right = sort(lists.get("right"), depth + 1);
            t1.join();
            return merge(sr.getResult(), right);
        }
        List<T> left = sort(lists.get("left"), depth + 1);
        List<T> right = sort(lists.get("right"), depth + 1);
        return merge(left, right);
    }

    private class SortRunnable implements Runnable {

        private List<T> result = null;
        private final List<T> input;
        private final int depth;

        public SortRunnable(List<T> input, int depth) {
            this.input = input;
            this.depth = depth;
        }

        @Override
        public void run() {
            try {
                result = sort(input, depth);
            } catch (ExecutionException | InterruptedException e) {
                // this shouldn't happen
            }
        }

        public List<T> getResult() {
            return result;
        }
    }
}
