package com.github.rafaeldeoliveira.knn.distances;

import com.github.rafaeldeoliveira.knn.model.Row;

public class CorrelationalDistance implements Distance {
    @Override
    public double calculate(Row element, Row refElement) {
        double distance = 0d;
        double avgElement = average(element);
        double avgRefElement = average(refElement);

        for (int i = 0; i < element.getColumnCount(); i++) {
            distance += (element.getColumn(i) - avgElement) * (refElement.getColumn(i) - avgRefElement);
        }

        double a = 0d;
        double b = 0d;

        for (int i = 0; i < element.getColumnCount(); i++) {
            a += (element.getColumn(i) - avgElement);
        }

        for (int i = 0; i < refElement.getColumnCount(); i++) {
            b += (refElement.getColumn(i) - avgRefElement);
        }

        distance /= Math.sqrt(Math.pow(a, 2) * Math.pow(b, 2));

        return distance;

    }

    private double average(Row element) {
        double sum = 0d;
        for (int i = 0; i < element.getColumnCount(); i++) {
            sum += element.getColumn(i);
        }
        return sum / element.getColumnCount();
    }
}
