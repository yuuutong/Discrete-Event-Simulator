package cs2030.simulator;

import java.util.function.Supplier;
import java.util.Optional;
import cs2030.util.Pair;
import cs2030.util.ImList;

class DoneEvent implements Event {
    private final Customer customer;
    private final double eventTime;
    private final Server server;

    DoneEvent(Customer customer, double eventTime, Server server) {
        this.customer = customer;
        this.eventTime = eventTime;
        this.server = server;
    }

    /* @Override
    public int getTypeInt() {
        return 4;
    } */
    @Override
    public double getCustWaitingTime() {
        return this.eventTime - this.customer.getArrivalTime();
    }

    @Override
    public String getType() {
        return "Done";
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
            double restTime = mostRecentServer.getSupplierRestTime().get();
            ImList<Customer> updatedQ = ImList.<Customer>of();
            for (Customer c: mostRecentServer.getWaitingCust()) {
                if (this.getCustNum() != c.getID()) {
                    updatedQ = updatedQ.add(c);
                }
            } 
            Server updatedServer = mostRecentServer;
            if (restTime != 0.0) { // go rest
                updatedServer = new Server(
                    serverID, 
                    false,
                    mostRecentServer.getMaxQLength(),
                    updatedQ,
                    this.eventTime + restTime,
                    mostRecentServer.getNumOfCustWaiting(),
                    true,
                    mostRecentServer.getSupplierRestTime());
            } else { // do not go rest
                updatedServer = new Server(
                    serverID, 
                    false,
                    mostRecentServer.getMaxQLength(),
                    updatedQ,
                    this.eventTime, // rest time == 0
                    mostRecentServer.getNumOfCustWaiting(),
                    false,
                    mostRecentServer.getSupplierRestTime());
            }     
            ImList<Server> updatedAllServers = ImList.<Server>of();
            for (Server s: shop.getAllServers()) {
                if (s.getID() != serverID) {
                    updatedAllServers = updatedAllServers.add(s);
                }
            }
            updatedAllServers = updatedAllServers.add(updatedServer);
            
            Shop updatedShop = new Shop(updatedAllServers.sort(new ServerComparator()));

            if (updatedServer.ifResting()) {
                return Pair.<Optional<Event>, Shop>of(Optional.<Event>ofNullable(
                    new RestEvent(this.customer, this.eventTime + restTime, updatedServer)), 
                        updatedShop);
            }
            return Pair.<Optional<Event>, Shop>of(Optional.<Event>empty(), updatedShop);
        } else {
            // case when server is counter
            SelfCheckCounter updatedServer = new SelfCheckCounter(
                serverID, 
                false, 
                mostRecentServer.getMaxQLength(),
                mostRecentServer.getWaitingCust(), 
                this.eventTime,
                mostRecentServer.getNumOfCustWaiting());
            
            ImList<Server> updatedAllServers = ImList.<Server>of();
            for (Server s: shop.getAllServers()) {
                if (s.getID() != serverID) {
                    updatedAllServers = updatedAllServers.add(s);
                }
            }
            updatedAllServers = updatedAllServers.add(updatedServer);
            Shop updatedShop = new Shop(updatedAllServers.sort(new ServerComparator()));
            // updatedShop = updatedShop.removeFromSharedQ(this.customer);

            return Pair.<Optional<Event>, Shop>of(Optional.<Event>empty(), updatedShop);
        }
        
    }

    @Override
    public String toString() {
        return String.format("%.3f %s done serving by %s", 
            this.eventTime, this.customer, this.server);
    }
}