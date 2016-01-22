/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.positions;

import bitbacktester.orders.Order;
import bitbacktester.orders.OrderType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmaciak
 */
public abstract class Position {
    protected List<Order> openingOrders;
    protected List<Order> closingOrders;
    private String asset;
    private PositionType type;
    
    public Position() {
        this.openingOrders = new ArrayList<>();
        this.closingOrders = new ArrayList<>();
    }
    public Position(Order o) {
        this.openingOrders = new ArrayList<>();
        this.openingOrders.add(o);
        this.closingOrders = new ArrayList<>();
        if(o.getType().equals(OrderType.BUY)) {
            type = PositionType.LONG;
        }
        else {
            type = PositionType.SHORT;
        }
    }
    /**
     * Adds an order to a position. If it is a LongPosition, an exception
     * will be thrown if a sell order is bigger than the open position.
     * For a ShortPosition, an exception will be thrown if cover order is
     * larger than open position.
     * @param o 
     * @throws bitbacktester.positions.OrderCantCloseException Thrown if order is too big to close a position.
     */
    public abstract void add(Order o) throws OrderCantCloseException;
    /**
     * Finds and returns the order that opened this position.
     * @return Order
     */
    public Order getOpeningOrder() {
        return openingOrders.get(0);
    }
    public Order getClosingOrder() {
        return closingOrders.get(closingOrders.size() - 1);
    }
    public LocalDateTime getTimeOpened() {
        return openingOrders.get(0).getTime();
    }
    public PositionType getType() {
        return type;
    }
    /**
     * Calculates the amount of the position that is outstanding. (not closed by
     * an opposite order.)
     * @return 
     */
    public double calcAmountOutstanding() {
        double openingAmount = 0;
        for(Order o : openingOrders) {
            openingAmount += o.getAmount();
        }
        double closingAmount = 0;
        for(Order o : closingOrders) {
            closingAmount += o.getAmount();
        }
        return openingAmount - closingAmount;    
    }
    /**
     * Calculates the total fee charged for the entire position.
     * @return 
     */
    public double calcFees() {
        double totalFee = 0;
        for(Order o : openingOrders) {
            totalFee += o.getFee().calc();
        }
        for(Order o : closingOrders) {
            totalFee += o.getFee().calc();
        }
        return totalFee;
    }
    public boolean isOpen() {
        return (calcAmountOutstanding() != 0);
    }
    /**
     * Calculates the total value of the position. If isClosed(), this will be
     * equal to calcClosingValue();
     * @param currentPrice
     * @return 
     */
    public double calcValue(double currentPrice) {
        return this.calcClosingValue() + calcUnrealizedValue(currentPrice);
    }
    public abstract double calcRealizedPercentGain();
    public abstract double calcRealizedPnL();
    public abstract double calcUnrealizedPnL(double currentPrice);
    public abstract double calcUnrealizedPercentGain(double currentPrice);
    /**
     * Calculates the value of the remaining portion of the position that has
     * not been closed out. Essentially marks to market.
     * @param currentPrice
     * @return 
     */
    public abstract double calcUnrealizedValue(double currentPrice);
    //public abstract double calcNetValue();
    /**
     * Determines whether or not the order can be added to a given
     * position.
     * @param o
     * @return 
     */
    public abstract boolean canAdd(Order o);
    public String getAsset() {
        return getOpeningOrder().getAsset();
    }
    public double calcOpeningValue() {
        double sum = 0;
        for(Order o : openingOrders) {
            sum += o.getValue();
        }
        return sum;
    }
    /**
     * Returns the value of the position that has already been closed out.
     * @return 
     */
    public double calcClosingValue() {
        double sum = 0;
        for(Order o : closingOrders) {
            sum += o.getValue();
        }
        return sum;
    }
    public double calcOpeningAmount() {
        double amount = 0;
        for(Order o : openingOrders) {
            amount += o.getAmount();
        }
        return amount;
    }

}
