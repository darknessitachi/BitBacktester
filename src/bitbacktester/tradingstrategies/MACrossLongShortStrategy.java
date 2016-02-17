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
import bitbacktester.datafeed.HistoricalData;
import bitbacktester.datafeed.Tick;
import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.positions.OrderCantCloseException;
import bitbacktester.positions.Position;
import bitbacktester.positions.PositionType;
import bitbacktester.riskmanagement.RiskManager;

/**
 *
 * @author jmaciak
 */
public class MACrossLongShortStrategy extends TradingStrategy {

    public String ASSET = "BTCCNY";
    public MACrossLongShortStrategy(Broker broker, RiskManager riskManager, DataFeed dataFeed) {
        super(broker, riskManager, dataFeed);
        STRATEGY_NAME = "MACrossLongShort";
        STRATEGY_DESCRIPTION = "A strategy based on the 100/200 SMA cross. Strategy will go long on upcross & short on downcross.";
    }

    @Override
    protected void onBar(Tick tick) throws InsufficientFundsException, InvalidOrderTypeException, CantCreatePositionException, OrderCantCloseException {
        if(this.dataFeed.getHistory().size() >= 199) {
            if(calcMA(100) >= calcMA(200)) {
                if(broker.getPortfolio().getNumOpenPositions() > 0) { //If there is a position open
                    for(Position p : broker.getPortfolio().getPositions()) { 
                        if(p.getType().equals(PositionType.LONG) && p.isOpen()) { //If position is Long, do nothing
                         
                        }
                        if(p.getType().equals(PositionType.SHORT) && p.isOpen()) { //If short, close position b/c trend changed.
                            broker.limitCoverOrder(ASSET, p.calcAmountOutstanding(), tick.getClose());
                        }
                    }
                } else {
                    broker.limitBuyOrder(ASSET, broker.getPortfolio().getCash() / tick.getClose(), tick.getClose());
                }
            } else {
                if(broker.getPortfolio().getNumOpenPositions() > 0) { //If there is a position open
                    for(Position p : broker.getPortfolio().getPositions()) { 
                        if(p.getType().equals(PositionType.LONG) && p.isOpen()) { //If position is long, close
                            broker.limitSellOrder(ASSET, p.calcAmountOutstanding(), tick.getClose());
                        }
                        if(p.getType().equals(PositionType.SHORT) && p.isOpen()) { //If short, do nothing
                           
                        }
                    }
                } else {
                    broker.limitShortOrder(ASSET, broker.getPortfolio().getCash() / tick.getClose(), tick.getClose()); 
                }
            }
        }
    }
    
    private double calcMA(int length) {
        HistoricalData data = dataFeed.getHistory();
        double sum = dataFeed.getTick().getClose();
        for(int i = 1; i < length; ++i) {
            sum += data.getTick(data.size() - i).getClose();
        }
        return sum / length;
    }
}
