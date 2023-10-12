package virtualthreads;


import java.util.ArrayList;
import java.util.List;

public class VirtualThreadDemo {

    private static final int NUMBER_OF_VIRTUAL_THREADS = 2000;
    public static void main(String[] args) throws InterruptedException {
        Runnable runnable = new BlockingTask();

        Thread platformThread = Thread.ofPlatform().unstarted(runnable);
        platformThread.start();
        platformThread.join();


        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_VIRTUAL_THREADS; i++) {
            Thread virtualThread = Thread.ofVirtual().unstarted(new BlockingTask());
            threads.add(virtualThread);
        }

        for (Thread virtualThread : threads) {
            virtualThread.start();
        }

        for (Thread virtualThread : threads) {
            virtualThread.join();
        }
    }

    private static class BlockingTask implements Runnable {

        @Override
        public void run() {
            System.out.println("Inside thread: "+ Thread.currentThread() + " before blocking call");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Inside thread: "+ Thread.currentThread() + " after blocking call");
        }
    }
}
