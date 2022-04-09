package resourcesharing;

// way three
public class ResourceSharingUsingSynchronizedLockObject {
    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);


        incrementingThread.start();
        decrementingThread.start();
        incrementingThread.join();
        decrementingThread.join();  // zero
        System.out.println("We currently have "+ inventoryCounter.getItems() + " items");
//        synchronized block or method is Reentrant
//        a thread can not prevent itelf from entering critical section, 1 thread can access both methods at same time
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
        private int items = 0;
        Object lock = new Object();
        public void increment() {
            synchronized (this.lock) {
                items++;
            }
        }
        public void decrement() {
            synchronized (lock) {
                items--;
            }
        }
        public int getItems() {
            return items;
        }
    }
}
