package cs2030.simulator;

import java.util.Optional;
import cs2030.util.Pair; // import cross-package class in order to use it

class EventStub implements Event {
    private final Customer customer;
    private final double eventTime;

    EventStub(Customer customer, double eventTime) {
        this.customer = customer;
        this.eventTime = eventTime;
    }

    Customer getCustomer() {
        return this.customer;
    }

    @Override
    public String getType() {
        return "EventStub";
    }

    @Override
    public double getCustWaitingTime() {
        return this.eventTime - this.customer.getArrivalTime();
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
        // return String.valueOf(this.eventTime) + "00"; 
        // String.valueOf(double) Returns the string representation of the double argument.
        return String.format("%.3f", this.eventTime);
    }
}