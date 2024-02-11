package cs2030.simulator;

import java.util.Comparator;

class ServerComparator implements Comparator<Server> {
    @Override
    public int compare(Server s1, Server s2) {
        return Integer.compare(s1.getID(), s2.getID());
    }
}