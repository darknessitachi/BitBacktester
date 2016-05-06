/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester;

import bitbacktester.riskmanagement.RiskManager;
import bitbacktester.tradingstrategies.TradingStrategy;
import bitbacktester.broker.Portfolio;

/**
 *
 * @author jmaciak
 */
public class Backtest {
    private TradingStrategy strategy;
    private RiskManager riskManager;
    private Portfolio portfolio;
    private StrategyPerformance performance;
    
    public Backtest(TradingStrategy strategy, RiskManager riskManager, Portfolio portfolio) {
        this.strategy = strategy;
        this.riskManager = riskManager;
        this.portfolio = portfolio;
    }
    /**
     * Runs the backtest.
     */
    public void run() {
        strategy.run();
    }
}
