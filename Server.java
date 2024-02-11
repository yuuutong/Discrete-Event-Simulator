package cs2030.simulator;

import java.util.function.Supplier;
import cs2030.util.Pair;
import cs2030.util.ImList;

class Server {
    private final int sID;
    private final boolean isBusy;
    private final int qmax;
    private final ImList<Customer> custInQ;
    private final double nextAvailableTime;
    private final int numOfCustWaiting;
    private final boolean isResting;
    private final Supplier<Double> restTime;

    private static final int DEFAULT_MAXQ = 1;
    private static final double DEFAULT_RESTTIME = 0.0;

    Server(int sID) {
        this.sID = sID; 
        this.isBusy = false;
        this.qmax = DEFAULT_MAXQ;
        this.custInQ = ImList.<Customer>of();
        this.nextAvailableTime = 0;
        this.numOfCustWaiting = 0;
        this.isResting = false;
        this.restTime = () -> DEFAULT_RESTTIME;
    }

    Server(int sID, int qmax) {
        this.sID = sID; 
        this.isBusy = false;
        this.qmax = qmax;
        this.custInQ = ImList.<Customer>of();
        this.nextAvailableTime = 0;
        this.numOfCustWaiting = 0;
        this.isResting = false;
        this.restTime = () -> DEFAULT_RESTTIME;
    }

    Server(int sID, int qmax, Supplier<Double> restTime) {
        this.sID = sID; 
        this.isBusy = false;
        this.qmax = qmax;
        this.custInQ = ImList.<Customer>of();
        this.nextAvailableTime = 0;
        this.numOfCustWaiting = 0;
        this.isResting = false;
        this.restTime = restTime;
    }

    Server(int sID, boolean isBusy, int qmax, ImList<Customer> custInQ, 
        double nextAvailableTime, int numOfCustWaiting, 
        boolean isResting, Supplier<Double> restTime) {
        this.sID = sID; 
        this.isBusy = isBusy;
        this.qmax = qmax;
        this.custInQ = custInQ;
        this.nextAvailableTime = nextAvailableTime;
        this.numOfCustWaiting = numOfCustWaiting;
        this.isResting = isResting;
        this.restTime = restTime;
    }

    boolean ifResting() {
        return this.isResting;
    }

    Supplier<Double> getSupplierRestTime() {
        return this.restTime;
    }

    int getNumOfCustWaiting() {
        return this.numOfCustWaiting;
    }

    int getID() {
        return this.sID;
    }

    boolean ifBusy() {
        return this.isBusy;
    }

    double getNextAvailableTime() {
        return this.nextAvailableTime;
    }

    ImList<Customer> getWaitingCust() {
        return this.custInQ;
    }

    int getMaxQLength() {
        return this.qmax;
    }

    boolean ifWaited() {
        return this.custInQ.size() != 0;
    }

    boolean canQueue() {
        return this.numOfCustWaiting < this.qmax;
    }

    String getType() {
        return "human";
    }

    ImList<Customer> addNewWaitingCust(Customer c) {
        ImList<Customer> updatedQ = this.custInQ.add(c);
        return updatedQ;
    }

    ImList<Customer> removeCustomerFromQ(Customer i) {
        ImList<Customer> updatedQ = ImList.<Customer>of();
        for (Customer c: this.getWaitingCust()) {
            if (i.getID() != c.getID()) {
                updatedQ = updatedQ.add(c);
            }
        }
        return updatedQ;
    }

    @Override
    public String toString() {
        return String.format("%d", this.sID); 
    }
}