import java.util.Comparator;

/**
 * An Event Comparator that allows for sorting
 * of events by their associated timestamps, and if
 * they have the same timestamps, by the customerId
 * of their associated Customers.
 */
public class EventComp implements Comparator<Event> {

    /**
     * The implemented compare method.
     *
     * @param i The first Event
     * @param j The second Event
     * @return An {@code int} value of either 1, 0, or -1;
     */
    public int compare(Event i, Event j) {
        double iTime = i.getTimeStamp();
        double jTime = j.getTimeStamp();
        int iCust = i.getCustomer().getCustomerId();
        int jCust = j.getCustomer().getCustomerId();

        if (iTime - jTime == 0) {
            if (iCust - jCust == 0) {
                return 0;
            }
            return iCust - jCust;
        }

        int diff = (iTime - jTime < 0) ? -1 : 1;
        return diff;
    }
}
