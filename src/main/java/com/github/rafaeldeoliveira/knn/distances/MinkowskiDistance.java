package com.github.rafaeldeoliveira.knn.distances;

import com.github.rafaeldeoliveira.knn.model.Row;

public class MinkowskiDistance implements DistanceMethod {
    @Override
    public double calculate(Row element, Row refElement) {
        double p = 1.5d;
        double sum = 0d;
        for (int i = 0; i < element.getColumnCount(); i++) {
            sum += Math.pow(Math.abs(element.getColumn(i) - refElement.getColumn(i)), p);
        }
        return Math.pow(sum, 1/p);
    }
}
