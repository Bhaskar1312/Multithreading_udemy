package atomic2;

import java.util.concurrent.atomic.AtomicInteger;

public class LockFreeInventoryCounter {
    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

        incrementingThread.start();
        decrementingThread.start();

        incrementingThread.join();
        decrementingThread.join();

        System.out.println("We currently have "+ inventoryCounter.getItems()+" items!");
    }
    public static class IncrementingThread extends Thread {
        private InventoryCounter inventoryCounter;
        public IncrementingThread(InventoryCounter counter) {
            this.inventoryCounter = counter;
        }

        @Override
        public void run() {
            for(int i=0;i<10000;i++) {
                inventoryCounter.increment();
            }
        }
    }
    public static class DecrementingThread extends Thread {
        private InventoryCounter inventoryCounter;
        public DecrementingThread(InventoryCounter counter) {
            this.inventoryCounter = counter;
        }

        @Override
        public void run() {
            for(int i=0;i<10000;i++) {
                inventoryCounter.decrement();
            }
        }
    }
    public static class InventoryCounter {
        private AtomicInteger items = new AtomicInteger(0);
        public void increment() {
            items.incrementAndGet();
        }
        public void decrement() {
            items.decrementAndGet();
        }
        public int getItems() {
            return items.get();
        }

        // This function together is not an atomic operation and has a race condition
        void f() {
            items.incrementAndGet();
            items.addAndGet(-5);
            //the combination of atomic operations are not an atomic operation.
        }
        void g() {
            items.set(10);
        }
        //If thread 1 is executing f, while thread 2 is executing g.
        //We may get the correct result 10 (sequence : f -> g)
        //We may get the correct result 6 (sequence g -> f)
    }
}
