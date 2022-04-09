package threads;

public class NewThread extends Thread{
    @Override
    public void run() {
//        System.out.println("Hello from " + Thread.currentThread().getName());
        System.out.println("Hello from " + this.getName());
        super.run();

    }

    public static void main(String[] args) {
        Thread newThread = new NewThread();
        newThread.start();

    }
}
