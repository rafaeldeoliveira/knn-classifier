package com.github.rafaeldeoliveira.knn;

import com.github.rafaeldeoliveira.knn.distances.DistanceMethod;
import com.github.rafaeldeoliveira.knn.model.Row;
import com.github.rafaeldeoliveira.knn.reader.DataReader;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IrisClassifyTask extends ClassifyTask {


    public IrisClassifyTask() {
        Map<String, Integer> labels = new HashMap<>();
        labels.put("Iris-setosa", 1);
        labels.put("Iris-versicolor", 2);
        labels.put("Iris-virginica", 3);
        setLabels(labels);

        DataReader reader = new DataReader();
        setRows(reader.read(reader.getClass().getResource("/iris.data"), ",", 4));
    }

}
