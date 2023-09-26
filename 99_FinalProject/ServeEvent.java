/**
 * The {@code ServeEvent} class extends the Event class
 * and provides for the implementation of the Customer
 * serving process.
 */
public class ServeEvent extends Event {

    /**
     * Constructs a ServeEvent with all parameters specified.
     *
     * @param timeStamp The timestamp at which serving begins
     * @param customer The customer being served
     * @param server The server associated with the ServeEvent
     */
    ServeEvent(double timeStamp, Customer customer, Server server) {
        super(timeStamp, customer, server);
    }

    /**
     * Returns a String representation of the ServeEvent.
     *
     * @return {@code String} inclusive of Event and Server Id.
     */
    @Override 
    public String toString() {
        return super.toString() + "serves by " + super.getServer() + "\n";
    }

    /**
     * Method for adding the waiting time of the customer. The
     * duration of waiting is determined by the time difference
     * between the start of serving the customer and the arrival
     * time of the customer.
     *
     * @return {@code double} value of the calculated waiting time.
     */
    @Override
    public double waitAdd() {
        return super.getTimeStamp() - super.getCustomer().getArrivalTime();
    }

    /**
     * ServeEvent's implementation of the nextEvent method. The ending
     * time of service is determined by invoking the customer's supplied
     * service duration time. The ending time is then used to generate a
     * DoneEvent which is returned together with the updated availability
     * of the server.
     * 
     * @return A {@code Pair<Event, ImList<Server>>} pair object.
     */
    @Override
    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> inputList) {
        
        ImList<Server> currentList = inputList;
        Customer currentCustomer = super.getCustomer(); 
        Server currentServer = super.getServer();
        int currentServerId = currentServer.getServerId();

        double endingTime = super.getTimeStamp() + 
            currentCustomer.getServiceTime(); // serviceTime invoked 

        currentServer = currentList.get(currentServerId - 1); 
        currentServer = currentServer.updateServerState(endingTime, true);
        // Server is set to be available at the endingTime 
                                                                    
        currentList = currentList.set(currentServerId - 1, currentServer); 
        // Server list is updated with the updated Server

        Event currentDoneEvent = new 
            DoneEvent(endingTime, currentCustomer, currentServer);
        
        return new Pair<Event, ImList<Server>>(
                currentDoneEvent, currentList); // DoneEvent returned
    }


}
