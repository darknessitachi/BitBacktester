package bitbacktester.tradingstrategies;

import bitbacktester.InsufficientFundsException;
import bitbacktester.InvalidOrderTypeException;
import bitbacktester.riskmanagement.RiskManager;
import bitbacktester.broker.Broker;
import bitbacktester.datafeed.DataFeed;
import bitbacktester.datafeed.HistoricalData;
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
    }

    @Override
    protected void onBar(Tick tick) throws InsufficientFundsException, InvalidOrderTypeException, CantCreatePositionException, OrderCantCloseException {
        if(dataFeed.getHistory().size() == 0) {
            broker.limitBuyOrder("BTCCNY", 1, tick.getClose());
            broker.getPortfolio().getPositions().get(0).setStopLoss(tick.getClose() - 1.0);
        }
    }
}
