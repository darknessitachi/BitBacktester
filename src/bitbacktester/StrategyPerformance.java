package bitbacktester;

import bitbacktester.broker.Portfolio;
import bitbacktester.datafeed.TSElement;
import bitbacktester.datafeed.TimeSeries;
import bitbacktester.positions.Position;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import stat.Distribution;

/**
 *
 * @author jmaciak
 */
public class StrategyPerformance {
    public int NUM_TRADES;
    public int NUM_WINNING_TRADES;
    public int NUM_LOSING_TRADES;
    public double AVERAGE_PROFIT_PER_TRADE;
    public double TOTAL_PERCENT_RETURN;
    public double TOTAL_DOLLAR_RETURN;
    public double FINAL_VALUE;
    public double MAX_DRAWDOWN;
    public double INITIAL_VALUE;
    public double SHARPE_RATIO;
    public double SORTINO_RATIO;
    public double STANDARD_DEVIATION;
    public double AVERAGE_DAILY_RETURN;
    public double AVERAGE_PERCENT_PROFIT_PER_TRADE;
    public double AVERAGE_LOSER_PERCENT_LOSS;
    public double VAR;
    
    //RUN-TIME vars
    public int NUM_OPEN_POSITIONS;
    public int NUM_CLOSED_POSITIONS;
    public TimeSeries DAILY_PORTFOLIO_VALUE = new TimeSeries("Daily Portfolio Value"); //Portfolio value over time
    public TimeSeries DAILY_RETURNS = new TimeSeries("Daily Returns"); //Absolute returns p_i - p_(i-1)
    public TimeSeries DAILY_LOG_RETURNS = new TimeSeries("Daily Log Returns"); // log(1 + r_t)
    public TimeSeries DAILY_PERCENT_RETURNS = new TimeSeries("Daily Percent Returns");
    public void update(Portfolio portfolio) {
        List<Position> positions = portfolio.getPositions();
        double totalProfit = 0;
        double percentGain = 0;
        double averageLoserPercentLoss = 0;
        for(Position p : positions) {
            totalProfit += p.calcRealizedPnL();
            percentGain += p.calcRealizedPercentGain();
            if(p.calcRealizedPnL() >= 0) {
                NUM_WINNING_TRADES += 1;
            } else {
                NUM_LOSING_TRADES += 1;
                averageLoserPercentLoss += p.calcRealizedPercentGain();
            }
        }
        NUM_TRADES  = positions.size();
        FINAL_VALUE = portfolio.getCash();
        INITIAL_VALUE = portfolio.getInitialCash();
        TOTAL_DOLLAR_RETURN = totalProfit;
        AVERAGE_PERCENT_PROFIT_PER_TRADE = percentGain / (double) NUM_TRADES;
        AVERAGE_LOSER_PERCENT_LOSS = averageLoserPercentLoss / (double) NUM_LOSING_TRADES;
        AVERAGE_PROFIT_PER_TRADE = TOTAL_DOLLAR_RETURN / (double) NUM_TRADES;
        
        TOTAL_PERCENT_RETURN = FINAL_VALUE / INITIAL_VALUE - 1;
        MAX_DRAWDOWN = maxDrawdown();
        for(int i = 0; i < DAILY_PORTFOLIO_VALUE.size(); i++) {
            if(i == 0) {
                DAILY_RETURNS.add(new TSElement(DAILY_PORTFOLIO_VALUE.get(i).getDatetime(), 0));
                DAILY_LOG_RETURNS.add(new TSElement(DAILY_PORTFOLIO_VALUE.get(i).getDatetime(), Math.log(0)));
                DAILY_PERCENT_RETURNS.add(new TSElement(DAILY_PORTFOLIO_VALUE.get(i).getDatetime(), Math.log(0)));
            }
            else {
                DAILY_RETURNS.add(new TSElement(DAILY_PORTFOLIO_VALUE.get(i).getDatetime(), DAILY_PORTFOLIO_VALUE.get(i).get() - DAILY_PORTFOLIO_VALUE.get(i - 1).get()));
                DAILY_LOG_RETURNS.add(new TSElement(DAILY_PORTFOLIO_VALUE.get(i).getDatetime(), Math.log(DAILY_PORTFOLIO_VALUE.get(i).get() / DAILY_PORTFOLIO_VALUE.get(i - 1).get())));
                DAILY_PERCENT_RETURNS.add(new TSElement(DAILY_PORTFOLIO_VALUE.get(i).getDatetime(), DAILY_PORTFOLIO_VALUE.get(i).get() / DAILY_PORTFOLIO_VALUE.get(i - 1).get() - 1));

            }
        }
        
        Distribution dist = new Distribution();
        List<Double> data = new ArrayList<>();
        for(TSElement ts : DAILY_PERCENT_RETURNS.toList()) {
            data.add(ts.get());
        }
        dist.add(data);
        VAR = dist.get(.01);
        STANDARD_DEVIATION = stdev();
        SHARPE_RATIO       = sharpe();
        SORTINO_RATIO      = sortino();
    }
    private double maxDrawdown() {
        double peak = 0;
        double dd = 0;
        double maxDD = 0;
        for(int i = 0; i < DAILY_PORTFOLIO_VALUE.size(); ++i) {
            if( DAILY_PORTFOLIO_VALUE.get(i).get() > peak) {
                peak = DAILY_PORTFOLIO_VALUE.get(i).get();
            }
            dd = (DAILY_PORTFOLIO_VALUE.get(i).get() - peak) / peak;
            if(dd < maxDD) {
                maxDD = dd;
            }
        }
        return maxDD;
    }
    private double stdev() {
        double sum = 0;
        for(TSElement e : DAILY_RETURNS.toList()) {
            sum += e.get();
        }
        AVERAGE_DAILY_RETURN = sum / DAILY_RETURNS.size();
        double squaredSum = 0;
        for(TSElement e : DAILY_RETURNS.toList()) {
            squaredSum += Math.pow(e.get() - AVERAGE_DAILY_RETURN, 2);
        }
        return Math.sqrt(squaredSum / ( DAILY_RETURNS.size() - 1));
    }
    private double sharpe() {
        return AVERAGE_DAILY_RETURN/STANDARD_DEVIATION * Math.sqrt(365);
    }
    private double sortino() {
        double sum = 0;
        double T = 0;
        double R = AVERAGE_DAILY_RETURN;
        double TDD = 0;
        for(TSElement ts : DAILY_RETURNS.toList()) {
            if(ts.get() >= T)
                TDD += 0;
            else
                TDD += Math.pow(ts.get() - T, 2);
        }
        TDD = TDD / DAILY_RETURNS.size();
        TDD = Math.sqrt(TDD);
        return (R - T) / TDD;
    }
    public void writeToFile(File f) {
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter(f));
            br.write(this.toString());
            br.close();
            br = new BufferedWriter(new FileWriter("DAILY_PORTFOLIO_VALUE"));
            for(TSElement e : DAILY_PORTFOLIO_VALUE.toList()) {
                br.write(e.getDatetime() + ", " + e.get());
                br.newLine();
            }
            br.close();
            
