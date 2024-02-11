package cs2030.simulator;

import java.util.Optional;
import java.util.List;
import java.util.stream.Stream;
import java.util.stream.Collectors;
import java.util.function.Supplier;
import cs2030.util.ImList;
import cs2030.util.PQ;
import cs2030.util.Pair;
import cs2030.util.Lazy;

public class Simulate5 {
    private final int numOfServers;
    private final ImList<Double> customerArrivalTime;
    private final PQ<Event> pq;
    private final Shop shop;
    private final ImList<Customer> customers;
    private final Statistic stats;

    public Simulate5(int numOfServers, List<Pair<Double, Supplier<Double>>> inputTimes) {
        this.numOfServers = numOfServers;
        this.customerArrivalTime = getCustArrivalTime(inputTimes);
        this.pq = formPQ(inputTimes);
        this.shop = formShop(numOfServers);
        this.customers = formCustomer(inputTimes);
        this.stats = new Statistic(0.0, 0, 0);
    }

    public Simulate5(Shop shop, PQ<Event> pq, ImList<Customer> customers, Statistic stats) {
        this.numOfServers = shop.getAllServers().size();
        this.customerArrivalTime = getCustArrivalTime(customers);
        this.pq = pq;
        this.shop = shop;
        this.customers = customers;
        this.stats = stats;
    }

    public static ImList<Double> getCustArrivalTime(ImList<Customer> customers) {
        ImList<Double> timingList = ImList.<Double>of();
        for (Customer c: customers) {
            timingList = timingList.add(c.getArrivalTime());
        }
        return timingList;
    }

    public static ImList<Double> getCustArrivalTime(
        List<Pair<Double, Supplier<Double>>> inputTimes) {
        ImList<Double> arrivalTimes = ImList.<Double>of();
        for (Pair<Double, Supplier<Double>> pair: inputTimes) {
            arrivalTimes = arrivalTimes.add(pair.first());
        }
        return arrivalTimes;
    }

    public static Shop formShop(int numOfServers) {
        ImList<Server> serverList = ImList.<Server>of();
        for (int i = 1; i <= numOfServers; i++) {
            serverList = serverList.add(new Server(i));
        }
        return new Shop(serverList);
    }

    public static ImList<Customer> formCustomer(
        List<Pair<Double, Supplier<Double>>> inputTimes) {
        ImList<Customer> customerList = ImList.<Customer>of();
        ImList<Pair<Double, Supplier<Double>>> imInputTimes = 
            ImList.<Pair<Double, Supplier<Double>>>of(inputTimes);
        for (int i = 0; i < imInputTimes.size(); i++) {
            customerList = customerList.add(
                new Customer(i + 1, imInputTimes.get(i).first(), 
                Lazy.<Double>of(imInputTimes.get(i).second())));
        }
        return customerList;
    }

    public static PQ<Event> formPQ(List<Pair<Double, Supplier<Double>>> inputTimes) {
        ImList<Pair<Double, Supplier<Double>>> imInputTimes = 
            ImList.<Pair<Double, Supplier<Double>>>of(inputTimes);
        PQ<Event> temporaryPQ = new PQ<Event>(new EventComparator());
        for (int i = 0; i < imInputTimes.size(); i++) {
            temporaryPQ = temporaryPQ.add(new ArrivalEvent(
                new Customer(i + 1, imInputTimes.get(i).first(), 
                Lazy.<Double>of(imInputTimes.get(i).second())), 
                imInputTimes.get(i).first()));
        }
        return temporaryPQ;
    }

    public String run() {
        Simulate5 curr = this;
        while (!curr.pq.isEmpty()) {
            Event event = curr.pq.poll().first();

            if (event.getType().equals("Serve")) {
                Statistic updatedStats = curr.stats.update(0.0, 1, 0);
                curr = new Simulate5(
                    curr.shop, curr.pq, curr.customers, updatedStats);                
            }

            if (event.getType().equals("Leave")) {
                Statistic updatedStats = curr.stats.update(0.0, 0, 1);
                curr = new Simulate5(curr.shop, curr.pq, curr.customers, updatedStats);
            }

            if (event.getType().equals("Wait")) {
                double waitingStartTime = event.getEventTime();                
                Event serve = event.execute(curr.shop).first().orElse(event);
                double waitingEndTime = serve.getEventTime();
                Statistic updatedStats = curr.stats.update(
                    waitingEndTime - waitingStartTime, 0, 0);
                curr = new Simulate5(curr.shop, curr.pq, curr.customers, updatedStats);
            }

            PQ<Event> updatedpq = curr.pq.poll().second();
            Pair<Optional<Event>, Shop> nextEventPair = event.execute(curr.shop);
            
            if (nextEventPair.first() != Optional.<Event>empty()) {
                Event nextEvent = nextEventPair.first().orElse(event);
                updatedpq = updatedpq.add(nextEvent);
            }
            Shop updatedShop = nextEventPair.second();
            if (!event.getType().equals("PendingServe")) {
                System.out.println(event);
            }
            curr = new Simulate5(updatedShop, updatedpq, curr.customers, curr.stats);
        }
        return curr.stats.toString();
    }
}
