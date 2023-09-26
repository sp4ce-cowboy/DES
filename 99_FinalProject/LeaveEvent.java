/**
 * The {@code LeaveEvent} class extends the Event class
 * and provides for the implementation of the Customer
 * leaving process.
 */
public class LeaveEvent extends Event {

    /**
     * Constructs an LeaveEvent with a timestamp
     * and a customer. The {@code Event} superclass
     * constructor for creating an Event with dummy
     * servers is invoked.
     * 
     * @param timeStamp the time associated with leaving
     * @param customer the arriving customer
     */
    LeaveEvent(double timeStamp, Customer customer) {
        super(timeStamp, customer);
    }

    /**
     * Method for adding leaving customers. Called in the simulate
     * method of the Simulator class.
     *
     * @return {@code int} value of 1.
     */
    @Override
    public int left() {
        return 1;
    }

    /**
     * Returns a String representation of the LeaveEvent.
     */
    @Override
    public String toString() {
        return super.toString() + "leaves" + "\n";
    }

    /**
     * LeaveEvent's implementation of the nextEvent method. Returns
     * itself and the same input list of servers. 
     * 
     * @return A {@code Pair<Event, ImList<Server>>} pair object.
     */
    @Override
    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> input) {
        return new Pair<Event, ImList<Server>>(this, input);
    }

}
