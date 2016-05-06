package bitbacktester.riskmanagement;

import bitbacktester.InsufficientFundsException;
import bitbacktester.InvalidOrderTypeException;
import bitbacktester.positionsizer.PositionSizer;
import bitbacktester.broker.Broker;
import bitbacktester.datafeed.DataFeed;
import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.positions.Position;
import bitbacktester.orders.Order;
import bitbacktester.positions.OrderCantCloseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class designed to handle some common risk management functions. Primarily
 * handling stop-losses, among other behavior to be added later. RiskManager
 * should be run within a TradingStrategy & should be passed the same Broker &
 * DataFeed inside TradingStrategy
 * @author jmaciak
 */
public class RiskManager {
    private final Broker broker;
    private final DataFeed dataFeed;
    private PositionSizer positionSizer;
    private final static Logger LOGGER = Logger.getLogger(RiskManager.class.getName()); 
    
    public RiskManager(Broker broker, DataFeed dataFeed) {
        this.broker   = broker;
        this.dataFeed = dataFeed; 
    }
    /**
     * Returns the recommended position size based on the class's PositionSizer.
     * @return 
     */
    public double getPositionSize() {
        return positionSizer.calculatePositionSize();
    }
    /**
     * Returns a PositionSizer.
     * @return 
     */
    public PositionSizer getPositionSizer() {
        return positionSizer;
    }
    /**
     * Set the PositionSizer.
     * @param positionSizer 
     */
    public void setPositionSizer(PositionSizer positionSizer) {
        this.positionSizer = positionSizer;
    }
    /**
     * Checks to see if any of the stop-losses have been hit. If so, it fills
     * the remaining position.
     */
    public void checkStopLosses() {
        for(Position p : broker.getPortfolio().getPositions()) {
            if(p.getStopLoss() == null) { //Skip check
                continue;
            }
            if(p.isOpen() && p.getStopLoss().isHit(dataFeed.getTick().getClose())) {
                LOGGER.log(Level.INFO, "Stop @ {0} is hit. Closing position.", p.getStopLoss().getPrice());
                Order o = p.getStopLoss().createOrder();
                o.setAmount(p.calcAmountOutstanding());
                try {
                    broker.placeOrder(o);
                } catch (InsufficientFundsException | CantCreatePositionException | OrderCantCloseException | InvalidOrderTypeException ex) {
                    LOGGER.log(Level.SEVERE, "{0}", ex);
                }
            }
        }
    }
}