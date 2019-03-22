package com.github.rafaeldeoliveira.knn.parser;

import com.github.rafaeldeoliveira.knn.model.Row;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Grouper {

    public ArrayList<Set<Row>> group(Set<Row> data, float... percents) {

        Map<String, List<Row>> rowsPerLabel = data.stream()
                .collect(Collectors.groupingBy(Row::getLabel));

        ArrayList<Set<Row>> groups = new ArrayList<>();
        for (int i = 0; i < percents.length; i++) {
            groups.add(new HashSet<>());
        }

        for (int i = 0; i < percents.length; i++) {
            float percent = percents[i];
            Set<String> labels = rowsPerLabel.keySet();
            int groupQuantity = Math.round(data.size() * percent);
            int quantityPerLabel = Math.round(groupQuantity / labels.size());

            for (String label : labels) {
                for (int k = 0; k < quantityPerLabel && !rowsPerLabel.get(label).isEmpty(); k++) {
                    int chosen = ThreadLocalRandom.current().nextInt(0, rowsPerLabel.get(label).size());
                    groups.get(i).add(rowsPerLabel.get(label).remove(chosen));
                }
            }
        }
        return groups;
    }



}
