package cs2030.simulator;

import java.util.Optional;
import java.util.function.Supplier;
import cs2030.util.Lazy;

class Customer {
    private final int cID;
    private final double arrivalTime;
    private final Lazy<Double> serviceTime;
    private static final double defaultServiceTime = 1.0;

    Customer(int cID, double arrivalTime) {
        this.cID = cID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = new Lazy<Double>(() -> defaultServiceTime);
    }

    Customer(int cID, double arrivalTime, Lazy<Double> serviceTime) {
        this.cID = cID;
        this.arrivalTime = arrivalTime;
        this.serviceTime = serviceTime;
    }

    double getArrivalTime() {
        return this.arrivalTime;
    }

    int getID() {
        return this.cID;
    }

    Lazy<Double> getLazyServiceTime() {
        return this.serviceTime;
    }

    @Override
    public String toString() {
        return String.format("%d", this.cID); 
    }
}