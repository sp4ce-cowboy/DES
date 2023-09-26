import java.util.function.Supplier;

/**
 * A Self-Checkout class that extends the Server Class.
 */ 
public class SelfCheck extends Server {
    
    private static final Supplier<Double> zeroRestTime = () -> 0.0;

    /**
     * Constructs a SelfCheck using the super class constructor
     * that specifies all parameters, with the boolean representation 
     * of self-checkout (by the "self" parameter) set to true.
     *
     * @param id the integer id of the SelfCheck
     * @param nextFree the SelfCheck's next free time
     * @param qmax the SelfCheck's max queue size
     * @param serverQ the integer value of the current size of queue
     * @param avail the boolean availability of the SelfCheck
     */
    SelfCheck(int id, double nextFree, int qmax, int serverQ,
            boolean avail) {
        super(id, nextFree, qmax, serverQ, avail, zeroRestTime, true);
    }

    /**
     * Constructs a SelfCheck with default zero {@code nextFree}
     * and queue.
     */
    SelfCheck(int id, int qmax) {
        this(id, 0.0, qmax, 0, true);
    }

}
