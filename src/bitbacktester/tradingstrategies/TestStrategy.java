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
        if(dataFeed.getHistory().size() > 200) {
            if(calcMA(dataFeed, 100) >= calcMA(dataFeed, 200) && broker.getPortfolio().getNumOpenPositions() == 0) {
                
                broker.limitBuyOrder("BTCCNY", broker.getPortfolio().getCash() / tick.getClose(), tick.getClose());
            }
            if(calcMA(dataFeed, 100) < calcMA(dataFeed, 200) && broker.getPortfolio().getNumOpenPositions() > 0) {
                broker.limitSellOrder("BTCCNY", broker.getPortfolio().getAmount() , tick.getClose());
            }
        }
    }
    private double calcMA(DataFeed df, int length) {
        HistoricalData data = df.getHistory();
        double sum = df.getTick().getClose();
        for(int i = 1; i < length; ++i) {
            sum += data.getTick(data.size() - i).getClose();
        }
        return sum / length;
    }
    
}
