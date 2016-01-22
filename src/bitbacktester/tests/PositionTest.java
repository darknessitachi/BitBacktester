/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.tests;

import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.orders.LimitBuyOrder;
import bitbacktester.orders.LimitCoverOrder;
import bitbacktester.orders.LimitSellOrder;
import bitbacktester.orders.LimitShortOrder;
import bitbacktester.orders.Order;
import bitbacktester.positions.LongPosition;
import bitbacktester.positions.OrderCantCloseException;
import bitbacktester.positions.Position;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmaciak
 */
public class PositionTest {
    public static void test() throws CantCreatePositionException {
        Order initialOrder = new LimitBuyOrder("ASSET", 2, 1.00);
        Position p = initialOrder.createPosition();
        System.out.println(p);
        try {
            p.add(new LimitSellOrder("ASSET", 1, 2.00 ));
        } catch (OrderCantCloseException ex) {
            Logger.getLogger(PositionTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        double amountOutstanding = p.calcAmountOutstanding();
        double realizedPGain = p.calcRealizedPercentGain();
        double realizedPnL = p.calcRealizedPnL();
        Order openingOrder = p.getOpeningOrder();
        double unrealizedValue = p.calcUnrealizedValue(2.00);
        double unrealizedPnL   = p.calcUnrealizedPnL(2.00);
        boolean isOpen = p.isOpen();
        System.out.println(p);
        
    }
}
