/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.orders;

import bitbacktester.positions.LongPosition;
import bitbacktester.positions.Position;
import java.time.LocalDateTime;

/**
 *
 * @author jmaciak
 */
public class LimitBuyOrder extends LimitOrder {

    public LimitBuyOrder(String asset, double amount, double price) {
        super(OrderType.BUY, asset, amount, price);
    }
    public LimitBuyOrder(LocalDateTime time, String asset, double amount, double price) {
        super(OrderType.BUY, asset, time, amount, price);
    }
    @Override public String toString() {
        return "LimitBuyOrder{time=" + getTime() + ", asset=" + getAsset() + ", amount=" + getAmount() + ", price=" + getPrice() + ", value=" + getValue() + "}"; 
    }

    @Override
    public Position createPosition() {
        return new LongPosition(this);
    }
}
