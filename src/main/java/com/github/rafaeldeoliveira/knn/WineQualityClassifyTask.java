package com.github.rafaeldeoliveira.knn;

import com.github.rafaeldeoliveira.knn.distances.DistanceMethod;
import com.github.rafaeldeoliveira.knn.model.Row;
import com.github.rafaeldeoliveira.knn.reader.DataReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class WineQualityClassifyTask extends ClassifyTask {

    public WineQualityClassifyTask() {
        Map<String, Integer> labels = new HashMap<>();
        labels.put("1", 1);
        labels.put("2", 2);
        labels.put("3", 3);
        labels.put("4", 4);
        labels.put("5", 5);
        labels.put("6", 6);
        labels.put("7", 7);
        setLabels(labels);

        DataReader reader = new DataReader();
        setRows(reader.read(reader.getClass().getResource("/winequality-red.csv"), ",", 11));
    }

}
