/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.datafeed;

import java.time.LocalDateTime;

/**
 *
 * @author jmaciak
 */
/**
 * A base class that represents a single element in a time series.
 * Any class that represents a more complex element should extend this class. The subclass should
 * define a constructor that will set the appropriate fields within the superclass.
 * @author jmaciak
 */
public class TSElement {
    private final LocalDateTime datetime;
    private double value;
    public LocalDateTime getDatetime() {
        return datetime;
    }
    public TSElement(LocalDateTime datetime, double value) {
        this.datetime = datetime;
        this.value = value;
    }
    public void set(double value) {
        this.value = value;
    }
    public double get() {
        return value;
    }
    @Override public String toString() {
        return "TSElement{time="+ datetime + ", value=" + value + "}";
    }
}
