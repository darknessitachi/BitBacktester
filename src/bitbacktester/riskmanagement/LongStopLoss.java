/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.riskmanagement;

import bitbacktester.orders.LimitSellOrder;
import bitbacktester.orders.Order;

/**
 *
 * @author jmaciak
 */
public class LongStopLoss extends StopLoss {
    public LongStopLoss(double price) {
        super(price);
    }

    @Override
    public boolean isHit(double currentPrice) {
        return currentPrice <= getPrice();
    }

    @Override
    public Order createOrder() {
        return new LimitSellOrder("", 0, getPrice());
    }
}
