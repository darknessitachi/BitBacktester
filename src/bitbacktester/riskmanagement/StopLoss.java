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
    private final String asset;
    
    public StopLoss(String asset, double price) {
        this.asset = asset;
        this.price = price;
    }
    public double getPrice() {
        return price;
    }
    public String getAsset() {
        return asset;
    }
    /**
     * Creates the order to fill the remaining position.
     * @return 
     */
    public abstract Order createOrder();
    public abstract boolean isHit(double currentPrice);
}