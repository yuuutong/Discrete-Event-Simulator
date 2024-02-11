package cs2030.simulator;

import java.util.Optional;
import cs2030.util.Pair;
import cs2030.util.ImList;

class ArrivalEvent implements Event {
    private final Customer customer;
    private final double eventTime;
    
    ArrivalEvent(Customer customer, double eventTime) {
        this.customer = customer;
        this.eventTime = eventTime;
    }

    /* @Override
    public int getTypeInt() {
        return 0;
    } */

    @Override
    public double getCustWaitingTime() {
        return this.eventTime - this.customer.getArrivalTime();
    }

    @Override
    public String getType() {
        return "Arrive";
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
        Shop sortedShop = new Shop(shop.getAllServers().sort(new ServerComparator()));

        // fresh customer come in, looking for idle servers
        // generate serve event
        for (Server s: sortedShop.getAllServers()) {
            if (!s.ifBusy() && !s.ifWaited() && !s.ifResting()) {               
                return Pair.<Optional<Event>, Shop>of(Optional.<Event>ofNullable(
                    new ServeEvent(this.customer, this.eventTime, s)), sortedShop);
            } 
        }

        // fresh customer come in, currently no idle servers,  
        // thus wait at a server who has not reached max q length
        // generate wait event
        for (Server s: sortedShop.getAllServers()) {
            if (s.canQueue()) {
                return Pair.<Optional<Event>, Shop>of(Optional.<Event>ofNullable(
                    new WaitEvent(this.customer, this.eventTime, s)), sortedShop);
            }
        }

        // all servers busy & reached max q length
        // generate leave event
        // no update needed to be made on server status and shop status
        return Pair.<Optional<Event>, Shop>of(Optional.<Event>ofNullable(
            new LeaveEvent(this.customer, this.eventTime)), sortedShop);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s arrives", this.eventTime, this.customer);
    }
}