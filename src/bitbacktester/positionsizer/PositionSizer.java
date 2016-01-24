package bitbacktester.positionsizer;

import bitbacktester.broker.Portfolio;

/**
 * Abstract class representing a mechanism for determining position size. 
 * @author jmaciak
 */
public abstract class PositionSizer {
    protected final Portfolio portfolio;
    public PositionSizer(Portfolio portfolio) {
        this.portfolio = portfolio;
    }
    public abstract double calculatePositionSize();
}
