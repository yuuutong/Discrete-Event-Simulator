package cs2030.simulator;

import java.util.function.Supplier;
import cs2030.util.Pair;
import cs2030.util.ImList;

class SelfCheckCounter extends Server {
    private static final double DEFAULT_RESTTIME = 0.0;

    SelfCheckCounter(int sID, int qmax) {
        super(sID, qmax);
    }

    SelfCheckCounter(int sID, boolean isBusy, int qmax, 
        ImList<Customer> custInQ, double nextAvailableTime, int numOfCustWaiting) {
        super(sID, isBusy, qmax, custInQ, nextAvailableTime, 
            numOfCustWaiting, false, () -> DEFAULT_RESTTIME);
    }

    @Override
    String getType() {
        return "selfCheck";
    }

    @Override
    public String toString() {
        return String.format("self-check %d", this.getID()); 
    }
}
