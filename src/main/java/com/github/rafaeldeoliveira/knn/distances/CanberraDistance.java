package com.github.rafaeldeoliveira.knn.distances;

import com.github.rafaeldeoliveira.knn.model.Row;

public class CanberraDistance implements DistanceMethod {
    @Override
    public double calculate(Row element, Row refElement) {
        if (element.getColumnCount() != refElement.getColumnCount()) {
            throw new RuntimeException("Incompatible elements, the columns count must be the same.");
        }
        double sum = 0d;
        for (int i = 0; i < element.getColumnCount(); i++) {
            sum += Math.abs(element.getColumn(i) - refElement.getColumn(i)) / Math.abs(element.getColumn(i) + refElement.getColumn(i));
        }
        return Math.sqrt(sum);
    }

}
