/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.riskmanagement;

import bitbacktester.orders.Order;

/**
 *
 * @author jmaciak
 */
public abstract class StopLoss {
    private final double price;
    
    public StopLoss(double price) {
        this.price = price;
    }
    public double getPrice() {
        return price;
    }
    /**
     * Creates the order to fill the remaining position.
     * @return 
     */
    public abstract Order createOrder();
    public abstract boolean isHit(double currentPrice);
}