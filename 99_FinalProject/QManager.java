/**
 * The QManager class consists of only static methods, which
 * serve the purpose of holistically managing the queues of self-checkout
 * counters. Generally any input list will consist an {@code
 * ImList<Server>} of both servers and self-checkouts.
 * Certain individual self-checkout functionalities can affect all
 * other self-checkouts as well. This class encapsulates the
 * methods required to manage these effects.
 */
public class QManager {

    private QManager() {
    }
    
    /**
     * Returns the serverId of the first server available to serve.
     * If no server is available, 0 is returned.
     *
     * @param list input list of servers
     * @param time input time for determining server availability
     * @return integer value of the serverId
     */
    public static int getServing(ImList<Server> list,
            double time) {
        int servingNumber = 0;
        for (Server s : list) {
            if (s.canServe(time)) {
                servingNumber = s.getServerId();
                break;
            }
        }
        return servingNumber;
    }

    /**
     * Returns the serverId of the first server available to queue.
     * If no server is available, 0 is returned.
     *
     * @param list input list of servers
     * @return integer value of the serverId
     */
    public static int getQueuing(ImList<Server> list) {
        int servingNumber = 0;
        for (Server s : list) {
            if (s.canQueue()) {
                servingNumber = s.getServerId();
                break;
            }
        }
        return servingNumber;
    }

    /**
     * Increments the unified queue for all self-checkouts by 1.
     * The input list contains a mix of human servers and
     * self-checkouts. This method sieves out the self-checkouts
     * and increments all their individual queues by 1 using the
     * {@code addOneServerQ} Server instance method.
     *
     * @param list the input list containing both servers and self-checkouts.
     *
     * @return {@code ImList<Server>} containing the updated self-checkouts.
     */
    public static ImList<Server> addOneAllQ(ImList<Server> list) {
        ImList<Server> tempList = new ImList<>();
        for (Server s : list) {
            if (s.isSelf()) {
                tempList = tempList.add(s.addOneServerQ());
            } else {
                tempList = tempList.add(s);
            }
        }
        return tempList;
    }

    /**
     * Decrements the unified queue for all self-checkouts by 1.
     * The input list contains a mix of human servers and
     * self-checkouts. This method sieves out the self-checkouts
     * and increments all their individual queues by 1 using the
     * {@code minusOneServerQ} Server instance method.
     * 
     * @param list the input list containing both servers and self-checkouts.
     *
     * @return {@code ImList<Server>} containing the updated self-checkouts.
     */
    public static ImList<Server> minusOneAllQ(ImList<Server> list) {
        ImList<Server> tempList = new ImList<>();
        for (Server s : list) {
            if (s.isSelf()) {
                tempList = tempList.add(s.minusOneServerQ());
            } else {
                tempList = tempList.add(s);
            }
        }
        return tempList;
    }

    /** 
     * Returns the serverId of the first self-checkout. This method
     * takes in a list of servers and self-checkout, and returns
     * the first self-checkout. This method is useful for
     * when it is required to make customer to wait at the "first" self-
     * checkout counter.
     * 
     * @param list the input list containing both servers and self-checkouts
     * 
     * @return {@code Integer} serverId of the first self-checkout
     */
    public static int getFirstSelfCheck(ImList<Server> list) {
        int serverId = 0;
        for (Server s : list) {
            if (s.isSelf()) {
                serverId = s.getServerId();
                break;
            }
        }
        return serverId;
    }

    /**
     * Returns first available self-checkout. Customers waiting at
     * a self-checkout queue might eventually be served by another
     * self-checkout counter. In order to faciliate the transition
     * from a WaitEvent associated with one self-checkout to a
     * ServeEvent associated with another self-checkout, this
     * method takes in a list of a mix of servers and
     * self-checkouts and returns the serverId of the self-checkout
     * with the earliest next free time.
     *
     * @param list the input list containing both servers and self-checkouts
     *
     * @return {@code Integer} serverId of the earliest available self-checkout.
     */
    public static int getEarliestSelfCheck(ImList<Server> list) { 
        int selfCheckId = 1;
        double earliestAvailable = 
            list.get(QManager.getFirstSelfCheck(list) - 1).getNextFree();

        for (Server s : list) {
            if (s.isSelf()) {
                if (s.getNextFree() < earliestAvailable) {
                    earliestAvailable = s.getNextFree();
                    selfCheckId = s.getServerId();
                }
            }
        } 

        return selfCheckId;
    }


} 
