package conditionNotify;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SomeClass2 {
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    boolean isCompleted = false;

    public void declareSuccess() throws InterruptedException {
        lock.lock();
        try {
            while (!isCompleted) {
                condition.await();
            }
        }
        finally {
            lock.unlock();
        }

        System.out.println("Success!!");
    }

    public void finishWork() {
        lock.lock();
        try {
            isCompleted = true;
            condition.signal();
        }
        finally {
            lock.unlock();
        }
    }
}










