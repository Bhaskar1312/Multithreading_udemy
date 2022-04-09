package conditionNotify;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class MatrixMultiply {
    private static final int N = 10;
    private static final String INPUT_FILE = "./out/matrices";
    private static final String OUTPUT_FILE = "./out/matrices_result.txt";

    public static void main(String[] args) throws IOException {
        ThreadSafeQueue queue = new ThreadSafeQueue();
        File inputFile = new File(INPUT_FILE);
        File outputFile = new File(OUTPUT_FILE);

        MatrixReaderProducer matrixReaderProducer = new MatrixReaderProducer(new FileReader(inputFile), queue);
        MatrixMultiplierConsumer matrixConsumer = new MatrixMultiplierConsumer(new FileWriter(outputFile), queue);

        matrixConsumer.start();
        matrixReaderProducer.start();
    }

//    private static class MatrixMultiplierConsumer extends Thread {
//        private ThreadSafeQueue queue;
//        private FileWriter fileWriter;
//
//        public MatrixMultiplierConsumer(FileWriter fileWriter, ThreadSafeQueue queue) {
//            this.fileWriter = fileWriter;
//            this.queue = queue;
//        }
//
//        private static void saveMatrixToFile(FileWriter fileWriter, float[][] matrix) throws IOException {
//            for (int r = 0; r < N; r++) {
//                StringJoiner stringJoiner = new StringJoiner(", ");
//                for (int c = 0; c < N; c++) {
//                    stringJoiner.add(String.format("%.2f", matrix[r][c]));
//                }
//                fileWriter.write(stringJoiner.toString());
//                fileWriter.write('\n');
//            }
//            fileWriter.write('\n');
//        }
//
//        @Override
//        public void run() {
//            while (true) {
//                MatrixPair matricesPair = queue.remove();
//                if (matricesPair == null) {
//                    System.out.println("No more matrices to read from the queue, consumer is terminating");
//                    break;
//                }
//
//                float[][] result = multiplyMatrices(matricesPair.matrix1, matricesPair.matrix2);
//
//                try {
//                    saveMatrixToFile(fileWriter, result);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            try {
//                fileWriter.flush();
//                fileWriter.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        private float[][] multiplyMatrices(float[][] m1, float[][] m2) {
//            float[][] result = new float[N][N];
//            for (int r = 0; r < N; r++) {
//                for (int c = 0; c < N; c++) {
//                    for (int k = 0; k < N; k++) {
//                        result[r][c] += m1[r][k] * m2[k][c];
//                    }
//                }
//            }
//            return result;
//        }
//    }

    private static class MatrixMultiplierConsumer extends Thread {
        private ThreadSafeQueue queue;
        private FileWriter filewriter;

        public  MatrixMultiplierConsumer(FileWriter writer, ThreadSafeQueue queue) {
            this.filewriter = writer;
            this.queue = queue;
        }

        private static void saveResultToFile(FileWriter fileWriter, float[][] matrix) throws IOException {
            for (int r = 0; r < N; r++) {
                StringJoiner stringJoiner = new StringJoiner(", ");
                for (int c = 0; c < N; c++) {
                    stringJoiner.add(String.format("%.2f", matrix[r][c]));
                }
                fileWriter.write(stringJoiner.toString());
                fileWriter.write('\n');
            }
            fileWriter.write('\n');
        }

        @Override
        public void run() {
            while (true) {
                MatrixPair matrixPair = queue.remove();
                if (matrixPair == null) {
                    System.out.println("No more matrices to be removed, consumer is terminating");
                    break;
                }

                float[][] res = multiplyMatrices(matrixPair.matrix1, matrixPair.matrix2);

                try {
                    saveResultToFile(filewriter, res);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
                try {
                    filewriter.flush();
                    filewriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }

        private float[][] multiplyMatrices(float[][] m1, float[][] m2) {
            float[][] res = new float[N][N];
            for(int r=0; r<N; r++) {
                for(int c=0; c<N; c++) {
                    for(int k = 0; k<N; k++) {
                        res[r][c] += m1[r][k] * m2[k][c];
                    }
                }
            }
            return res;
        }
    }

    private static class MatrixReaderProducer extends Thread {
        private Scanner scanner;
        private ThreadSafeQueue queue;

        public MatrixReaderProducer(FileReader reader, ThreadSafeQueue queue) {
            this.scanner = new Scanner(reader);
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                float[][] matrix1 = readMatrix();
                float[][] matrix2 = readMatrix();
                if (matrix1 == null || matrix2 == null) {
                    queue.terminate();
                    System.out.println("No more matrices to read. Producer Thread is terminating");
                    return;
                }

                MatrixPair matricesPair = new MatrixPair();
                matricesPair.matrix1 = matrix1;
                matricesPair.matrix2 = matrix2;

                queue.add(matricesPair);
            }
        }

        private float[][] readMatrix() {
            float[][] matrix = new float[N][N];
            for (int r = 0; r < N; r++) {
                if (!scanner.hasNext()) {
                    return null;
                }
                String[] line = scanner.nextLine().split(",");
                for (int c = 0; c < N; c++) {
                    matrix[r][c] = Float.valueOf(line[c]);
                }
            }
            scanner.nextLine();
            return matrix;
        }
    }

//    private static class MatrixReaderProducer extends Thread {
//        private Scanner scanner;
//        private ThreadSafeQueue queue;
//
//        public MatrixReaderProducer(FileReader reader, ThreadSafeQueue queue) {
//            this.scanner = new Scanner(reader);
//            this.queue = queue;
//        }
//
//        @Override
//        public void run() {
//            while (true) {
//                float[][] matrix1 = readMatrix();
//                float[][] matrix2 = readMatrix();
//                if (matrix1 == null || matrix2 == null) {
//                    queue.terminate();
//                    System.out.println("No more matrices to read. Producer Thread is terminating");
//                    return;
//                }
//
//                MatrixPair matricesPair = new MatrixPair();
//                matricesPair.matrix1 = matrix1;
//                matricesPair.matrix2 = matrix2;
//
//                queue.add(matricesPair);
//            }
//        }
//
//        private float[][] readMatrix() {
//            float[][] matrix = new float[N][N];
//            for (int r = 0; r < N; r++) {
//                if (!scanner.hasNext()) {
//                    return null;
//                }
//                String[] line = scanner.nextLine().split(",");
//                for (int c = 0; c < N; c++) {
//                    matrix[r][c] = Float.valueOf(line[c]);
//                }
//            }
//            scanner.nextLine();
//            return matrix;
//        }
//    }

    private static class ThreadSafeQueue {
        private Queue<MatrixPair> queue = new LinkedList<>();
        private boolean isEmpty = true;
        private boolean isTerminate = false; //producer has nothing more to offer, consumer should terminate

        public synchronized void add(MatrixPair matrixPair) { //called by producer
            queue.add(matrixPair);
            isEmpty = false;
            //notify consumer any(0-1), waiting for work
            notify();
        }

        public synchronized MatrixPair remove() { //called by consumer to consume result
            while (isEmpty && !isTerminate) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if(queue.size() == 1) {
                isEmpty = true;
            }

            if(queue.size()==0 && isTerminate) {
                return null;
            }

            System.out.println("Current queue size is: "+ queue.size());
            return queue.remove();
        }

        public synchronized void terminate() { //called by consumer to let producer it terminated
            isTerminate = true;
            //wake up all potentially waiting consumer threads
            notifyAll();
        }
    }

//private static class ThreadSafeQueue {
//    private Queue<MatrixPair> queue = new LinkedList<>();
//    private boolean isEmpty = true;
//    private boolean isTerminate = false;
//    private static final int CAPACITY = 5;
//
//    public synchronized void add(MatrixPair matricesPair) {
//        while (queue.size() == CAPACITY) {
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        queue.add(matricesPair);
//        isEmpty = false;
//        notify();
//    }
//
//    public synchronized MatrixPair remove() {
//        MatrixPair matricesPair = null;
//        while (isEmpty && !isTerminate) {
//            try {
//                wait();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (queue.size() == 1) {
//            isEmpty = true;
//        }
//
//        if (queue.size() == 0 && isTerminate) {
//            return null;
//        }
//
//        System.out.println("queue size " + queue.size());
//
//        matricesPair = queue.remove();
//        if (queue.size() == CAPACITY - 1) {
//            notifyAll();
//        }
//        return matricesPair;
//    }
//
//    public synchronized void terminate() {
//        isTerminate = true;
//        notifyAll();
//    }
//}

    private static class MatrixPair {
        public float[][] matrix1;
        public float[][] matrix2;
    }
}
