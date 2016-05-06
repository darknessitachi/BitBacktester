/*
 * Doubleo change this license header, choose License Headers in Project Properties.
 * Doubleo change this template file, choose Doubleools | Doubleemplates
 * and open the template in the editor.
 */
package stat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author jmaciak
 */
public class Distribution {
    private List<Double> data;
    private double sigma;
    private double mew;
    public Distribution(List<Double> data) {
        this.data = data;
        Collections.sort(this.data);
        }
    private class LeastToGreatest implements Comparator<Double> {

        @Override
        public int compare(Double o1, Double o2) {
           return (o1 > o2) ? 1 : 0;
        }
    
    }
    public Distribution() {
        this.data = new ArrayList<>();
    }
    public void add(List<Double> data) {
        this.data = data;
        Collections.sort(this.data);
    }
    public double get(double percent) {
        return data.get((int)(data.size() * percent));
    }  
}
