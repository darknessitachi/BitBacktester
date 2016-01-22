/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.tests;

import bitbacktester.broker.Broker;
import bitbacktester.broker.FlatFee;
import bitbacktester.InsufficientFundsException;
import bitbacktester.InvalidOrderTypeException;
import bitbacktester.broker.PercentFee;
import bitbacktester.broker.Portfolio;
import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.orders.LimitBuyOrder;
import bitbacktester.positions.OrderCantCloseException;

/**
 *
 * @author jmaciak
 */
public class BrokerTest {
    public static void test() throws InsufficientFundsException, CantCreatePositionException, OrderCantCloseException, InvalidOrderTypeException {
        Broker b = new Broker(new Portfolio(10), new PercentFee(.00));
        b.placeOrder(new LimitBuyOrder("ASSET", 1, 5.00));
        System.out.println(b);
    }
}
