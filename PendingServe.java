package cs2030.simulator;

import java.util.Optional;
import cs2030.util.Pair;
import cs2030.util.ImList;

class PendingServe implements Event {
    private final Customer customer;
    private final double eventTime;
    private final Server server;

    PendingServe(Customer customer, double eventTime, Server server) {
        this.customer = customer;
        this.eventTime = eventTime;
        this.server = server;
    }

    /* @Override
    public int getTypeInt() {
        return 2;
    } */
    @Override
    public double getCustWaitingTime() {
        return this.eventTime - this.customer.getArrivalTime();
    }

    @Override
    public String getType() {
        return "PendingServe";
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
        for (Server s: shop.getAllServers()) {
            if (this.server.getID() == s.getID()) {
                mostRecentServer = s;
            }
        }

        if (mostRecentServer.getType().equals("human")) {
            if (this.customer.getID() != mostRecentServer.getWaitingCust().get(0).getID()) {    
                return Pair.<Optional<Event>, Shop>of(Optional.<Event>ofNullable(
                    new PendingServe(this.customer, mostRecentServer.getNextAvailableTime(), 
                    mostRecentServer)), shop);                  
            }
    
            return Pair.<Optional<Event>, Shop>of(Optional.<Event>ofNullable(
                new ServeEvent(this.customer, 
                    mostRecentServer.getNextAvailableTime(), mostRecentServer)), shop);
        } else { // get the earliestAvailableCounter
            Server updatedServer = shop.earliestAvailableCounter(); 
            // wait at the earliest available counter

            if (this.customer.getID() != mostRecentServer.getWaitingCust().get(0).getID()) {    
                return Pair.<Optional<Event>, Shop>of(Optional.<Event>ofNullable(
                    new PendingServe(this.customer, updatedServer.getNextAvailableTime(), 
                    mostRecentServer)), shop);                  
            }           

            return Pair.<Optional<Event>, Shop>of(Optional.<Event>ofNullable(
                new ServeEvent(this.customer, 
                    updatedServer.getNextAvailableTime(), updatedServer)), shop);
        }             
    }
}
