import java.util.function.Supplier;

/**
 * A Discrete Event Simulator that simulates the sequential
 * generation and execution of discrete events in the context of a shop   
 * that contains servers and self-checkout counters that serve customers
 * that arrive at specified times and have unspecified service
 * times. A customer that arrives will be served by either a
 * server or a self-checkout counter if available. If the customer
 * cannot be served immediately, they wait in a queue of specified
 * length. If they are unable to wait, they leave the shop.
 * 
 * <p>A customer's arrival triggers a sequence of events that simulate
 * a real-world scenario. A customer can arrive, then be served,
 * wait, or leave. Some time later the customer would be done
 * serving by the server or a self-checkout counter.
 * A human server would then take a break for an unspecified duration
 * of time, but a self-checkout counter does not need to rest.
 * 
 * <p>All events generate another event which, if not the same event,
 * is added to an immutable implementation of a PriorityQueue, PQ.
 * Events are polled and the relevant information is contained
 * within a String that is printed line by line by the Main
 * class. The average waiting time per customer, number of customers
 * served, and the number of customers who leave without being
 * served is output as the final line.
 *
 * @author Rubesh S
 * @version CS2030 AY22/23 Semester 2
 */
public class Simulator {

    private final int numOfServers;
    private final int qmax;
    private final int selfChecks;
    private final ImList<Double> arrivalTimes;
    private final Supplier<Double> serviceTimes;
    private final Supplier<Double> restTimes;

    /**
     * Constructs the Simulator.
     * @param servers number of servers in the shop
     * @param selfChecks number of self-checkout counters
     * @param qmax the maximum queue length 
     * @param arrivalTimes the list of customer arrival times
     * @param serviceTimes the supplier of service times.
     */
    Simulator(int servers, int selfChecks, int qmax, ImList<Double> arrivalTimes,
            Supplier<Double> serviceTimes, Supplier<Double> restTimes) {
        this.numOfServers = servers;
        this.qmax = qmax;
        this.selfChecks = selfChecks;
        this.serviceTimes = serviceTimes;
        this.arrivalTimes = arrivalTimes;
        this.restTimes = restTimes;
    }

    /**
     * Returns an ImList of Customers for each arrival time in
     * arrivalTimes.
     *
     * @return the list of customers 
     */
    private ImList<Customer> makeCustomerList() {

        ImList<Customer> customerList = new ImList<Customer>();
        int custCounter = 1;

        for (Double a : arrivalTimes) {
            customerList = customerList.add(
                    new Customer(custCounter, a, this.serviceTimes));
            custCounter++;
        }

        return customerList;
    }

    /**
     * Returns  an ImList of default Servers with the specified
     * qmax and restTimes and Self-Checkouts with the specified
     * qmax.
     *
     * @return the list of Servers and Self-Checkouts 
     */
    private ImList<Server> makeServerList() {

        ImList<Server> serverList = new ImList<Server>();
        for (int i = 1; i <= this.numOfServers; i++) {
            serverList = serverList.add(new Server(i, this.qmax, this.restTimes));
        }
       
        /* Numbering of self-checkouts starts from k + 1 where k is
         * the number of servers (numOfServers)
         */
        for (int j = this.numOfServers + 1; j <= this.numOfServers + 
                this.selfChecks; j++) {
            serverList = serverList.add(new SelfCheck(j, this.qmax));
        }

        return serverList;
    }

    /**
     * Returns an initial PQ with the arrival events of the customers
     * created based on the list of customers.
     * 
     * @return the PQ with ArriveEvents
     */
    private PQ<Event> makePQ() {

        ImList<Customer> tempCustomerList = this.makeCustomerList();
        PQ<Event> pq = new PQ<Event>(new EventComp());

        for (int i = 0; i < tempCustomerList.size(); i++) {
            Customer tempCustomer = tempCustomerList.get(i);
            double tempArrival = tempCustomer.getArrivalTime();

            ArriveEvent tempArrivalEvent = new 
                ArriveEvent(tempArrival, tempCustomer);

            pq = pq.add(tempArrivalEvent);
        }
        return pq;
    }

    /**
     * Main simulate method. While the PQ is not empty, events are
     * polled and the next event generated is added back into the
     * PQ, unless the event returns itself, in which case the
     * event is not added back. The String representation of the
     * event is added to {@code finalOutput}. The list of servers
     * returned overwrites the existing list of servers and thereby
     * updating the state of the Servers with each event.
     *
     * <p>All events have standardized statistics reporting. Each event is
     * queried for the number of customers served, left, and waiting time.
     * Only the relevant events will output a non-zero value. 
     *
     * @return the String representation of the entire Simulation
     */

    public String simulate() { 
        String finalOutput = "";        // final output to be printed
        int servedNumber = 0;           // number of customers served
        int leftNumber = 0;             // number of customers left
        double totalWaitingTime = 0.0;  // total waiting time
        double averageTime = 0.0;       // average waiting time

        PQ<Event> pq = this.makePQ();
        ImList<Server> serverList = this.makeServerList(); // Initialize serverList

        while (!pq.isEmpty()) {
            Event currentEvent = pq.poll().first(); // Event retrived from polled pair
            pq = pq.poll().second();                // Subsequent PQ retrived from polled pair
            
            Pair<Event, ImList<Server>> eventServerPair = // Generate nextEvent
                currentEvent.nextEvent(serverList); 
            
            Event nextEvent = eventServerPair.first();    // Next event retrieved from pair
            ImList<Server> nextServers = eventServerPair.second(); // Server list retrieved

            
            if (!currentEvent.equals(nextEvent)) {
                pq = pq.add(nextEvent); // Add next event back to PQ if not the same event 
            }

            totalWaitingTime += currentEvent.waitAdd(); // All statistics updated
            servedNumber += currentEvent.add();
            leftNumber += currentEvent.left();
            finalOutput += currentEvent.toString();
            serverList = nextServers;  // list of servers is updated 

        }
        
        if (totalWaitingTime > 0) {    // to prevent zero division error
            averageTime = totalWaitingTime / servedNumber;
        } 

        String stats = String.format("[%.3f %d %d]", averageTime,
                servedNumber, leftNumber); // Statistics formatting
        return finalOutput + stats;
        
    }

}
