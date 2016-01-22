package bitbacktester.orders;

import bitbacktester.positions.Position;
import java.time.LocalDateTime;

/**
 *
 * @author jmaciak
 */
public class LimitCoverOrder extends LimitOrder {

    public LimitCoverOrder(String asset, double amount, double price) {
        super(OrderType.COVER, asset, amount, price);
    }
    public LimitCoverOrder(LocalDateTime time, String asset, double amount, double price) {
        super(OrderType.COVER, asset, time, amount, price);
    }
    @Override public String toString() {
        return "LimitCoverOrder{time=" + getTime() + ", asset=" + getAsset() + ", amount=" + getAmount() + ", price=" + getPrice() + ", value=" + getValue() + "}"; 
    }    

    @Override
    public Position createPosition() {
        return null;
    }
}

