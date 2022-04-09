package atomic2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class LockFreeDS {
    public static void main(String[] args) throws InterruptedException {
        int N = 100000;
        Random random = new Random();

        LockFreeStack<Integer> stack = new LockFreeStack<>(); //415944511 operations were performed in 10 seconds
//        StandardStack<Integer> stack = new StandardStack<>(); // 305313744 operations were performed in 10 seconds
        for(int i=0;i<N;i++) {
            stack.push(random.nextInt());
        }

        List<Thread> threads = new ArrayList<>();
        int pushingThreads = 2;
        int poppingThreads = 2;

        for(int i=0;i< pushingThreads; i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    stack.push(random.nextInt());
                }
            });

            thread.setDaemon(true);
            threads.add(thread);
        }

        for(int i=0;i<poppingThreads;i++) {
            Thread thread = new Thread(() -> {
                while (true) {
                    stack.pop();
                }
            });
            thread.setDaemon(true);
            threads.add(thread);
        }

        for(Thread thread: threads) {
            thread.start();
        }
        Thread.sleep(10000);
        System.out.println(String.format("%d", stack.size())+" operations were performed in 10 seconds" );

    }
    public static class LockFreeStack<T> {
        private AtomicReference<StackNode<T>> head = new AtomicReference<>();
        private AtomicInteger counter = new AtomicInteger(0); // #ops, not size

        public void push(T value) {
            StackNode<T> newHeadNode = new StackNode<>(value);
            while (true) {  //need multiple tries
                StackNode<T> currHeadNode = head.get();
                newHeadNode.next = currHeadNode;
                if(head.compareAndSet(currHeadNode, newHeadNode)) {
                    break;
                } else {
                    LockSupport.parkNanos(1);
                }
            }
            counter.incrementAndGet();
        }
        public T pop() {
            StackNode<T> currHeadNode = head.get();
            StackNode<T> newHeadNode;
            while (currHeadNode!= null) {
                newHeadNode = currHeadNode.next;
                if(head.compareAndSet(currHeadNode, newHeadNode)) {
                    break;
                } else {
                    //head changed by some other thread
                    LockSupport.parkNanos(1);
                    currHeadNode = head.get();
                }
            }
//            counter.decrementAndGet();
            counter.incrementAndGet();
            return currHeadNode==null? null: currHeadNode.value;
        }

        public int size() {
            return counter.get();
        }
    }

    public static class StandardStack<T> {
        private StackNode<T> head;
        private int counter =0;

        public synchronized void push(T value) {
            StackNode<T> newHeadNode = new StackNode<>(value);
            newHeadNode.next = head;
            head = newHeadNode;
            counter++;
        }
        public synchronized T pop() {
            if(head == null) {
                counter++;
                return null;
            }
            T value = head.value;
            head = head.next;
//            counter--;
            counter++;
            return value;
        }
        public int size() {
            return counter;
        }
    }

    private static class StackNode<T> {
        public T value;
        public StackNode<T> next;
        public StackNode(T value) { //}, StackNode<T> next) {
            this.value = value;
//            this.next = next;
        }
    }
}
