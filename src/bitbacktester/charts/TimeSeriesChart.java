/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bitbacktester.charts;

/**
 *
 * @author jmaciak
 */
import bitbacktester.BitBacktester;
import bitbacktester.datafeed.TSElement;
import bitbacktester.datafeed.TimeSeries;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
 
 
public class TimeSeriesChart extends Application {
    private TimeSeries ts;
 
    public TimeSeriesChart() {
        super();
        this.ts = BitBacktester.perf.DAILY_PORTFOLIO_VALUE;
    }
    public void setTimeSeries(TimeSeries ts) {
        this.ts = ts;
    }
    @Override public void start(Stage stage) {
        stage.setTitle("Line Chart Sample");
        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Number of Month");
        //creating the chart
        final LineChart<String,Number> lineChart = new LineChart<>(xAxis,yAxis);
                
        lineChart.setCreateSymbols(false);
        lineChart.setTitle(ts.getName());
        //defining a series
        XYChart.Series series = new XYChart.Series();
        series.setName("Portfolio");
        //populating the series with data
        for(TSElement e : ts.toList()) {
            series.getData().add(new XYChart.Data(e.getDatetime().toLocalDate().toString(), e.get()));
        }

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(series);
        stage.setScene(scene);
        stage.show();
    }
    public void show() {
        launch();
    }
}
