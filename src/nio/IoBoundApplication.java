package nio;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IoBoundApplication {
    private static final int NUMBER_OF_TASKS = 1_000_000; //1000, 10_000, 100_000

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("please enter to start");
        sc.nextLine();
        System.out.printf("Running %d tasks\n", NUMBER_OF_TASKS);

        long start = System.currentTimeMillis();
        performTasks();
        System.out.printf("Tasks took %dms to complete\n", System.currentTimeMillis()-start);
    }

    private static void performTasks() {
        try (ExecutorService executorService = // Executors.newVirtualThreadPerTaskExecutor()) {
                 // Executors.newFixedThreadPool(1000)) {  // newCachedThreadPool()
                Executors.newCachedThreadPool()) {

            for (int i = 0; i < NUMBER_OF_TASKS; i++) {
                executorService.submit(() -> {
                    blockingIoOperation();
                });
            }
        }

    }

    // Simulates a long blocking IO
    private static void blockingIoOperation() {
        System.out.println("Executing a blocking task from thread: "+ Thread.currentThread());
        // /*
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

         // */

        /*
        // 99 context switches, same time for blocking
        for (int i = 0; i < 100; i++) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        */

    }
}
