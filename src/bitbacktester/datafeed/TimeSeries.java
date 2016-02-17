/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.datafeed;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author jmaciak
 */
public class TimeSeries {
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Duration elementDuration;
    private List<TSElement> d;
    
    public TimeSeries(String name, List<TSElement> data) {
        this.name = name;
        this.d = data;
    }
    public TimeSeries(String name) {
        this.name = name;
        this.d = new ArrayList<>();
    }
    public TimeSeries() {
        this.d = new ArrayList<>();
    }
    public void add(TSElement element) {
        d.add(element);
    }
    public void add(List<TSElement> data) {
        d.addAll(data);
    }
    public int size() {
        return d.size();
    }
    public LocalDateTime getStartDatetime() {
        return d.get(0).getDatetime();
    }
    public LocalDateTime getEndDatetime() {
        return d.get(d.size() - 1).getDatetime();
    }
    public void setName(String name) {
        this.name = name;
    }
    public TSElement get(int i) {
        return d.get(i);
    }
    public TSElement getLast() {
        return d.get(d.size() - 1);
    }
    //public MovingAverage getMovingAverage(int length) {
    //    return new MovingAverage(length, this);
    //}
    public static List<Double> toList(TimeSeries ts) {
        List<Double> list = new ArrayList<>();
        for(int i = 0; i < ts.size(); ++i) {
            list.add(ts.get(i).get());
        }
        return list;
    }  
    public List<TSElement> toList() {
        return d;
    }
    @Override public String toString() {
        String s = "[" + name + "] Start:" + startDateTime + " End: " + endDateTime + " Element Duration: " + elementDuration + " = {" ;
        for(int i = 0; i < d.size(); ++i) {
            s += ", ";
            s += d.get(i);
        }
        s += " }";
        return s;
    }

    public String getName() {
        return name;
    }
}
