package bitbacktester.positions;

import bitbacktester.orders.Order;
import bitbacktester.orders.OrderType;
import bitbacktester.riskmanagement.ShortStopLoss;

/**
 * @author jmaciak
 */
public class ShortPosition extends Position {

    public ShortPosition(Order o) {
        super(o);
    }
    @Override
    public void add(Order o) throws OrderCantCloseException {
        if(!canAdd(o)) {
            throw new OrderCantCloseException();
        }
        if(o.getType().equals(OrderType.SHORT)) {
            openingOrders.add(o);
        }
        if(o.getType().equals(OrderType.COVER)) {
            closingOrders.add(o);
        }        
    }

    @Override
    public double calcRealizedPercentGain() {
        if(isOpen())
            return 0;
        return (calcOpeningValue()/calcClosingValue()) - 1 ;
    }

    @Override
    public double calcRealizedPnL() {
        if(isOpen()) {
            return 0;
        }
        return calcOpeningValue() - calcClosingValue();
    }

    @Override
    public double calcUnrealizedPnL(double currentPrice) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double calcUnrealizedPercentGain(double currentPrice) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean canAdd(Order o) {
        if(o.getType().equals(OrderType.BUY) || o.getType().equals(OrderType.SELL)) {
            return false;
        }
        if(!o.getAsset().equals(this.getAsset())) {
            return false;
        }
        return !(o.getType().equals(OrderType.COVER) && o.getAmount() > this.calcAmountOutstanding());
    }

    /**
     * Calculates the value of the unclosed portion of a position.
     * @param currentPrice
     * @return 
     */
    @Override
    public double calcUnrealizedValue(double currentPrice) {
        if(calcAmountOutstanding() == 0) 
            return 0;
        return calcOpeningValue() + (this.calcOpeningValue() - (calcAmountOutstanding() * currentPrice));
    }

    @Override
    public void setStopLoss(double price) {
        this.stopLoss = new ShortStopLoss(getAsset(), price);
    }
    @Override
    public String toString() {
        String s = "ShortPosition{timeOpened=";
        s += getTimeOpened();
        s += ", openingOrders=[";
        for(Order o : openingOrders) {
            s += o + ", ";
        }
        s += "], closingOrders=[";
        for(Order o : closingOrders) {
           s += o + ", ";        
        }
        s += "], realizedPnL=";
        s += calcRealizedPnL();
        s += "}";
        return s;
    }
    
}
