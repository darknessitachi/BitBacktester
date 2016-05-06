package bitbacktester;

import bitbacktester.broker.Broker;
import bitbacktester.broker.FlatFee;
import bitbacktester.broker.Portfolio;
import bitbacktester.charts.TimeSeriesChart;
import bitbacktester.datafeed.DataFeed;
import bitbacktester.datafeed.HistoricalData;
import bitbacktester.datafeed.LocalDataFeed;
import bitbacktester.datafeed.TSElement;
import bitbacktester.datafeed.TimeSeries;
import bitbacktester.orders.CantCreatePositionException;
import bitbacktester.riskmanagement.RiskManager;
import bitbacktester.tradingstrategies.BuyAndHoldStrategy;
import bitbacktester.tradingstrategies.MACrossLongShortStrategy;
import bitbacktester.tradingstrategies.TestStrategy;
import bitbacktester.tradingstrategies.TradingStrategy;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;

/**
 *
 * @author jmaciak
 */
public class BitBacktester {

    public static StrategyPerformance perf;
    public static void main(String[] args) throws IOException, CantCreatePositionException {
        DataFeed dataFeed = new LocalDataFeed(HistoricalData.loadFromCSV(new File("C:\\Users\\jmaciak\\Desktop\\btcnCNYHOURDATA.csv")));
        //dataFeed.setDataStart(LocalDateTime.parse("2015-07-19T21:00:00"));
        Portfolio p = new Portfolio(1300);
        Broker b = new Broker(p, dataFeed, new FlatFee(0));
        b.LOGGER.setLevel(Level.OFF);
        RiskManager rm = new RiskManager(b, dataFeed);
        TradingStrategy ts = new MACrossLongShortStrategy(b, rm, dataFeed);
        ts.run();
        perf = ts.getPerformance();
        TimeSeriesChart times = new TimeSeriesChart();
        times.show();
    }
}
