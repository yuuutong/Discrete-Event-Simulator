package cs2030.simulator;

import java.util.List;
import cs2030.util.Pair; // need import from util class in order to use Pair and ImList!
import cs2030.util.ImList;

class Shop {
    private final ImList<Server> serverList;

    Shop(List<Server> list) {
        this(ImList.<Server>of(list));
    }

    Shop(ImList<Server> serverList) {
        this.serverList = serverList;
    }

    ImList<Server> getAllServers() {
        return this.serverList;
    }

    int getNumOfHumanServer() {
        int ans = 0;
        for (Server s: this.serverList) {
            if (s.getType().equals("human")) {
                ans += 1;
            }
        }
        return ans;
    }

    Shop addToSharedQ(Customer c) {
        ImList<Server> updatedAllServers = ImList.<Server>of();
        for (Server s: this.getAllServers()) {
            if (s.getType().equals("human")) {
                updatedAllServers = updatedAllServers.add(s);
            }
        }
        // add the same customer to the q of all counters
        for (int i = this.getNumOfHumanServer(); i < this.getAllServers().size(); i++) {
            Server old = this.getAllServers().get(i);
            SelfCheckCounter updated = new SelfCheckCounter(
                old.getID(),
                old.ifBusy(),
                old.getMaxQLength(),
                old.addNewWaitingCust(c),
                old.getNextAvailableTime(),
                old.getNumOfCustWaiting() + 1);
            updatedAllServers = updatedAllServers.add(updated);
        }
        return new Shop(updatedAllServers);
    }

    Shop removeFromSharedQ(Customer c) {
        ImList<Server> updatedAllServers = ImList.<Server>of();
        for (Server s: this.getAllServers()) {
            if (s.getType().equals("human")) {
                updatedAllServers = updatedAllServers.add(s);
            }
        }
        // remove the same customer from the q of all counters
        // to be used in done(-1 in q length is done in serve)
        for (int i = this.getNumOfHumanServer(); i < this.getAllServers().size(); i++) {
            Server old = this.getAllServers().get(i);
            SelfCheckCounter updated = new SelfCheckCounter(
                old.getID(),
                old.ifBusy(),
                old.getMaxQLength(),
                old.removeCustomerFromQ(c),
                old.getNextAvailableTime(),
                old.getNumOfCustWaiting());
            updatedAllServers = updatedAllServers.add(updated);
        }
        return new Shop(updatedAllServers);
    }

    Shop reduceQueueLengthByOne() {
        ImList<Server> updatedAllServers = ImList.<Server>of();
        for (Server s: this.getAllServers()) {
            if (s.getType().equals("human")) {
                updatedAllServers = updatedAllServers.add(s);
            }
        }
    
        for (int i = this.getNumOfHumanServer(); i < this.getAllServers().size(); i++) {
            Server old = this.getAllServers().get(i);
            SelfCheckCounter updated = new SelfCheckCounter(
                old.getID(),
                old.ifBusy(),
                old.getMaxQLength(),
                old.getWaitingCust(),
                old.getNextAvailableTime(),
                old.getNumOfCustWaiting() - 1);
            updatedAllServers = updatedAllServers.add(updated);
        }
        return new Shop(updatedAllServers);
    }

    Server earliestAvailableCounter() {
        ImList<Server> counters = ImList.<Server>of();
        for (Server s: this.getAllServers()) {
            if (!s.getType().equals("human")) {
                counters = counters.add(s);
            }
        }
        /* counters = counters.sort(new CounterComparator());
        Server i = counters.get(0);
        return i; */
        Server earliestAvailableCounter = counters.get(0);
        for (Server s: counters) {
            if (s.getNextAvailableTime() < earliestAvailableCounter.getNextAvailableTime()) {
                earliestAvailableCounter = s;
            }           
        }
        return earliestAvailableCounter;
    }

    @Override
    public String toString() {
        return this.serverList.toString();
    }
}
