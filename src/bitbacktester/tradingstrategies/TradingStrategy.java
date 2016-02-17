/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.tradingstrategies;

import bitbacktester.InsufficientFundsException;
import bitbacktester.InvalidOrderTypeException;
import bitbacktester.StrategyPerformance;
import bitbacktester.riskmanagement.RiskManager;
import bitbacktester.datafeed.DataFeed;
import bitbacktester.broker.Broker;
import bitbacktester.datafeed.TSElement;
import bitbacktester.datafeed.Tick;
import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.positions.OrderCantCloseException;
import bitbacktester.positions.Position;
import bitbacktester.positions.PositionType;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmaciak
 */
public abstract class TradingStrategy {
    public String STRATEGY_NAME;
    public String STRATEGY_DESCRIPTION;
    protected RiskManager riskManager;
    protected Broker broker;
    protected DataFeed dataFeed;
    private boolean logToConsole;
    private final StrategyPerformance performance;
    private final static Logger LOGGER = Logger.getLogger(TradingStrategy.class.getName()); 
    
    
    public TradingStrategy(Broker broker, RiskManager riskManager, DataFeed dataFeed) {
        this.broker = broker;
        this.riskManager = riskManager;
        this.dataFeed = dataFeed;
        this.performance = new StrategyPerformance();
    }
    
    protected abstract void onBar(Tick tick) throws InsufficientFundsException, InvalidOrderTypeException, CantCreatePositionException, OrderCantCloseException;
    public void run() {
        while(dataFeed.hasNext()) {
            Tick t = dataFeed.getTick();
            try {
                onBar(t);
            } catch (InsufficientFundsException | InvalidOrderTypeException | CantCreatePositionException | OrderCantCloseException ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage());
            }
            if(dataFeed.getTick().getDatetime().getHour() == 0 && dataFeed.getTick().getDatetime().getMinute() == 0 && dataFeed.getTick().getDatetime().getSecond() == 0) {  
                performance.DAILY_PORTFOLIO_VALUE.add(new TSElement(dataFeed.getTick().getDatetime(), broker.getPortfolio().markToMarket(dataFeed)));
                System.out.println("NEW DAY: " + dataFeed.getTick().getDatetime() + " Last tick: " + dataFeed.getHistory().getLastTick().getDatetime());
            }
            dataFeed.next();
        }
        cleanup();
    }
    public StrategyPerformance getPerformance() {
        return performance;
    }
    /**
     * Closes all open positions and updates StrategyPerformance
     */
    private void cleanup() {
        for(Position p : broker.getPortfolio().getPositions() ) {
            if(p.isOpen()) {
                try {
                    if(p.getType().equals(PositionType.LONG)) {
                        broker.limitSellOrder(p.getAsset(), p.calcAmountOutstanding(), dataFeed.getTick().getClose());
                    } else if(p.getType().equals(PositionType.SHORT)){
                        broker.limitCoverOrder(p.getAsset(), p.calcAmountOutstanding(), dataFeed.getTick().getClose());
                    }
                } catch (InsufficientFundsException | CantCreatePositionException | OrderCantCloseException | InvalidOrderTypeException ex) {
                    Logger.getLogger(TradingStrategy.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }
        performance.update(broker.getPortfolio());
        performance.writeToFile(new File("PERFORMANCE"));
    }
}