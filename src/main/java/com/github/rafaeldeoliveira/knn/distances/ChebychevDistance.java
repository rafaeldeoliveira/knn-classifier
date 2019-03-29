package com.github.rafaeldeoliveira.knn.distances;

import com.github.rafaeldeoliveira.knn.model.Row;

public class ChebychevDistance implements DistanceMethod {
    @Override
    public double calculate(Row element, Row refElement) {
        double max = 0d;
        for (int i = 0; i < element.getColumnCount(); i++) {
            double result = Math.abs(element.getColumn(i) - refElement.getColumn(i));
            if (result > max) {
                max = result;
            }

        }
        return max;
    }

}
