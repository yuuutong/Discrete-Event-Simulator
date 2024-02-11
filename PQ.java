package cs2030.util;

import java.util.Collection;
import java.util.Comparator;
import java.util.PriorityQueue;

public class PQ<T> {
    private final PriorityQueue<T> pq;

    PQ() {
        this.pq = new PriorityQueue<T>();
    }

    PQ(PriorityQueue<T> other) {
        this.pq = new PriorityQueue<T>(other);
    }

    // type parameter is not Comparator<T>. why?
    // PriorityQueue(Comparator<? super E> comparator) is specified in API, just follow
    // The comparator will eventually make use of the type T 
    // (use the methods in T to do it's calculation), 
    // so it needs to ensure type is at least a T
    public PQ(Comparator<? super T> cmp) {
        this.pq = new PriorityQueue<T>(cmp);
    }

    // implementation follow the add mtd in ImList
    public PQ<T> add(T elem) {
        PQ<T> newPQ = new PQ<T>(this.pq);
        newPQ.pq.add(elem); // newPQ.pq to call attribute's mtd (inner core of PQ)
        return newPQ;
    }

    // implementation follow the remove mtd in ImList
    public Pair<T, PQ<T>> poll() {
        PQ<T> newPQ = new PQ<T>(this.pq);
        T firstElem = newPQ.pq.poll(); // newPQ.pq to call attribute's mtd (inner core of PQ)
        return Pair.<T, PQ<T>>of(firstElem, newPQ);
    }

    public boolean isEmpty() {
        return this.pq.size() == 0;
    }

    @Override
    public String toString() {
        return this.pq.toString();
    }
}
