/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.positionsizer;

import bitbacktester.broker.Portfolio;

/**
 * An implementation of a PositionSizer.
 * Calculates the position size as a pre-defined percent of a portfolio's
 * cash.
 * @author jmaciak
 */
public class PercentPositionSizer extends PositionSizer {
    private final double percent;
    public PercentPositionSizer(Portfolio portfolio, double percent) {
        super(portfolio);
        this.percent = percent;
    }
    @Override
    public double calculatePositionSize() {
        return percent * portfolio.getCash();
    }
    
}
