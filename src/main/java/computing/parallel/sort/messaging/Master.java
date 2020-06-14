package computing.parallel.sort.messaging;

import computing.parallel.sort.MergeSortBase;
import computing.parallel.sort.messaging.tasks.ListTask;
import computing.parallel.sort.messaging.tasks.MergeTask;
import computing.parallel.sort.messaging.tasks.SplitTask;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Queue;
import javax.jms.Session;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Phaser;
import java.util.concurrent.atomic.AtomicReference;

public class Master<T extends Comparable<T> & Serializable> extends MqConnection {
    private Process[] workers;
    public Master() throws RemoteException {
    }

    public Master(String url, String queue) throws RemoteException {
        super(url, queue);
    }

    public void createWorkers() throws IOException {
        if (workers != null) {
            throw new UnsupportedOperationException("Workers should be null before creating new worker processes");
        }
        int numWorkers = Math.max(MergeSortBase.MAX_THREADS - 1, 1);
        workers = new Process[numWorkers];
        String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
        String classPath = System.getProperty("java.class.path");
        for (int childId = 0; childId < numWorkers; childId++) {
            ProcessBuilder child = new ProcessBuilder(
                    javaBin, "-classpath", classPath, Worker.class.getName()
            );
            workers[childId] = child.inheritIO().start();
            System.out.printf("%s %s%n", Worker.class.getName(), workers[childId]);
        }
        final Process[] fWorkers = workers;
        Thread closeChildThread = new Thread(() -> {
            for (Process fWorker : fWorkers) {
                fWorker.destroyForcibly();
                System.out.println(fWorker);
            }
        });
        Runtime.getRuntime().addShutdownHook(closeChildThread);
    }

    public void stopWorkers() {
        if (workers == null) return;
        for (Process worker : workers) {
            worker.destroyForcibly();
            System.out.println(worker);
        }
        workers = null;
    }

    public List<T> sort(List<T> list) throws JMSException {
        Map<UUID, ListTask<T>> a = new ConcurrentHashMap<>();
        Queue tempQueue = session.createTemporaryQueue();
        SplitTask<T> initialTask = new SplitTask<T>(list, new ArrayList<>(), UUID.randomUUID());
        Message message = session.createMessage();
        message.setJMSReplyTo(tempQueue);
        initialTask.process(this, message);

        MessageConsumer tempConsumer = session.createConsumer(tempQueue);
        final AtomicReference<List<T>> result = new AtomicReference<>();
        final Phaser phaser = new Phaser(2);
        tempConsumer.setMessageListener(tempMessage -> {
            try {
                ObjectMessage objectMessage = (ObjectMessage) tempMessage;
                if (objectMessage.getObject() instanceof ListTask) {
                    ListTask<T> task = (ListTask<T>) objectMessage.getObject();
                    final UUID taskId = task.getId();
                    final List<T> right = task.getList();
                    if (a.containsKey(taskId)) {
                        final List<T> left = a.remove(taskId).getList();
                        MergeTask<T> mergeTask = new MergeTask<>(left, right, task.getParents());
                        if (left.size() + right.size() >= list.size()) {
                            result.set(mergeTask.merge());
                            tempConsumer.close();
                            phaser.arrive();
                            return;
                        }
                        Message msg = session.createObjectMessage(mergeTask);
                        msg.setJMSReplyTo(tempQueue);
                        getProducer().send(msg);
                    } else {
                        a.put(taskId, task);
                    }
                } else {
                    System.out.println("Something went wrong in processing");
                    System.exit(-1);
                }
            } catch (JMSException | OutOfMemoryError e) {
                e.printStackTrace();
                System.out.println(e);
                System.exit(-1);
            }
        });
        phaser.arriveAndAwaitAdvance();
        return result.get();
    }
}
