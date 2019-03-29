package com.github.rafaeldeoliveira.knn.distances;

import com.github.rafaeldeoliveira.knn.model.Row;

public class ManhattanDistance implements DistanceMethod {
    @Override
    public double calculate(Row element, Row refElement) {
        double sum = 0d;
        for (int i = 0; i < element.getColumnCount(); i++) {
            sum += Math.abs(element.getColumn(i) - refElement.getColumn(i));
        }
        return sum;
    }
}
