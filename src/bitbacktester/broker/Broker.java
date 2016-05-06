package bitbacktester.broker;

import bitbacktester.InsufficientFundsException;
import bitbacktester.InvalidOrderTypeException;
import bitbacktester.broker.Portfolio;
import bitbacktester.broker.Fee;
import bitbacktester.datafeed.DataFeed;
import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.orders.LimitBuyOrder;
import bitbacktester.orders.LimitCoverOrder;
import bitbacktester.orders.LimitSellOrder;
import bitbacktester.orders.LimitShortOrder;
import bitbacktester.orders.Order;
import bitbacktester.positions.OrderCantCloseException;
import bitbacktester.positions.Position;
import bitbacktester.tradingstrategies.TradingStrategy;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Broker {
    private Portfolio portfolio;
    private Fee fee;
    private DataFeed dataFeed;
    public final static Logger LOGGER = Logger.getLogger(TradingStrategy.class.getName()); 

    public Broker(Portfolio portfolio, DataFeed dataFeed, Fee fee) {
        this.portfolio = portfolio;
        this.dataFeed = dataFeed;
        this.fee = fee;
    }
    /**
     * Places an order. Throws InsufficientFundsException if the portfolio does
     * not have enough cash to place the order.
     * @param o
     * @return 
     * @throws InsufficientFundsException 
     * @throws bitbacktester.orders.CantCreatePositionException 
     * @throws bitbacktester.positions.OrderCantCloseException 
     * @throws bitbacktester.InvalidOrderTypeException 
     */
    public Position placeOrder(Order o) throws InsufficientFundsException, CantCreatePositionException, OrderCantCloseException, InvalidOrderTypeException {
        //Apply the trading fee at the broker level.
        o.setFee(fee);
        LOGGER.log(Level.INFO, "{0}| Placed order {1}", new Object[] { dataFeed.getTick().getDatetime(), o});
        return portfolio.add(o);
    }
    public Position limitBuyOrder(String asset, double amount, double price) throws InsufficientFundsException, CantCreatePositionException, OrderCantCloseException, InvalidOrderTypeException {
        return placeOrder(new LimitBuyOrder(dataFeed.getTick().getDatetime(), asset, Math.floor(amount * 100) / 100, price)); //Math.floor ensures amount is in 3 decimal places
    }
    public Position limitSellOrder(String asset, double amount, double price) throws InsufficientFundsException, CantCreatePositionException, OrderCantCloseException, InvalidOrderTypeException {
                //System.out.println("Origamount: " + amount + "Round:" + Math.floor(amount * 100) / 100);
        return placeOrder(new LimitSellOrder(dataFeed.getTick().getDatetime(), asset, amount, price));
    }
    public Position limitShortOrder(String asset, double amount, double price) throws InsufficientFundsException, CantCreatePositionException, OrderCantCloseException, InvalidOrderTypeException {
                //System.out.println("Origamount: " + amount + "Round:" + Math.floor(amount * 100) / 100);

        return placeOrder(new LimitShortOrder(dataFeed.getTick().getDatetime(), asset, Math.floor(amount * 100) / 100, price));
    }
    public Position limitCoverOrder(String asset, double amount, double price) throws InsufficientFundsException, CantCreatePositionException, OrderCantCloseException, InvalidOrderTypeException {
                //System.out.println("Origamount: " + amount + "Round:" + Math.floor(amount * 100) / 100);
        return placeOrder(new LimitCoverOrder(dataFeed.getTick().getDatetime(), asset, amount, price));
    }
    public Portfolio getPortfolio() {
        return portfolio;
    }
    @Override public String toString() {
        return "Broker{portfolio=" + portfolio + ", fee=" + fee + "}";
    }
}