/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.tradingstrategies;

import bitbacktester.InsufficientFundsException;
import bitbacktester.InvalidOrderTypeException;
import bitbacktester.RiskManager;
import bitbacktester.datafeed.DataFeed;
import bitbacktester.broker.Broker;
import bitbacktester.datafeed.Tick;
import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.positions.OrderCantCloseException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jmaciak
 */
public abstract class TradingStrategy {
    protected RiskManager riskManager;
    protected Broker broker;
    protected DataFeed dataFeed;
    private boolean logToConsole;
   
    
    public TradingStrategy(Broker broker, RiskManager riskManager, DataFeed dataFeed) {
        this.broker = broker;
        this.riskManager = riskManager;
        this.dataFeed = dataFeed;
    }
    public void log(LogType type, Tick t, String message) { 
        try {
            BufferedWriter logFile = new BufferedWriter(new FileWriter(type.toString(), true));
            logFile.write("[" + t.getDatetime() + "]" + message);
            logFile.newLine();
            logFile.close();
        } catch (IOException ex) {
            Logger.getLogger(TradingStrategy.class.getName()).log(Level.SEVERE, null, ex);
        }
        if(logToConsole) {
            System.err.println("[" + type + "] " +  t.getDatetime() + " " + message );
        }
    }
    protected abstract void onBar(Tick tick) throws InsufficientFundsException, InvalidOrderTypeException, CantCreatePositionException, OrderCantCloseException;
    public void setLogToConsole(boolean logToConsole) {
        this.logToConsole = logToConsole;
    }
    public void run() {
        while(dataFeed.hasNext()) {
            Tick t = dataFeed.getTick();
            try {
                onBar(t);
            } catch (InsufficientFundsException | InvalidOrderTypeException | CantCreatePositionException | OrderCantCloseException ex) {
                log(LogType.ERROR, t, ex.toString());
            }
            dataFeed.next();
        }
    }
}