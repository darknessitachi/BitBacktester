/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.datafeed;

/**
 *
 * @author jmaciak
 */
public class LocalDataFeed extends DataFeed {
    private final HistoricalData data;
    private int incrementer;
    public LocalDataFeed(HistoricalData data) {
        this.data = data;
        this.incrementer = 0;
    }

    @Override
    public Tick getTick() {
        return data.getTick(incrementer);
    }

    @Override
    public HistoricalData getHistory() {
        return data.getBefore(incrementer);
    }

    @Override
    public boolean hasNext() {
        return incrementer < data.size();
    }
    /**
     * Gets tick at
     * @param index
     * @return 
     */
    @Override
    public Tick get() {
        return data.getTick(incrementer);
    }

    @Override
    public void next() {
        ++incrementer;
    }
    
}
