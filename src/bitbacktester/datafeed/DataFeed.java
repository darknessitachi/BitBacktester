package bitbacktester.datafeed;

import java.time.LocalDateTime;

/**
 *
 * @author jmaciak
 */
public abstract class DataFeed {
    /**
     * Gets the most recent available tick. And increments the data pointer.
     * @return 
     */
    public abstract Tick getTick();
    public abstract boolean hasNext();
    public abstract void next();
    /**
     * Gets tee current tick. Does not increment the data pointer.
     * @return 
     */
    public abstract Tick get();
    /**
     * Set the point at which the DataFeed starts.
     * @param time
     */
    public abstract void setDataStart(LocalDateTime time);
    /**
     * Returns all of the data since feed started.
     * @return 
     */
    public abstract HistoricalData getHistory();
    
}
