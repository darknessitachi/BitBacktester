package bitbacktester.broker;

import bitbacktester.InsufficientFundsException;
import bitbacktester.InvalidOrderTypeException;
import bitbacktester.datafeed.DataFeed;
import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.orders.Order;
import bitbacktester.orders.OrderType;
import bitbacktester.positions.OrderCantCloseException;
import bitbacktester.positions.Position;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmaciak
 */
public class Portfolio {
    List<Position> positions;
    private final double initialCash;
    private double currentCash;
    private double currentAmount;
    
    public Portfolio(double initialCash) {
        positions = new ArrayList<>();
        this.initialCash = initialCash;
        this.currentCash = initialCash;
        
    }
    public List<Position> getPositions() {
        return positions;
    }
    /**
     * Adds an order to the portfolio, either opening a new position or adding 
     * to an already open one.
     * @param o
     * @throws InsufficientFundsException
     * @throws CantCreatePositionException
     * @throws OrderCantCloseException 
     * @throws bitbacktester.InvalidOrderTypeException 
     */
    public void add(Order o) throws InsufficientFundsException, CantCreatePositionException, OrderCantCloseException, InvalidOrderTypeException {
        Position foundPosition = findExistingPosition(o);
        if(!sufficientFunds(o)) {
            throw new InsufficientFundsException("Order " + o + " greater than current funds.");
        }
        
        if(foundPosition == null) {
            if(o.getType().equals(OrderType.SELL) || o.getType().equals(OrderType.COVER)) {
                throw new InvalidOrderTypeException("Cannot open position based on invalid order type: " + o.getType());
            }
            currentCash -= o.getValue();
            currentAmount += o.getAmount();
            positions.add(o.createPosition());
        } else {
            if(o.getType().equals(OrderType.BUY) || o.getType().equals(OrderType.SHORT)) {
                currentCash -= o.getValue();
                currentAmount += o.getAmount();
            }
            if(o.getType().equals(OrderType.COVER)) {
                currentCash += o.getValue() + foundPosition.calcOpeningValue();
                currentAmount -= o.getAmount();
            }
            if(o.getType().equals(OrderType.SELL)) {
                currentCash += o.getValue();
                currentAmount -= o.getAmount();
            }
            foundPosition.add(o);
        }
        //Subtract fee from current cash regardless of order type
        currentCash -= o.getFee().calc();
    }
    /**
     * Determines whether or not there are sufficient funds to open a position.
     * @param o
     * @return 
     */
    private boolean sufficientFunds(Order o) {
        switch(o.getType()) {
            case BUY:
                return (currentCash - (o.getValue() + o.getFee().calc()) >= 0);
            case SHORT:
                return (currentCash - (o.getValue() + o.getFee().calc()) >= 0);
            case SELL:
                return (currentAmount - o.getAmount() >= 0) && ((currentCash - o.getFee().calc()) >= 0);
            case COVER:
                return (currentAmount - o.getAmount() >= 0) && ((currentCash - o.getFee().calc()) >= 0);                
        }
        return false;
    }
    /**
     * Attempts to find a position that is open & that fits a certain order
     * @param o
     * @return 
     */
    private Position findExistingPosition(Order o) {
        for(Position p : this.getPositions()) {
            if(p.canAdd(o)) {
                return p;
            }
        }
        return null;
    }
    /**
     * Calculates the realized PnL of the portfolio, not including fees
     * @return 
     */
    public double calcRealizedPnL() {
        double pnl = 0;
        for(Position p : positions) {
            pnl += p.calcRealizedPnL();
        }
        
        return pnl;
    }
    /**
     * Returns the value of all the positions if they are marked to market.
     * @param df
     * @return 
     */
    public double markToMarket(DataFeed df) {
        double sum = 0;
        for(Position p : positions) {
            sum += p.calcValue(df.getTick().getClose());
        }
        return sum + currentCash;
                
    }
    public int getNumOpenPositions() {
        int num = 0;
        for(Position p : positions) {
            if(p.isOpen())
                ++num;
        }
        return num;
    } 
    @Override
    public String toString() {
        return "Portfolio{initialCash=" + initialCash + ", currentCash=" + currentCash + ", currentAmount=" + currentAmount + ", realizedPnL=" + calcRealizedPnL() + "}";
    }
}