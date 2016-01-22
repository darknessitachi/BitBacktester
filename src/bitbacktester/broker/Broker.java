package bitbacktester.broker;

import bitbacktester.InsufficientFundsException;
import bitbacktester.InvalidOrderTypeException;
import bitbacktester.broker.Portfolio;
import bitbacktester.broker.Fee;
import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.orders.LimitBuyOrder;
import bitbacktester.orders.LimitCoverOrder;
import bitbacktester.orders.LimitSellOrder;
import bitbacktester.orders.LimitShortOrder;
import bitbacktester.orders.Order;
import bitbacktester.positions.OrderCantCloseException;

public class Broker {
    Portfolio portfolio;
    Fee fee;
    
    public Broker(Portfolio portfolio, Fee fee) {
        this.portfolio = portfolio;
        this.fee = fee;
    }
    /**
     * Places an order. Throws InsufficientFundsException if the portfolio does
     * not have enough cash to place the order.
     * @param o
     * @throws InsufficientFundsException 
     * @throws bitbacktester.orders.CantCreatePositionException 
     * @throws bitbacktester.positions.OrderCantCloseException 
     * @throws bitbacktester.InvalidOrderTypeException 
     */
    public void placeOrder(Order o) throws InsufficientFundsException, CantCreatePositionException, OrderCantCloseException, InvalidOrderTypeException {
        //Apply the trading fee at the broker level.
        o.setFee(fee);
        portfolio.add(o);
    }
    public void limitBuyOrder(String asset, double amount, double price) throws InsufficientFundsException, CantCreatePositionException, OrderCantCloseException, InvalidOrderTypeException {
        placeOrder(new LimitBuyOrder(asset, amount, price));
    }
    public void limitSellOrder(String asset, double amount, double price) throws InsufficientFundsException, CantCreatePositionException, OrderCantCloseException, InvalidOrderTypeException {
        placeOrder(new LimitSellOrder(asset, amount, price));
    }
    public void limitShortOrder(String asset, double amount, double price) throws InsufficientFundsException, CantCreatePositionException, OrderCantCloseException, InvalidOrderTypeException {
        placeOrder(new LimitShortOrder(asset, amount, price));
    }
    public void limitCoverOrder(String asset, double amount, double price) throws InsufficientFundsException, CantCreatePositionException, OrderCantCloseException, InvalidOrderTypeException {
        placeOrder(new LimitCoverOrder(asset, amount, price));
    }
    public Portfolio getPortfolio() {
        return portfolio;
    }
    @Override public String toString() {
        return "Broker{portfolio=" + portfolio + ", fee=" + fee + "}";
    }
}
