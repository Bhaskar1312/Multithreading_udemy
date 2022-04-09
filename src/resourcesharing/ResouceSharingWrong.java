package resourcesharing;

// way one
public class ResouceSharingWrong {
    public static void main(String[] args) throws InterruptedException {
        InventoryCounter inventoryCounter = new InventoryCounter();
        IncrementingThread incrementingThread = new IncrementingThread(inventoryCounter);
        DecrementingThread decrementingThread = new DecrementingThread(inventoryCounter);

//        incrementingThread.start();
//        incrementingThread.join();
//        decrementingThread.start();
//        decrementingThread.join(); // result - > 0

        incrementingThread.start();
        decrementingThread.start();
        incrementingThread.join();
        decrementingThread.join(); // non -zero mostly
        //1. inventoryCounter is shared obj, items is shared resource between two threads
        //2. items++ and items-- both happening at same time, and are not atomic operations
        //atomic - all or nothing - no intermediate state

        System.out.println("We currently have "+ inventoryCounter.getItems() + " items");
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
        public void increment() {
            items++;
        }
        public void decrement() {
            items--;
        }
        public int getItems() {
            return items;
        }
    }
}
