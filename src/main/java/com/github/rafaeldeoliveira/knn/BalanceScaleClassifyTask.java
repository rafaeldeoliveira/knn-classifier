package com.github.rafaeldeoliveira.knn;

import com.github.rafaeldeoliveira.knn.distances.DistanceMethod;
import com.github.rafaeldeoliveira.knn.model.Row;
import com.github.rafaeldeoliveira.knn.reader.DataReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BalanceScaleClassifyTask extends ClassifyTask {

    public BalanceScaleClassifyTask() {
        Map<String, Integer> labels = new HashMap<>();
        labels.put("R", 1);
        labels.put("L", 2);
        labels.put("B", 3);

        setLabels(labels);
        DataReader reader = new DataReader();
        setRows(reader.read(reader.getClass().getResource("/balance-scale.data"), ",", 0));
    }

}
