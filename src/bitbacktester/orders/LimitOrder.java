/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.orders;

import java.time.LocalDateTime;

/**
 *
 * @author jmaciak
 */
public abstract class LimitOrder extends Order {
    private double price;
    public LimitOrder(OrderType type, String asset, double amount, double price) {
        super(type, asset, amount);
        this.price = price;
    }
    public LimitOrder(OrderType type, String asset, LocalDateTime time, double amount, double price) {
        super(type, asset, time, amount);
        this.price = price;
    }
    @Override
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        if(price >= 0) {
            this.price = price;
        }
    }
    @Override
    public double getValue() {
        return price * getAmount();
    }
}
