/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.positions;

import bitbacktester.orders.Order;
import bitbacktester.orders.OrderType;
import bitbacktester.riskmanagement.LongStopLoss;

/**
 *
 * @author jmaciak
 */
public class LongPosition extends Position {
    public LongPosition(Order o) {
        super(o);
    }
    @Override
    public void add(Order o) throws OrderCantCloseException {
        if(!canAdd(o)) {
            throw new OrderCantCloseException();
        }
        if(o.getType().equals(OrderType.BUY)) {
            this.openingOrders.add(o);
        }
        if(o.getType().equals(OrderType.SELL)) {
            this.closingOrders.add(o);
        }
    }

    @Override
    public double calcRealizedPercentGain() {
        if(isOpen())
            return 0;
        return (calcClosingValue()/calcOpeningValue()) - 1;
    }

    @Override
    public double calcRealizedPnL() {
        if(isOpen()) {
            return 0;
        } else {
            return calcClosingValue() - calcOpeningValue();
        }
    }
        
    @Override
    public double calcUnrealizedPnL(double currentPrice) {
        return -1;//    return calcUnrealizedValue(currentPrice);
    }

    @Override
    public double calcUnrealizedPercentGain(double currentPrice) {
        return -1;
    }
    @Override
    public double calcUnrealizedValue(double currentPrice) {
        return (this.calcAmountOutstanding() * currentPrice);
    }
    /*@Override
    public double calcNetValue() {
        return calcClosingValue() - calcOpeningValue();
    }*/
    @Override
    public String toString() {
        String s = "LongPosition{timeOpened=";
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

    @Override
    public boolean canAdd(Order o) {
        if(!o.getAsset().equals(this.getAsset())) {
            return false;
        }
        if(o.getType().equals(OrderType.SHORT) || o.getType().equals(OrderType.COVER))
            return false;
        return ! (o.getType().equals(OrderType.SELL) && o.getAmount() > this.calcAmountOutstanding());
    }

    @Override
    public void setStopLoss(double price) {
        this.stopLoss = new LongStopLoss(getAsset(), price);
    }
}
