/**
 * The {@code DoneEvent} class extends the Event class
 * and provides for the implementation of the Customer
 * being done serving process.
 */
public class DoneEvent extends Event {

    /**
     * Constructs a DoneEvent with all specified parameters.
     *
     * @param timeStamp The timestamp associated with the DoneEvent
     * @param customer The customer associated with the DoneEvent
     * @param server The server associated with the DoneEvent
     */
    DoneEvent(double timeStamp, Customer customer, Server server) {
        super(timeStamp, customer, server);
    }

    /**
     * Method for adding served customers. Called in the simulate
     * method of the Simulator class.
     *
     * @return {@code int} value of 1.
     */
    @Override
    public int add() {
        return 1;
    }

    /**
     * Returns a String representation of the DoneEvent.
     *
     * @return {@code String} inclusive of Event and Server Id.
     */
    @Override
    public String toString() {
        return super.toString() + "done serving by " + super.getServer() + "\n";
    }

    /**
     * DoneEvent's implementation of the nextEvent method. Returns
     * itself and an updated Server accounting for the Server's
     * supplied rest time, which can be zero if the Server is a
     * self-checkout counter. 
     * 
     * @return A {@code Pair<Event, ImList<Server>>} pair object.
     */
    @Override
    public Pair<Event, ImList<Server>> nextEvent(ImList<Server> input) {
       
        ImList<Server> currentList = input;
        Server currentServer = super.getServer();
        int currentServerNumber = currentServer.getServerId();

        Server listServer = currentList.get(currentServerNumber - 1);
        listServer = listServer.addRestTime();
        currentList = currentList.set(currentServerNumber - 1, 
                listServer);

        return new Pair<Event, ImList<Server>>(this, currentList);
    }


}
