package bitbacktester.tradingstrategies;

import bitbacktester.InsufficientFundsException;
import bitbacktester.InvalidOrderTypeException;
import bitbacktester.RiskManager;
import bitbacktester.broker.Broker;
import bitbacktester.datafeed.DataFeed;
import bitbacktester.datafeed.Tick;
import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.positions.OrderCantCloseException;

/**
 *
 * @author jmaciak
 */
public class TestStrategy extends TradingStrategy {

    public TestStrategy(Broker broker, RiskManager riskManager, DataFeed dataFeed) {
        super(broker, riskManager, dataFeed);
        this.setLogToConsole(true);
    }

    @Override
    protected void onBar(Tick tick) throws InsufficientFundsException, InvalidOrderTypeException, CantCreatePositionException, OrderCantCloseException {
        if(dataFeed.getHistory().getLastTick() != null) {
            if(tick.getClose() > dataFeed.getHistory().getLastTick().getClose() && broker.getPortfolio().getNumOpenPositions() == 0) {
                    broker.limitBuyOrder("BTCCNY", 1, tick.getClose());
                    log(LogType.INFO, tick, "Bought 1 BTC @ " + tick.getClose() + " CNY");
            }
            else {
                log(LogType.INFO, tick, "Portfolio market value:" + broker.getPortfolio().markToMarket(dataFeed) + " Position pnl:" + broker.getPortfolio().getPositions().get(0).calcUnrealizedPnL(dataFeed.getTick().getClose()));
            }
        }
    }
    
}
