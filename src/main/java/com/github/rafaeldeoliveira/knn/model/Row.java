package com.github.rafaeldeoliveira.knn.model;

import java.util.ArrayList;
import java.util.List;

public class Row {
    private List<Double> columns;
    private String label;

    public Row() {
        this.columns = new ArrayList<>();
    }

    public void addColumn(Double data) {
        this.columns.add(data);
    }

    public double getColumn(int i) {
        return this.columns.get(i);
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getColumnCount() {
        return this.columns.size();
    }
}
