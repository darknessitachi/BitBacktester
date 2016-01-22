/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.tests;

import bitbacktester.datafeed.HistoricalData;
import bitbacktester.datafeed.Tick;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author jmaciak
 */
public class HistoricalDataTest {
    public static void test() throws IOException {
        System.out.println("Reading from csv...");
        HistoricalData hist = HistoricalData.loadFromCSV(new File("C:\\Users\\jmaciak\\Desktop\\btcnCNY.csv"));
        for(Tick t : hist.toList()) {
            System.out.println(t);
        }
        System.out.println("Translating to minute data...");
        hist = HistoricalData.toMinute(hist);
        System.out.println("Writing to csv...");
        HistoricalData.writeToCSV(hist, new File("C:\\Users\\jmaciak\\Desktop\\btcnCNYminute.csv"));
    }
}
