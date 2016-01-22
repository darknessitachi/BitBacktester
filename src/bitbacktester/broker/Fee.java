package bitbacktester.broker;

public abstract class Fee {
    public abstract double calc();
    public abstract void setOrderValue(double value);

}
