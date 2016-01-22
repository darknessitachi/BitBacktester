package bitbacktester.broker;

public class FlatFee extends Fee {
    private final double fee;
    
    public FlatFee(double fee) {
        this.fee = fee;
    }
    
    @Override
    public double calc() {
        return fee;
    }
    @Override public void setOrderValue(double value) {
    }
    @Override public String toString() {
        return "FlatFee{fee=" + fee + "}";
    }
}