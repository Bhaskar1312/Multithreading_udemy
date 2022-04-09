package atomic;

public class DataRace {
    public static void main(String[] args) {
        SharedClass sharedClass = new SharedClass();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < Integer.MAX_VALUE; i++) {
                sharedClass.increment();
            }
        });
        Thread thread2 = new Thread(() -> {
            for(int i=0;i<Integer.MAX_VALUE;i++) {
                sharedClass.checkForDataRace();
            }
        });
        thread1.start();
        thread2.start();
    }
    public static class SharedClass {
        private int x;
        private int y;
        public void increment() {
            x++;
//            y++;
            y = x;
        }
        public void checkForDataRace() {
            if(y>x) {
                System.out.println("y > x - Data Race is detected");
            }
        }
    }
}
