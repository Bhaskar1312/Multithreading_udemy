package atomic;

public class MinMaxMetrics {

    // Add all necessary member variables
    volatile long min = Long.MAX_VALUE;
    volatile long max = Long.MIN_VALUE;

    /**
     * Initializes all member variables
     */
    public MinMaxMetrics() {
        // Add code here

    }

    /**
     * Adds a new sample to our metrics.
     */
    public void addSample(long newSample) {
        // Add code here
        synchronized (this){
            if (min > newSample) min = newSample;
            if (max < newSample) max = newSample;
        }
    }

    /**
     * Returns the smallest sample we've seen so far.
     */
    public long getMin() {
        // Add code here
        return min;
    }

    /**
     * Returns the biggest sample we've seen so far.
     */
    public long getMax() {
        // Add code here
        return max;
    }
}
