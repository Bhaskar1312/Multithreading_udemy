package threads;

import javax.swing.plaf.TableHeaderUI;
import java.math.BigInteger;

public class ThreadTermination {
    public static void main(String[] args) {
        Thread thread = new Thread(new BlockingTask());
        thread.start();

        thread.interrupt();

        thread = new Thread(new LongComputation(BigInteger.TWO, new BigInteger("10")));
        thread.start();

        thread = new Thread(new LongComputation(BigInteger.TWO, new BigInteger("1000000")));
        thread.start();
        thread.interrupt(); //will not interrupt if it is running, so add isInterrupted to check for interruptions

        thread = new Thread(new LongComputation(BigInteger.TEN, new BigInteger("1000000")));
        thread.setDaemon(true);
        //to prevent a thread from blocking our app from exiting, we set the thread to be a daemon thread
        thread.interrupt();
    }
    public static class BlockingTask extends Thread {
        public void run() {
            try {
                Thread.sleep(500000);
            } catch (InterruptedException e) {
                System.out.println("Exiting the blocking thread");
            }
        }
        //when can we interrupt a thread?
        //if thread is executing a method, that throws an InterruptedException
        //if the thread's code is handling the interrupt signal explicitly
    }
    public static class LongComputation extends Thread {
        BigInteger base;
        BigInteger power;
        public LongComputation(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base + "^" + power + "="+ exp());
        }
        public BigInteger exp() {
            BigInteger res = BigInteger.ONE;
            for(BigInteger i=BigInteger.ZERO; i.compareTo(power) !=0; i = i.add(BigInteger.ONE)) {
                //
                if(Thread.interrupted()) {
                    System.out.println("Thread is interrupted");
                    return BigInteger.ZERO;
                }
                res = res.multiply(base);
            }
            return res;
        }
    }
}
