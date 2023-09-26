import java.util.function.Supplier;

/**
 * A Server class that makes immutable Server objects containing an 
 * identifier, the time stamp of the Server's next 
 * available time, the maximum queue size, 
 * the current number of customers waiting in queue, the 
 * current availability of the Server, a
 * supplied value for the Server's restTime, and an indicator to 
 * determine if the Server is a Human Server or a Self-Checkout.
 */

public class Server {

    private final int serverId;
    private final double nextFree;
    private final int qmax;
    private final int serverQ;
    private final boolean available;
    private final Supplier<Double> restTime;
    private final boolean selfCheck;


    /**
     * Constructs a Server with all parameters specified.
     * @param serverId the integer id of the server
     * @param nextFree the server's next free time
     * @param qmax the max queue size for the server
     * @param serverQ the integer value of the size of the server queue
     * @param avail the boolean availability of the server
     * @param restTime the server's restTime supplier.
     * @param self the boolean representation of self-checkout
     */
    public Server(int serverId, double nextFree, int qmax, int serverQ, 
            boolean avail, Supplier<Double> restTime, boolean self) {
        this.serverId = serverId;
        this.nextFree = nextFree;
        this.qmax = qmax;
        this.serverQ = serverQ;
        this.available = avail;
        this.restTime = restTime;
        this.selfCheck = self;
    }

    /**
     * Constructs a Human Server with all parameters specified.
     *
     * @param serverId the integer id of the server
     * @param nextFree the server's next free time
     * @param qmax the max queue size for the server
     * @param serverQ the integer value of the size of the server queue
     * @param avail the boolean availability of the server
     * @param restTime the server's restTime supplier.
     */
    public Server(int serverId, double nextFree, int qmax, int serverQ, 
            boolean avail, Supplier<Double> restTime) {
        this(serverId, nextFree, qmax, serverQ, avail, restTime, false);
    }

    /**
     * Constructs a Human Server with default zero {@code nextFree},
     * server queue and availability.
     *
     * @param serverId the integer id of the server
     * @param qmax the max queue size for the server
     * @param restTime the server's restTime supplier. 
     */
    public Server(int serverId, int qmax, Supplier<Double> restTime) {
        this(serverId, 0.0, qmax, 0, true, restTime);
    }

    /**
     * Returns the id of the server.
     * 
     * @return {@code Integer} value of the server. 
     */
    public int getServerId() {
        return this.serverId;
    }
    
    /**
     * Returns the server's next free time.
     * @return the double value of the server's nextFree time.
     */
    public Double getNextFree() {
        return this.nextFree;
    }

    /** 
     * Returns the server's current queue.
     * @return the integer value of server's current queue.
     */
    public int getServerQ() { // returns the serverQ int
        return this.serverQ;
    }

    /** 
     * Returns the server's availability.
     * @return {@code true} if the server is available.
     */
    public boolean isAvailable() { //returns true if not serving
        return this.available;
    }

    /** 
     * Returns the type of server.
     * 
     * @return {@code false} if human server, {@code true} if self-check out.
     */
    public boolean isSelf() {
        return this.selfCheck;
    }

    /** 
     * Checks if the server is able to queue customers.
     * @return {@code true} if server queue is less than qmax.
     */
    public boolean canQueue() { // returns true if canQueue
        return this.serverQ < this.qmax;
    }

    /** 
     * Checks if server can serve at a given time.
     * Returns true if the server is available and the
     * server's next free time is greater than or equal to the given
     * time.
     *
     * @param time the input timestamp to be checked.
     * @return {@code true} if the server can serve.
     */
    public boolean canServe(double time) { 
        return (this.available && time >= this.nextFree);
    }

    /** 
     * Update the state of the server. Given an input time and
     * state, the server is updated with all other fields constant.
     * 
     * @param time the timestamp to be updated with
     * @param state the availability to be updated with
     * @return returns an updated {@code Server}.
     */
    public Server updateServerState(double time, boolean state) {
        return new Server(this.serverId, time, this.qmax, 
                this.serverQ, state, this.restTime, this.selfCheck);
    }
   
    /** 
     * Private method to internally set the the server queue.
     * 
     * @param number the integer value to set the server queue to.
     * @return returns an update {@code Server}.
     */
    private Server setServerQ(int number) {
        return new Server(this.serverId, this.nextFree, 
                this.qmax, number, this.available, 
                this.restTime, this.selfCheck);
    }

    /** 
     * Increment the server queue by one. Invokes the private
     * setServerQ method and adds 1 to the server queue.
     * 
     * @return returns a {@code Server} with a queue incremented by 1
     */
    public Server addOneServerQ() { // public method to add to ServerQ
        return this.setServerQ(this.serverQ + 1);
    }

    /** 
     * Decrement the server queue by one. Invokes the private
     * setServerQ method and subtracts 1 from the server queue.
     * 
     * @return returns a {@code Server} with a queue decremented by 1.
     */
    public Server minusOneServerQ() {
        return this.setServerQ(this.serverQ - 1);
    }

    /** 
     * Adds rest time to the server's next free time. Invokes the
     * {@code updateServerState} method with the server's current
     * next free time and the server's rest time generated from
     * {@code Supplier<Double> restTime}.
     * 
     * @return returns a Server with an updated next free time.
     */
    public Server addRestTime() {
        return this.updateServerState(this.nextFree + this.restTime.get(), this.available);
    }

    /** 
     * Returns a string representation of the server. The string
     * representation is the {@code String.valueOf} the server's
     * serverId, preceded by "self-check" if the server is not a
     * human server.
     * 
     * @return returns the {@code toString()} of the object.
     */
    @Override
    public String toString() {
        if (!this.selfCheck) {
            return String.valueOf(this.serverId);
        } else {
            return "self-check " + String.valueOf(this.serverId);
        }
    }

}
