package bitbacktester.broker;

public class PercentFee extends Fee {
    private final double percent;
    private double orderValue;
    
    public PercentFee(double orderValue, double percent) {
        this.orderValue = orderValue;
        this.percent = percent;
    }
    
    public PercentFee(double percent) {
        this.percent = percent;
    }
    @Override
    public void setOrderValue(double value) {
        this.orderValue = value;
    }
    @Override
    public double calc() {
        return orderValue * percent;
    }
    public double calc(double orderValue) {
        return orderValue * percent;
    }
    @Override
    public String toString() {
        return "PercentFee{percent=" + percent + ", orderValue=" + orderValue + ", value=" + calc() + "}"; 
    }
    
}
