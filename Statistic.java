package cs2030.simulator;

class Statistic {
    private final double totalWaitTime;
    private final int noOfServe;
    private final int noOfLeave;

    Statistic(double totalWaitTime, int noOfServe, int noOfLeave) {
        this.totalWaitTime = totalWaitTime;
        this.noOfServe = noOfServe;
        this.noOfLeave = noOfLeave;
    }

    Statistic update(double newWT, int newS, int newL) {
        return new Statistic(newWT + totalWaitTime, newS + noOfServe, newL + noOfLeave);
    }

    @Override
    public String toString() {
        return String.format(
            "[%.3f %d %d]", this.totalWaitTime / this.noOfServe, this.noOfServe, this.noOfLeave);
    }    
}
