/**
 * The {@code ArriveEvent} class extends the Event class
 * and provides for the implementation of the Customer
 * arrival process.
 */
public class ArriveEvent extends Event {

    /**
     * Constructs an ArriveEvent with a timestamp
     * and a customer. The {@code Event} superclass
     * constructor for creating an Event with dummy
     * servers is invoked.
     * 
     * @param timeStamp the time associated with arrival
     * @param customer the arriving customer
     */
    ArriveEvent(double timeStamp, Customer customer) {
        super(timeStamp, customer);
    }

    /**
     * Returns the String representation of the ArriveEvent.
     */
    @Override
    public String toString() {
        return super.toString() + "arrives" + "\n";
    }
    
    /** 
     * ArriveEvent's implementation of the nextEvent method. An ArriveEvent
     * generates a ServeEvent if any server or self-checkout can serve
     * the associated customer. It generates a WaitEvent otherwise, and
     * human server queues will increment by one, or self-checkout queues
     * will increment by one for all self-checkouts which is handled by the
     * static methods of the QManager class. If the customer cannot
     * be served, then a LeaveEvent is generated. The generated Event 
     * together with the final state of the servers will be returned as a 
     * Pair object with the first being the generated Event (which can be Serve, 
     * Wait, or Leave) and the associated updated list of Servers.
     *
     * @param input the input list containing servers and self-checkouts
     *
     * @return A {@code Pair<Event, ImList<Server>>} pair object.
     */
    @Override
    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> input) {

        // Setup
        ImList<Server> currentList = input;
        int servingNumber = 0;
        Customer currentCustomer = super.getCustomer();
        double currentTime = currentCustomer.getArrivalTime();

        // Trigger Serve Event
        /* for (Server s : input) {
            if (s.canServe(currentTime)) {
                servingNumber = s.getServerId();
                break;
            }
        }*/

        servingNumber = QManager.getServing(input, currentTime);

        if (servingNumber != 0) { // Only executed if any server can serve
            Server currentServer = currentList.get(servingNumber - 1);
            currentServer = currentServer
                .updateServerState(currentTime, false);

            currentList = currentList.set(servingNumber - 1, 
                    currentServer);

            Event tempServeEvent = new ServeEvent(currentTime,
                    currentCustomer, currentServer);

            return new Pair<>(tempServeEvent, currentList); // return ServeEvent
        }

        // Trigger Wait Event
        /*for (Server s : input) {
            if (s.canQueue()) {
                servingNumber = s.getServerId();
                break;
            }
        }*/

        servingNumber = QManager.getQueuing(input);

        if (servingNumber != 0) { // Only executed if any server can queue
            Server currentServer = currentList.get(servingNumber - 1);
            double currentServerFree = currentServer.getNextFree();
            
            if (!currentServer.isSelf()) { // if queueing at human server
                currentServer = currentServer.addOneServerQ();
                currentList = currentList.set(servingNumber - 1, 
                    currentServer);

            } else { // if queuing at self-checkout
                currentList = QManager.addOneAllQ(currentList); // All SC queues updated
                int id = QManager.getFirstSelfCheck(currentList); // First SC id retrieved
                currentServer = currentList.get(id - 1);
            }
            
            Event tempWaitEvent = new WaitEvent(currentTime,
                    currentCustomer, currentServer);
                
            return new Pair<>(tempWaitEvent, currentList); // return WaitEvent
        
        // Trigger Leave Event
        } else { // Only executed if no server can queue
            Event tempLeaveEvent = new LeaveEvent(currentTime,
                    currentCustomer);

            return new Pair<>(tempLeaveEvent, currentList); // return LeaveEvent
        }
    }    
   
}


