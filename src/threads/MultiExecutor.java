package threads;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class MultiExecutor {

    // Add any necessary member variables here
    private List<Runnable> tasks;

    /*
     * @param tasks to executed concurrently
     */
    public MultiExecutor(List<Runnable> tasks) {
        // Complete your code here
        this.tasks = tasks;
    }

    /**
     * Starts and executes all the tasks concurrently
     */
    public void executeAll() {
        // complete your code here
//        for(Runnable task : tasks) {
//            task.run();
//        }
        List<Thread> threads = new ArrayList<>();
        for(Runnable r: tasks) {
            threads.add(new Thread(r));
        }

        for(Thread thread: threads) {
            thread.start();
        }
    }

    public static void main(String[] args) {
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        };
        Runnable r2 = new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName());
            }
        };
        List<Runnable> tasks = new ArrayList<>();
        tasks.add(r1);
        tasks.add(r2);
        MultiExecutor me = new MultiExecutor(tasks);
        me.executeAll();
    }
}