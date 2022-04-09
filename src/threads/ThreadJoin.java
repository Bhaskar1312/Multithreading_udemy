package threads;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ThreadJoin {
    public static class FactorialThread extends Thread {
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;
        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }
        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        private BigInteger factorial(long n) {
            BigInteger tempRes = BigInteger.ONE;
            for(long i=n;i>0;i--) {
                tempRes = tempRes.multiply(new BigInteger(Long.toString(i)));
            }
            return tempRes;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        List<Long> inputNumbers = List.of(0L, 3422L, 2455L, 23L, 55566l);

        List<FactorialThread> threads = new ArrayList<>();

        for(long input : inputNumbers) {
            threads.add(new FactorialThread(input));
        }

        for(Thread thread: threads) {
//            thread.setDaemon(true); // Whenever the last non-daemon thread terminates, all the daemon threads will be terminated automatically.
            thread.start();
        }

        for(FactorialThread thread: threads) {
            if(thread.isFinished) {
                System.out.println(thread.result);
            } else {
                System.out.println("The calc for "+ thread.inputNumber + " is still in progress");
            }
        }

        //race condition between thread.start() and thread.isFinished(checking for results) racing independently for results

        for(Thread thread: threads) {
            thread.join(); // wait for the thread to join
//            thread.join(2000); // after 2 secs, terminate anyway even if not complete
        }

        for(FactorialThread thread: threads) {
            if(thread.isFinished) {
                System.out.println(thread.result);
            } else {
                System.out.println("The calc for "+ thread.inputNumber + " is still in progress");
            }
        }
    }
}
