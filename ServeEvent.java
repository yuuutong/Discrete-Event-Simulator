package cs2030.simulator;

import java.util.Optional;
import cs2030.util.Pair;
import cs2030.util.ImList;

class ServeEvent implements Event {
    private final Customer customer;
    private final double eventTime;
    private final Server server;

    ServeEvent(Customer customer, double eventTime, Server server) {
        this.customer = customer;
        this.eventTime = eventTime;
        this.server = server;
    }

    ServeEvent(Customer customer, double eventTime) {
        this.customer = customer;
        this.eventTime = eventTime;
        this.server = new Server(1);
    }

    /* @Override
    public int getTypeInt() {
        return 3;
    } */

    @Override
    public String getType() {
        return "Serve";
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
    public double getCustWaitingTime() {
        return this.eventTime - this.customer.getArrivalTime();
    }
    
    // this.server.ifBusy() == false for sure
    @Override
    public Pair<Optional<Event>, Shop> execute(Shop shop) {
        Server mostRecentServer = this.server;
        int serverID = this.server.getID();
        for (Server s: shop.getAllServers()) {
            if (serverID == s.getID()) {
                mostRecentServer = s;
            }
        }
        
        double serviceTime = this.customer.getLazyServiceTime().get();

        Server updatedServer = mostRecentServer;
        Shop updatedShop = shop;

        if (mostRecentServer.getType().equals("human")) {
            updatedServer = new Server(
            serverID, 
            true, 
            mostRecentServer.getMaxQLength(),
            mostRecentServer.getWaitingCust(), 
            this.eventTime + serviceTime,
            mostRecentServer.getNumOfCustWaiting() - 1,
            mostRecentServer.ifResting(),
            mostRecentServer.getSupplierRestTime());

            if (mostRecentServer.getNumOfCustWaiting() == 0) {        
                updatedServer = new Server(
                serverID, 
                true, 
                mostRecentServer.getMaxQLength(),
                mostRecentServer.getWaitingCust(), 
                this.eventTime + serviceTime,
                mostRecentServer.getNumOfCustWaiting(),
                mostRecentServer.ifResting(),
                mostRecentServer.getSupplierRestTime());
            }

            ImList<Server> updatedAllServers = ImList.<Server>of();
            for (Server s: shop.getAllServers()) {
                if (s.getID() != serverID) {
                    updatedAllServers = updatedAllServers.add(s);
                }
            }

            updatedAllServers = updatedAllServers.add(updatedServer);       
            updatedShop = new Shop(updatedAllServers.sort(new ServerComparator()));
        } else {
            // else case: type is counter
            // update next available time and busy for the this.server
            updatedServer = new SelfCheckCounter(
            serverID, 
            true, 
            mostRecentServer.getMaxQLength(),
            mostRecentServer.getWaitingCust(), 
            this.eventTime + serviceTime,
            mostRecentServer.getNumOfCustWaiting());
            
            ImList<Server> updatedAllServers = ImList.<Server>of();
            for (Server s: shop.getAllServers()) {
                if (s.getID() != serverID) {
                    updatedAllServers = updatedAllServers.add(s);
                }
            }
            updatedAllServers = updatedAllServers.add(updatedServer);
            updatedShop = new Shop(updatedAllServers.sort(new ServerComparator()));
            // -1 in length of q for all servers
            updatedShop = updatedShop.reduceQueueLengthByOne(); 
            // remove the served customer from q of all counters
            updatedShop = updatedShop.removeFromSharedQ(this.customer); 

            // do not minus q length by 1 for all counters, 
            // if the customer that the counter is serving now has not been in q
            if (mostRecentServer.getNumOfCustWaiting() == 0) {
                updatedServer = new SelfCheckCounter(
                serverID, 
                true, 
                mostRecentServer.getMaxQLength(),
                mostRecentServer.getWaitingCust(), 
                this.eventTime + serviceTime,
                mostRecentServer.getNumOfCustWaiting());

                updatedShop = new Shop(updatedAllServers.sort(new ServerComparator()));
                // remove the served customer from q of all counters
                updatedShop = updatedShop.removeFromSharedQ(this.customer); 
            }
        }
        return Pair.<Optional<Event>, Shop>of(Optional.<Event>ofNullable(
            new DoneEvent(this.customer, this.eventTime + serviceTime, updatedServer)), 
            updatedShop);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s serves by %s", this.eventTime, this.customer, this.server);
    }
}