package cs2030.simulator;

import java.util.Optional;
import cs2030.util.Pair;
import cs2030.util.ImList;

class LeaveEvent implements Event {
    private final Customer customer;
    private final double eventTime;

    LeaveEvent(Customer customer, double eventTime) {
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
        return "Leave";
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
        return Pair.<Optional<Event>, Shop>of(Optional.<Event>empty(), shop);
    }

    @Override
    public String toString() {
        return String.format("%.3f %s leaves", this.eventTime, this.customer);
    }
}