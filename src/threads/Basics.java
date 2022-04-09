package threads;

public class Basics {
    public static void main(String[] args) throws InterruptedException {
        System.out.println(Thread.activeCount()); // HackerVault thread, garbage collector
        System.out.println(Runtime.getRuntime().availableProcessors()); //

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //whatever the code that we write here will be run by new thread, scheduler
                System.out.println("In new thread "+ Thread.currentThread().getName());
                System.out.println("Priority " + Thread.currentThread().getPriority());
                throw new RuntimeException("something");
            }
        });
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                System.out.println("A critical error happened happened in thread "+ t.getName() + " the error is " + e.getMessage());
            }
        });
        thread.setName("New Worker thread");
        thread.setPriority(Thread.MAX_PRIORITY);
        System.out.println("Before new thread "+Thread.currentThread().getName() + " starts");
        thread.start();
        System.out.println("After new thread "+Thread.currentThread().getName() + " starts");

        Thread.sleep(5000); //instructs scheduler/OS to not schedule current thread until the time passes


    }
}
