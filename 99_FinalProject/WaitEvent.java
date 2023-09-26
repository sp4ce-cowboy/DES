/**
 * The {@code WaitEvent} class extends the Event class
 * and provides for the implementation of the Customer
 * waiting process. Customers who wait in a Server queue
 * need to wait until the specific server is available.
 * Customers who wait in a self-checkout queue need to
 * wait until any self-checkout counter is available.
 */
public class WaitEvent extends Event {

    private final boolean firstInstance;

    /**
     * Constructs a WaitEvent with all parameters specified.
     *
     * @param timeStamp The timestamp associated with the WaitEvent
     * @param customer The customer associated with the WaitEvent
     * @param server The server associated with the WaitEvent
     * @param first A boolean indicating if the WaitEvent is the first instance
     */
    WaitEvent(double timeStamp, Customer customer, Server server,
            boolean first) {
        super(timeStamp, customer, server);
        this.firstInstance = first;
    }
    
    /**
     * Constructs a first instance WaitEvent.
     *
     * @param timeStamp The timestamp associated with the WaitEvent
     * @param customer The customer associated with the WaitEvent
     * @param server The server associated with the WaitEvent
     */
    WaitEvent(double timeStamp, Customer customer, Server server) {
        this(timeStamp, customer, server, true);
    }

    /**
     * The String representation of the WaitEvent. Only the first instance
     * of WaitEvents return a full string. Repeated occurences of WaitEvents
     * for the same customer are represented by empty String.
     */
    @Override
    public String toString() {
        if (firstInstance) {
            return super.toString() + "waits at " + super.getServer() + "\n";
        } else {
            return "";
        }
    }

    /**
     * WaitEvent's implementation of the nextEvent method. For WaitEvents
     * associated with human servers, a non-first instance WaitEvent is
     * returned with the human server's updated nextFree time until the 
     * customer associated can be served by the specific server, afterwhich
     * a ServeEvent is returned. For WaitEvents associated with self-checkout
     * counters, a non-first instance WaitEvent is returned with the earliest 
     * available self-checkout counter's nextFree time until the customer
     * associated can be served by any self-checkout counter, afterwhich
     * a ServeEvent is returned.
     *
     * @param input The input list of servers
     * @return A {@code Pair<Event, ImList<Server>>} pair object.
     */
    @Override
    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> input) {

        /* Flow: 
         * if not SC:
         *     if can serve, serve, minus Queue
         *     else return non-first Wait
         * if SC:
         *     if any can serve, serve, minus Queue for all
         *     else return silentWait with earliest free SC
         */

        ImList<Server> currentList = input;
        int currentServerNumber = super.getServer().getServerId();
        Server currentServer = currentList.get(currentServerNumber - 1);
        Customer currentCustomer = super.getCustomer();
        double waitTimeStamp = super.getTimeStamp();

        if (!currentServer.isSelf()) { // MAIN IF -- HUMAN SERVER
            if (currentServer.canServe(waitTimeStamp)) { // Sub-If -- HS Serve

                currentServer = currentServer
                    .updateServerState(waitTimeStamp, false);

                currentServer = currentServer.minusOneServerQ();

                Event tempServeEvent = new ServeEvent(waitTimeStamp,
                        currentCustomer, currentServer);

                currentList = currentList.set(currentServerNumber - 1, 
                        currentServer);

                return new Pair<Event, ImList<Server>>(tempServeEvent, 
                        currentList); // Return HS ServeEvent

            } else { // Sub-Else -- HS non-first WaitEvent 

                Event tempSilentWait = 
                    new WaitEvent(currentServer.getNextFree(), 
                            currentCustomer, currentServer, false);

                return new Pair<Event, ImList<Server>>(tempSilentWait, 
                        currentList); // Return HS non-first WaitEvent

            }

        } else { // MAIN ELSE -- SELF-CHECKOUT COUNTER
            
            boolean served = false;
            for (Server s : currentList) { // Check if any self-checkout free 
                if (s.isSelf() && s.canServe(waitTimeStamp)) {
                    currentServer = s.updateServerState(waitTimeStamp,false);
                    currentList = currentList.set(s.getServerId() - 1, 
                            currentServer);
                    currentList = QManager.minusOneAllQ(currentList);         

                    served = true; 
                    break;
                }
            } 

            if (served == true) { // Sub-If -- SC Counter ServeEvent

                Event tempServeEvent = new ServeEvent(waitTimeStamp,
                        currentCustomer, currentServer);

                return new Pair<Event, ImList<Server>>(tempServeEvent,
                        currentList); // Return SC ServeEvent

            } else { // Sub-Else -- Earliest SC Counter non-first WaitEvent
                double earliestAvailable = currentServer.getNextFree();
                int selfCheckId = currentServer.getServerId();
                
                for (Server s : currentList) {
                    if (s.isSelf()) {
                        if (s.getNextFree() < earliestAvailable) {
                            earliestAvailable = s.getNextFree();
                            selfCheckId = s.getServerId();
                        }
                    }
                }

                currentServer = currentList.get(selfCheckId - 1);
                Event tempSilentWait = 
                    new WaitEvent(currentServer.getNextFree(),
                            currentCustomer, currentServer, false);

                return new Pair<Event, ImList<Server>>(tempSilentWait, 
                        currentList); // Return SC non-first WaitEvent
            }

        } // MAIN ELSE END


    } // nextEvent END


} // class END

