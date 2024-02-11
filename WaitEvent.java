package cs2030.simulator;

import java.util.Optional;
import cs2030.util.Pair;
import cs2030.util.ImList;

class WaitEvent implements Event {
    private final Customer customer;
    private final double eventTime;
    private final Server server;

    WaitEvent(Customer customer, double eventTime, Server server) {
        this.customer = customer;
        this.eventTime = eventTime;
        this.server = server;
    }

    @Override
    public double getCustWaitingTime() {
        return this.eventTime - this.customer.getArrivalTime();
    }

    @Override
    public String getType() {
        return "Wait";
    }

    @Override 
    public int getCustNum() {
        return this.customer.getID();
    }

    @Override
    public double getEventTime() {
        return this.eventTime;
    }

    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        Server mostRecentServer = this.server;
        int serverID = this.server.getID();
        for (Server s: shop.getAllServers()) {
            if (serverID == s.getID()) {
                mostRecentServer = s;
            }
        }

        if (mostRecentServer.getType().equals("human")) {
            Server updatedServer = new Server(
                serverID, 
                mostRecentServer.ifBusy(), 
                mostRecentServer.getMaxQLength(), 
                mostRecentServer.addNewWaitingCust(this.customer),
                mostRecentServer.getNextAvailableTime(),
                mostRecentServer.getNumOfCustWaiting() + 1,
                mostRecentServer.ifResting(),
                mostRecentServer.getSupplierRestTime());
            ImList<Server> updatedAllServers = ImList.<Server>of();
            for (Server s: shop.getAllServers()) {
                if (s.getID() != serverID) {
                    updatedAllServers = updatedAllServers.add(s);
                }
            }
            updatedAllServers = updatedAllServers.add(updatedServer);        
            Shop updatedShop = new Shop(updatedAllServers.sort(new ServerComparator()));
            return Pair.<Optional<Event>, Shop>of(Optional.<Event>ofNullable(
                new PendingServe(this.customer, mostRecentServer.getNextAvailableTime(), 
                updatedServer)), updatedShop);
        } else {
            // this add cust to imlist, and increase length of q by 1 for all counters
            Shop updatedShop = shop.addToSharedQ(this.customer); 
            // sort according to id
            updatedShop = new Shop(updatedShop.getAllServers().sort(new ServerComparator())); 
            // get the earliestAvailableCounter for cust to wait at
            Server updatedServer = updatedShop.earliestAvailableCounter(); 
            return Pair.<Optional<Event>, Shop>of(Optional.<Event>ofNullable(
                new PendingServe(this.customer, updatedServer.getNextAvailableTime(), 
                mostRecentServer)), updatedShop);
        }               
    }

    @Override
    public String toString() {
        return String.format("%.3f %s waits at %s", this.eventTime, this.customer, this.server);
    }
}