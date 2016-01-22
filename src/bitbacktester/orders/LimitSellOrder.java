/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.orders;

import bitbacktester.positions.Position;
import java.time.LocalDateTime;

/**
 *
 * @author jmaciak
 */
public class LimitSellOrder extends LimitOrder {

    public LimitSellOrder(String asset, double amount, double price) {
        super(OrderType.SELL, asset, amount, price);
    }
    public LimitSellOrder(LocalDateTime time, String asset, double amount, double price) {
        super(OrderType.SELL, asset, time, amount, price);
    }    
    @Override public String toString() {
        return "LimitSellOrder{time=" + getTime() + ", asset=" + getAsset() + ", amount=" + getAmount() + ", price=" + getPrice() + ", value=" + getValue() + "}"; 
    }

    @Override
    public Position createPosition() {
        return null;
    }
}
