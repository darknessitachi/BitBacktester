package bitbacktester;

import bitbacktester.broker.Broker;
import bitbacktester.broker.PercentFee;
import bitbacktester.broker.Portfolio;
import bitbacktester.datafeed.HistoricalData;
import bitbacktester.datafeed.LocalDataFeed;
import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.tests.PositionTest;
import bitbacktester.tradingstrategies.TestStrategy;
import bitbacktester.tradingstrategies.TradingStrategy;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author jmaciak
 */
public class BitBacktester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, CantCreatePositionException {
        TradingStrategy ts = new TestStrategy(new Broker(new Portfolio(1000000), new PercentFee(0)), null, new LocalDataFeed(HistoricalData.loadFromCSV(new File("C:\\Users\\jmaciak\\Desktop\\btcnCNY.csv"))));
        ts.run();
       // PositionTest.test();
    }
    
}
