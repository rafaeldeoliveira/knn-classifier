package com.github.rafaeldeoliveira.knn.classifier;

import com.github.rafaeldeoliveira.knn.distances.DistanceMethod;
import com.github.rafaeldeoliveira.knn.model.Row;

import java.util.Comparator;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class KnnClassifier {

    private final DistanceMethod distanceMethod;
    private Set<Row> data;

    public KnnClassifier(Set<Row> data, DistanceMethod distanceMethod) {
        this.data = data;
        this.distanceMethod = distanceMethod;
    }

    public String classify(Row element, int k, Comparator<String> comparator) {

        return this.data.parallelStream()
                .sorted(Comparator.comparing(ref -> distanceMethod.calculate(element, ref)))
                .limit(k)
                .map(Row::getLabel)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .min((b, a) -> {
                    int valueComparison = a.getValue().compareTo(b.getValue());
                    if (valueComparison == 0) {
                        return comparator.compare(a.getKey(), b.getKey());
                    }
                    return valueComparison;
                }).orElseThrow()
                .getKey();
    }



}
