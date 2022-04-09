package deadlock;

import java.util.Random;

public class Deadlock {
    public static void main(String[] args) {
        Intersection intersection = new Intersection();
        Thread trianAThread = new Thread(new TrainA(intersection));
        Thread trainBThread = new Thread(new TrainB(intersection));

        trianAThread.start();
        trainBThread.start();

    }
    public static class TrainA implements Runnable {
        private Intersection intersection;
        private Random random = new Random();
        public TrainA(Intersection intersection) {
            this.intersection = intersection;
        }
        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(10);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intersection.takeRoadB();
            }
        }
    }
    public static class TrainB implements Runnable {
        private Intersection intersection;
        private Random random = new Random();
        public TrainB(Intersection intersection) {
            this.intersection = intersection;
        }
        @Override
        public void run() {
            while (true) {
                long sleepingTime = random.nextInt(5);
                try {
                    Thread.sleep(sleepingTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                intersection.takeRoadA();
            }
        }
    }
    public static class Intersection {
        private Object roadA = new Object();
        private Object roadB = new Object();

        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("Road A is locked by "+ Thread.currentThread().getName());
                synchronized (roadB) {
                    System.out.println("Train is passing through road A");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        public void takeRoadB() {
//            synchronized (roadA) {
            synchronized (roadB) {
                System.out.println("Road B is locked by "+ Thread.currentThread().getName());
                synchronized (roadA) {
//                synchronized (roadB) {
                    System.out.println("Train is passing through road B");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // to avoid deadlock, use the same ordering of locks, if u cant avoid circular locks
    // deadlock detection, watchdog.
    // Thread interruption, (not possible with synchronized)
    // tryLock operations, (not possible with synchronized)
}
