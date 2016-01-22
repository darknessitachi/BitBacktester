/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.orders;

import bitbacktester.positions.Position;
import bitbacktester.positions.ShortPosition;
import java.time.LocalDateTime;

/**
 *
 * @author jmaciak
 */
public class LimitShortOrder extends LimitOrder {

    public LimitShortOrder(String asset, double amount, double price) {
        super(OrderType.SHORT, asset, amount, price);
    }
    public LimitShortOrder(LocalDateTime time, String asset, double amount, double price) {
        super(OrderType.SHORT, asset, time, amount, price);
    }
    @Override public String toString() {
        return "LimitShortOrder{time=" + getTime() + ", asset=" + getAsset() + ", amount=" + getAmount() + ", price=" + getPrice() +", value=" + getValue() + "}"; 
    }
    @Override
    public Position createPosition() {
        return new ShortPosition(this);
    }
}

