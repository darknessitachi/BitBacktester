/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.riskmanagement;

import bitbacktester.orders.LimitCoverOrder;
import bitbacktester.orders.Order;

/**
 *
 * @author jmaciak
 */
public class ShortStopLoss extends StopLoss {

    public ShortStopLoss(String asset, double price) {
        super(asset, price);
    }
    @Override
    public boolean isHit(double currentPrice) {
        return (currentPrice >= super.getPrice());
    } 

    @Override
    public Order createOrder() {
        return new LimitCoverOrder(getAsset(), 0, getPrice());
    }
}
