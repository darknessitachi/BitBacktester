/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.tests;

import bitbacktester.InsufficientFundsException;
import bitbacktester.InvalidOrderTypeException;
import bitbacktester.broker.Portfolio;
import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.orders.LimitBuyOrder;
import bitbacktester.orders.LimitCoverOrder;
import bitbacktester.orders.LimitSellOrder;
import bitbacktester.orders.LimitShortOrder;
import bitbacktester.positions.OrderCantCloseException;

/**
 *
 * @author jmaciak
 */
public class PortfolioTest {
    public static void test() throws InsufficientFundsException, CantCreatePositionException, OrderCantCloseException, InvalidOrderTypeException {
        /*Portfolio portfolio = new Portfolio(100.00);
        System.out.println(portfolio);
        portfolio.add(new LimitBuyOrder("ASSET", 1, 10.0));
        System.out.println(portfolio);
        portfolio.add(new LimitSellOrder("ASSET", 1, 20.0));
        System.out.println(portfolio);
    */}
}