            br = new BufferedWriter(new FileWriter("DAILY_RETURNS"));
            for(TSElement e : DAILY_RETURNS.toList()) {
                br.write(e.getDatetime() + ", " + e.get());
                br.newLine();
            }
            br.close();
            
            br = new BufferedWriter(new FileWriter("DAILY_LOG_RETURNS"));
            for(TSElement e : DAILY_LOG_RETURNS.toList()) {
                br.write(e.getDatetime() + ", " + e.get());
                br.newLine();
            }
            br.close();
            
            br = new BufferedWriter(new FileWriter("DAILY_PERCENT_RETURNS"));
            for(TSElement e : DAILY_PERCENT_RETURNS.toList()) {
                br.write(e.getDatetime() + ", " + e.get());
                br.newLine();
            }
            br.close();
                
        } catch (IOException ex) {
            Logger.getLogger(StrategyPerformance.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @Override
    public String toString() {
        String s = new String();
        s += "NUM_TRADES|" + NUM_TRADES + "\r\n";
        s += "INITIAL_VALUE|" + INITIAL_VALUE + "\r\n";
        s += "FINAL_VALUE|" + FINAL_VALUE + "\r\n";
        s += "NUM_WINNING_TRADES|" + NUM_WINNING_TRADES + "\r\n";
        s += "NUM_LOSING_TRADES|" + NUM_LOSING_TRADES + "\r\n";
        s += "AVERAGE_PROFIT_PER_TRADE|" + AVERAGE_PROFIT_PER_TRADE + "\r\n"; 
        s += "AVERAGE_PERCENT_PROFIT_PER_TRADE|" + AVERAGE_PERCENT_PROFIT_PER_TRADE + "\r\n";
        s += "AVERAGE_LOSER_PERCENT_LOSS|" + AVERAGE_LOSER_PERCENT_LOSS + "\r\n";
        s += "TOTAL_PERCENT_RETURN|" + TOTAL_PERCENT_RETURN + "\r\n";
        s += "TOTAL_DOLLAR_RETURN|" + TOTAL_DOLLAR_RETURN + "\r\n";
        s += "MAX_DRAWDOWN|" + MAX_DRAWDOWN + "\r\n";
        s += "STANDARD_DEVIATION_DAILY_RETURNS|" + STANDARD_DEVIATION + "\r\n";
        s += "SHARPE_RATIO|" + SHARPE_RATIO + "\r\n";
        s += "VAR|" + VAR + "\r\n";
        s += "SORTINO_RATIO|" + SORTINO_RATIO + "\r\n";
        return s;
    }
}