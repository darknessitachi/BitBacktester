/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bitalgotrader;

/**
 *
 * @author jmaciak
 */
enum TimeFrame {
    MINUTELY, HOURLY, DAILY, MONTHLY, YEARLY
}
public class StrategyPerformance {
    private int numTrades;
    private int numWinningTrades;
    private int numLosingTrades;
    private double averageProfitPerTrade;
    private double totalPercentReturn;
    private double totalDollarReturn;
    private double maxDrawdown;
 
//    public TimeSeries calculateReturns(TimeFrame timeFrame) {
 //   }
    
    
}
