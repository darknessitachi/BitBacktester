package bitbacktester.datafeed;

import java.time.LocalDateTime;

/**
 *
 * @author jmaciak
 */
public class Tick extends TSElement {
    private double open;
    private double high;
    private double low;
    private double close;
    private double volume;
   
    public Tick(LocalDateTime time, double open, double high, double low, double close, double volume) {
        super(time, close);
        this.open = open;
        this.high = high;
        this.low  = low;
        this.close = close;
        this.volume = volume;
    }

    public Tick(LocalDateTime time) {
        super(time, 0);
    }
    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }
    public double getLow() {
        return low;
    }
    
    public void setLow(double low) {
        this.low = low;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume; 
    }
    @Override public String toString() {
        return "Tick{time=" + getDatetime() + ", open=" + getOpen() + ", high=" + getHigh() + ", low=" + getLow() + ", close=" + getClose() + ", volume=" + getVolume() + "}"; 
    }
}
