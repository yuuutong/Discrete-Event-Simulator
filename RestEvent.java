package cs2030.simulator;

import java.util.Optional;
import cs2030.util.Pair;
import cs2030.util.ImList;

class RestEvent implements Event {
    private final Customer customer;
    private final double eventTime;
    private final Server server;

    RestEvent(Customer customer, double eventTime, Server server) {
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
        return "Rest";
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
        
        Server updatedServer = new Server(
            serverID, 
            mostRecentServer.ifBusy(),
            mostRecentServer.getMaxQLength(),
            mostRecentServer.getWaitingCust(),
            this.eventTime,
            mostRecentServer.getNumOfCustWaiting(),
            false,
            mostRecentServer.getSupplierRestTime());
                
        
        // System.out.println(this.server.getWaitingCust());
        // System.out.println(updatedServer.getWaitingCust());
        ImList<Server> updatedAllServers = ImList.<Server>of();
        for (Server s: shop.getAllServers()) {
            if (s.getID() != serverID) {
                updatedAllServers = updatedAllServers.add(s);
            }
        }
        updatedAllServers = updatedAllServers.add(updatedServer);
        
        Shop updatedShop = new Shop(updatedAllServers.sort(new ServerComparator()));

        return Pair.<Optional<Event>, Shop>of(Optional.<Event>empty(), updatedShop);
    }
}
