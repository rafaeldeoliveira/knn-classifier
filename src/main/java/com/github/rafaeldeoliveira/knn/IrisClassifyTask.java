package com.github.rafaeldeoliveira.knn;

import com.github.rafaeldeoliveira.knn.distances.Distance;
import com.github.rafaeldeoliveira.knn.model.Row;
import com.github.rafaeldeoliveira.knn.reader.DataReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IrisClassifyTask extends ClassifyTask {


    public IrisClassifyTask(int maxInteractions, int kMax, int id, Distance distance) {

        setMaxInteractions(maxInteractions);
        setkMax(kMax);
        setId(id);
        setDistance(distance);

        Map<String, Integer> labels = new HashMap<>();
        labels.put("Iris-setosa", 1);
        labels.put("Iris-versicolor", 2);
        labels.put("Iris-virginica", 3);

        setLabels(labels);

        DataReader reader = new DataReader();

        Set<Row> rows = reader.read(reader.getClass().getResource("/iris.data"), ",", 4);

        setRows(rows);
    }
}
