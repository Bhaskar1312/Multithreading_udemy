package resourcesharing;

public class SharedObject {
        public static void main(String [] args) {
            SharedClass sharedObject = new SharedClass();

            Thread thread1 = new Thread(() -> {
                while (true) {
                    sharedObject.increment();
                }
            });

            Thread thread2 = new Thread(() -> {
                while (true) {
                    sharedObject.increment();
                }
            });

            thread1.start();
            thread2.start();
            System.out.println();
        }

        static class SharedClass {
            private int counter = 0;

            public synchronized void increment() {
                this.counter++;
            }
        }
    }