/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.tradingstrategies;

import bitbacktester.InsufficientFundsException;
import bitbacktester.InvalidOrderTypeException;
import bitbacktester.broker.Broker;
import bitbacktester.datafeed.DataFeed;
import bitbacktester.datafeed.Tick;
import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.positions.OrderCantCloseException;
import bitbacktester.riskmanagement.RiskManager;

/**
 *
 * @author jmaciak
 */
public class BuyAndHoldStrategy extends TradingStrategy {

    public BuyAndHoldStrategy(Broker broker, RiskManager riskManager, DataFeed dataFeed) {
        super(broker, riskManager, dataFeed);
    }

    @Override
    protected void onBar(Tick tick) throws InsufficientFundsException, InvalidOrderTypeException, CantCreatePositionException, OrderCantCloseException {
        if(dataFeed.getHistory().size() == 0) {
            double currentPrice = dataFeed.getTick().getClose();
            broker.limitBuyOrder("BTCCNY", broker.getPortfolio().getCash() / currentPrice, currentPrice);
        }
    }
    
}
