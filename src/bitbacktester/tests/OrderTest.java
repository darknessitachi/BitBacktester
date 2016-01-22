/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.tests;

import bitbacktester.orders.LimitBuyOrder;
import bitbacktester.orders.LimitCoverOrder;
import bitbacktester.orders.LimitOrder;
import bitbacktester.orders.LimitSellOrder;
import bitbacktester.orders.LimitShortOrder;
import bitbacktester.orders.Order;

/**
 *
 * @author jmaciak
 */
public class OrderTest {
    public static void test() {
        Order order = new LimitBuyOrder("ASSET", 10, 100);
        System.out.println(order);
        order = new LimitSellOrder("ASSET", 10, 100);
        System.out.println(order);
        order = new LimitShortOrder("ASSET", 10, 100);
        System.out.println(order);
        order = new LimitCoverOrder("ASSET", 10, 100);
        System.out.println(order);
    }
}
