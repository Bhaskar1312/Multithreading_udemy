package threads;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class ComplexCalculation {
//    public static void main(String[] args) throws InterruptedException {
//        ComplexCalculation cc = new ComplexCalculation();
//        System.out.println(cc.calculateResult(BigInteger.TWO, BigInteger.TEN, BigInteger.TEN, BigInteger.TWO));
//
//    }
    public BigInteger calculateResult(BigInteger base1, BigInteger power1, BigInteger base2, BigInteger power2) throws InterruptedException {
        List<PowerCalculatingThread> list = new ArrayList<>();
        list.add(new PowerCalculatingThread(base1, power1));
        list.add(new PowerCalculatingThread(base2, power2));
        for(Thread thread: list) {
            thread.start();
        }
        for(Thread thread: list) {
            thread.join();
        }
        BigInteger result = BigInteger.ZERO;
        for(PowerCalculatingThread thread: list) {
            result = result.add(thread.getResult());
        }
        /*
            Calculate result = ( base1 ^ power1 ) + (base2 ^ power2).
            Where each calculation in (..) is calculated on a different thread
        */
        return result;
    }

    private static class PowerCalculatingThread extends Thread {
        private BigInteger result = BigInteger.ONE;
        private BigInteger base;
        private BigInteger power;

        public PowerCalculatingThread(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
           /*
           Implement the calculation of result = base ^ power
           */
            if(base.compareTo(BigInteger.ZERO)==0) {
                result = BigInteger.ZERO;
                return;
            } else {
                for(BigInteger i=BigInteger.ONE;i.compareTo(power) <=0;i = i.add(BigInteger.ONE))  {
                    result = result.multiply(base);
                }
            }

        }

        public BigInteger getResult() { return result; }
    }
}