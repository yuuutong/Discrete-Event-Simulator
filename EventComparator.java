package cs2030.simulator;

import java.util.Comparator;

class EventComparator implements Comparator<Event> {
    // no need write constructor, as we will use the default empty constrcutor

    @Override
    public int compare(Event e1, Event e2) {
        if (e1.getEventTime() != e2.getEventTime()) {
            return Double.compare(e1.getEventTime(), e2.getEventTime()); // ascending order
        } 
        return Integer.compare(e1.getCustNum(), e2.getCustNum());
    }
}