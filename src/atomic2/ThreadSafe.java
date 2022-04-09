package atomic2;

import java.util.concurrent.atomic.AtomicReference;

public class ThreadSafe {
    private static class Metric {
        private static class InternalMetric{
            public long count;
            public long sum;
        }

        private AtomicReference<InternalMetric> internalMetric = new AtomicReference<>(new InternalMetric());

        public void addSample(long sample) {
            InternalMetric currentState;
            InternalMetric newState;
            do {
                currentState = internalMetric.get();
                newState = new InternalMetric();
                newState.sum = currentState.sum + sample;
                newState.count = currentState.count + 1;
            } while (!internalMetric.compareAndSet(currentState, newState));
        }

        public double getAverage() {
            InternalMetric newResetState = new InternalMetric();
            InternalMetric currentState;
            double average;
            do {
                currentState = internalMetric.get();
                average = (double)currentState.sum / currentState.count;
            } while (!internalMetric.compareAndSet(currentState,  newResetState));

            return average;
        }
    }
}
