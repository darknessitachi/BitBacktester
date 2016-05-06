/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.datafeed;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * The HistoricalData class is a representation of historical asset data. Each
 * element of data -- or tick -- is represented by the Tick class. Historical
 * data can be queried using the getTick(...) methods - either by index or by the 
 * LocalDateTime associated with a tick object. Historical Data is typically 
 * loaded via a comma-separated file, which can be done via the loadFromCSV(...)
 * method.
 * @author jmaciak
 */
public class HistoricalData {
    List<Tick> data = new ArrayList<>();
    
    public static HistoricalData loadFromCSV(File f) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        List<Tick> ticks = new ArrayList<>();
        String[] parts;
        String line;
        while((line = br.readLine()) != null) {
            parts = line.split(",");
            Tick t = new Tick(LocalDateTime.ofEpochSecond(Long.parseLong(parts[0]), 0, ZoneOffset.UTC));
            t.setClose(Double.parseDouble(parts[1]));
            t.setHigh(t.getClose());
            t.setLow(t.getClose());
            t.setOpen(t.getClose());
            t.setVolume(Double.parseDouble(parts[2]));
            ticks.add(t);
        }
        
        return new HistoricalData(ticks);
    }
    public static HistoricalData loadFromCSV(File f, int numLines) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new FileReader(f));
        List<Tick> ticks = new ArrayList<>();
        
        int counter = 0;
        String line;
        while((line = br.readLine()) != null && counter < numLines) {
            String[] parts = line.split(",");
            Tick t = new Tick(LocalDateTime.ofEpochSecond(Long.parseLong(parts[0]), 0, ZoneOffset.UTC));
            t.setClose(Double.parseDouble(parts[1]));
            t.setHigh(t.getClose());
            t.setLow(t.getClose());
            t.setOpen(t.getClose());
            t.setVolume(Double.parseDouble(parts[2]));
            ticks.add(t);
            counter++;
        }
        
        return new HistoricalData(ticks);
    }    
    public static void writeToCSV(HistoricalData data, File f) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        for(Tick t : data.toList()) {
            bw.write(t.getDatetime().toEpochSecond(ZoneOffset.UTC) + "," + t.getOpen() + "," + t.getHigh() + "," + t.getLow() + "," + t.getClose() + "," + t.getVolume());
            bw.newLine();
        }
        bw.close();
    }
    public HistoricalData(List<Tick> data) {
        this.data = data;
    }
    public HistoricalData() {
        this.data = new ArrayList<>();
    }
    public int size() {
        return data.size();
    }
    class TimeComparator implements Comparator<Tick> {
        @Override
        public int compare(Tick o1, Tick o2) {
           return o1.getDatetime().compareTo(o2.getDatetime());
        }
    }
    /**
     * Returns all ticks before a given index.
     * @param index
     * @return 
     */
    public HistoricalData getBefore(int index) {
        List<Tick> ticks = new ArrayList<>();
        for(int i = 0; i < index; ++i) {
            ticks.add(data.get(i));
        }
        return new HistoricalData(ticks);
    }
    /**
     * Returns a HistoricalData object representing all data before a given time.
     * @param time
     * @return 
     */
    public HistoricalData getBefore(LocalDateTime time) {
        List<Tick> ticks = new ArrayList<>();
        for(Tick t : data) {
            if(t.getDatetime().isBefore(time)) {
                ticks.add(t);
            }
        }
        return new HistoricalData(ticks);
    }
    /**
     * Returns the tick at the specified index
     * @param index
     * @return 
     */
    public Tick getTick(int index) {
        return data.get(index);
    }
    /**
     * Returns the last tick available.
     * @return 
     */
    public Tick getLastTick() {
        if(data.isEmpty()) {
            return null;
        }
        return data.get(data.size() - 1);
    }
    public static HistoricalData toMinute(HistoricalData data) {
        LocalDateTime time = null;
        List<Tick> minuteTicks = new ArrayList<>();
        int highestSeconds = 0;
        int lowestSeconds  = 60;
        Tick minuteTick = null;
        for(Tick t : data.toList()) {
            if(time == null) {
                time = t.getDatetime();
                minuteTick = new Tick(LocalDateTime.ofEpochSecond(t.getDatetime().toEpochSecond(ZoneOffset.UTC) - t.getDatetime().getSecond(), 0, ZoneOffset.UTC));
                minuteTick.setLow(10000); //Magic really big number
            }
            if( t.getDatetime().getMinute() != time.getMinute() ) {
                time = t.getDatetime();
                highestSeconds = 0;
                lowestSeconds  = 60;
                minuteTicks.add(minuteTick);
                minuteTick = new Tick(LocalDateTime.ofEpochSecond(t.getDatetime().toEpochSecond(ZoneOffset.UTC) - t.getDatetime().getSecond(), 0, ZoneOffset.UTC));
                minuteTick.setLow(10000); //Magic really big number
            }
            if(t.getDatetime().getMinute() == time.getMinute()) {
                if(t.getDatetime().getSecond() < lowestSeconds) {
                    minuteTick.setOpen(t.getClose());
                    lowestSeconds = t.getDatetime().getSecond();
                }
                if(t.getDatetime().getSecond() > highestSeconds) {
                    minuteTick.setClose(t.getClose());
                    highestSeconds = t.getDatetime().getSecond();
                }
                if(t.getHigh() >= minuteTick.getHigh()) {
                    minuteTick.setHigh(t.getHigh());
                }
                if(t.getLow() <= minuteTick.getLow()) {
                    minuteTick.setLow(t.getLow());
                }
                minuteTick.setVolume(minuteTick.getVolume() + t.getVolume());
            }
        }
        minuteTicks.add(minuteTick);
        return new HistoricalData(minuteTicks);
    }
    public static HistoricalData toHour(HistoricalData data) {
        LocalDateTime time = null;
        List<Tick> hourTicks = new ArrayList<>();
        int highestMinutes = 0;
        int lowestMinutes  = 60;
        Tick hourTick = null;
        for(Tick t : data.toList()) {
            if(time == null) {
                time = t.getDatetime();
                hourTick = new Tick(LocalDateTime.ofEpochSecond(t.getDatetime().toEpochSecond(ZoneOffset.UTC) - t.getDatetime().getMinute() * 60 - t.getDatetime().getSecond(), 0, ZoneOffset.UTC));
                hourTick.setLow(10000); //Magic really big number
            }
            if( t.getDatetime().getHour() != time.getHour() ) {
                time = t.getDatetime();
                highestMinutes = 0;
                lowestMinutes  = 60;
                hourTicks.add(hourTick);
                hourTick = new Tick(LocalDateTime.ofEpochSecond(t.getDatetime().toEpochSecond(ZoneOffset.UTC) - t.getDatetime().getMinute()*60 - t.getDatetime().getSecond(), 0, ZoneOffset.UTC));
                hourTick.setLow(10000); //Magic really big number
            }
            if(t.getDatetime().getHour() == time.getHour()) {
                if(t.getDatetime().getMinute() < lowestMinutes) {
                    hourTick.setOpen(t.getClose());
                    lowestMinutes = t.getDatetime().getMinute();
                }
                if(t.getDatetime().getMinute() > highestMinutes) {
                    hourTick.setClose(t.getClose());
                    highestMinutes = t.getDatetime().getMinute();
                }
                if(t.getHigh() >= hourTick.getHigh()) {
                    hourTick.setHigh(t.getHigh());
                }
                if(t.getLow() <= hourTick.getLow()) {
                    hourTick.setLow(t.getLow());
                }
                hourTick.setVolume(hourTick.getVolume() + t.getVolume());
            }
        }
        hourTicks.add(hourTick);
        return new HistoricalData(hourTicks);
    }
    public List<Tick> toList() {
        return data;
    }
    /**
     * Returns the tick at the specified time.
     * @param time
     * @return 
     */
    public Tick getTick(LocalDateTime time) {
        for(Tick t : data) {
            if(t.getDatetime().equals(time)) {
                return t;
            }
        }
        return null;
    }
    /**
     * Returns the index of the tick at the given time.
     * @param time
     * @return 
     */
    public int getIndexOf(LocalDateTime time) {
        for(int i = 0; i < data.size(); ++i) {
            if(data.get(i).getDatetime().equals(time)) {
                return i;
            }
        }
        return -1;
    }
}
