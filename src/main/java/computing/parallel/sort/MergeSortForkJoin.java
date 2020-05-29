package computing.parallel.sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.stream.Collectors;

public class MergeSortForkJoin<T extends Comparable<T>> extends MergeSortBase<T> implements Sorter<T> {
    private final ForkJoinPool pool = new ForkJoinPool(MAX_THREADS);

    @Override
    public List<T> sort(List<T> list) {
        ForkJoinTask<List<T>> task = new RecursiveThreadPoolTask(list, 1);
        return pool.invoke(task);
    }


    private class RecursiveThreadPoolTask extends RecursiveTask<List<T>> {
        final List<T> input;
        final int depth;

        private RecursiveThreadPoolTask(List<T> input, int depth) {
            this.input = input;
            this.depth = depth;
        }

        @Override
        protected List<T> compute() {
            if (input.size() <= 1) return input;
            Map<String, List<T>> lists = split(input);
            final var leftTask = new RecursiveThreadPoolTask(lists.get("left"), depth + 1);
            final var rightTask = new RecursiveThreadPoolTask(lists.get("right"), depth + 1);
            List<T> left, right;
            if (depth <= MAX_DEPTH) {
                List<RecursiveThreadPoolTask> subTasks = new ArrayList<>();
                subTasks.add(leftTask);
                subTasks.add(rightTask);
                var results = ForkJoinTask.invokeAll(subTasks).stream().map(ForkJoinTask::join).collect(Collectors.toList());
                return merge(results.get(0), results.get(1));
            }

            left = leftTask.compute();
            right = rightTask.compute();

            return merge(left, right);
        }
    }
}
