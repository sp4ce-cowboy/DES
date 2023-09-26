import java.util.function.Supplier;

/**
 * A Customer class that makes immutable Customer objects containing
 * a customer identifier, an arrival timestamp, and a supplied value
 * for the customer's service time.
 */
public class Customer {
    
    private final int customerId;
    private final double arrivalTime;
    private final Supplier<Double> serviceTime;
    
    /**
     * Constructs a customer with all parameters specified.
     * 
     * @param customerId The integer identifier of the customer
     * @param arrivalTime The arrival time of the customer
     * @param serviceTime The supplied value of the service duration
     */
    Customer(int customerId, double arrivalTime, Supplier<Double> serviceTime) {
        this.customerId = customerId;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    /**
     * Returns the id of the customer.
     *
     * @return {@code Integer} value of the customer.
     */
    public int getCustomerId() {
        return this.customerId;
    }
    
    /** 
     * Returns the arrival time of the customer.
     *
     * @return {@code Double} value of the customer's arrival time.
     */
    public double getArrivalTime() {
        return this.arrivalTime;
    }

    /**
     * Returns the service time of the customer. Invokes the supplier 
     * to get a serviceTime when this method is called. This method 
     * should only be invoked once per customer.
     *
     * @return {@code Double} value of the customer service time.
     */
    public double getServiceTime() {
        return this.serviceTime.get();
    }

    /**
     * Returns the String representation of the customer.
     *
     * @return {@code String} representation of the customerId.
     */
    @Override
    public String toString() {
        return String.valueOf(this.customerId);
    }

}
