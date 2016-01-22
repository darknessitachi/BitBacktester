/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.orders;

import bitbacktester.broker.Fee;
import bitbacktester.positions.Position;
import java.time.LocalDateTime;

/**
 *
 * @author jmaciak
 */
public abstract class Order {
    private final LocalDateTime time;
    private final String asset;
    private double amount;
    private OrderType type;
    private Fee fee;

    public Order(OrderType type, String asset, double amount) {
        this.type = type;
        this.asset = asset;
        this.amount = amount;
        this.time = LocalDateTime.now();       
    } 
    public Order(OrderType type, String asset, LocalDateTime time, double amount) {
        this.type = type;
        this.asset = asset;
        this.amount = amount;
        this.time = time;       
    }    
    public void setAmount(double amount) {
        this.amount = amount;
    }
    public void setType(OrderType type) {
        this.type = type;
    }
    public OrderType getType() {
        return type;
    }
    public String getAsset() {
        return asset;
    }
    public LocalDateTime getTime() {
        return time;
    }
    public void setFee(Fee f) {
        f.setOrderValue(this.getValue());
        this.fee = f;
    }
    public Fee getFee() {
        return fee;
    }
    /**
     * Gets the amount of an asset an order is placed for.
     * @return Amount of asset traded.
     */
    public double getAmount() {
        //if(type.equals(OrderType.SHORT) || type.equals(OrderType.SELL))
        //    return amount;
        //else
            return amount;
    }
    /**
     * Creates a new position with this order as the opening order. Only works
     * with LimitBuyOrder or LimitShortOrder. 
     * @return A new position object
     * @throws bitbacktester.orders.CantCreatePositionException
     */
    public abstract Position createPosition() throws CantCreatePositionException;
    public abstract double getPrice();
    public abstract double getValue();
}
