package conditionNotify;

class SomeClass1 {
    boolean isCompleted = false;

    public synchronized void declareSuccess() throws InterruptedException {
        while (!isCompleted) {
            wait();
        }

        System.out.println("Success!!");
    }

    public synchronized void finishWork() {
        isCompleted = true;
        notify();
    }
}