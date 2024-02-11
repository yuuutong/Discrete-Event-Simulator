package cs2030.simulator;

import java.util.List;
import cs2030.util.ImList; // need import cross-package class if want to use

// make the class "public facing", thus add public before class name, constructor and method names
public class Simulate2 {
    private final int numOfServers;
    private final List<Double> customerArrivalTime;

    public Simulate2(int numOfServers, List<Double> customerArrivalTime) {
        this.numOfServers = numOfServers;
        this.customerArrivalTime = customerArrivalTime;
    }

    public String run() {
        String ans = "";
        for (double time: this.customerArrivalTime) {
            ans += String.format("%.3f", time) + "\n";
        }
        ans += "-- End of Simulation --";
        return ans;
    }

    @Override
    public String toString() {
        // no use of ImList<Integer> serverList = new ImList<Integer>() anymore
        // constructor is made private from now onwards
        // can only use the static mtd provided in ImList to create new Imlist obj
        ImList<Integer> serverList = ImList.<Integer>of();
        for (int i = 1; i <= this.numOfServers; i++) {
            serverList = serverList.add(i);
        }
        return String.format("Queue: %s; Shop: %s", this.customerArrivalTime, serverList);
    }
}