package com.github.rafaeldeoliveira.knn;

import com.github.rafaeldeoliveira.knn.classifier.KnnClassifier;
import com.github.rafaeldeoliveira.knn.distances.Distance;
import com.github.rafaeldeoliveira.knn.model.Row;
import com.github.rafaeldeoliveira.knn.parser.Grouper;

import java.util.*;
import java.util.concurrent.Callable;

public class ClassifyTask implements Callable<Double> {


    private Distance distance;
    private int maxInteractions;
    private int kMax;
    private Set<Row> rows;
    private int id;


    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    private Map<String, Integer> labels;

    public void setMaxInteractions(int maxInteractions) {
        this.maxInteractions = maxInteractions;
    }

    public void setkMax(int kMax) {
        this.kMax = kMax;
    }

    public void setRows(Set<Row> rows) {
        this.rows = rows;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLabels(Map<String, Integer> labels) {
        this.labels = labels;
    }

    public ClassifyTask() {

    }

    public ClassifyTask(Set<Row> rows, Map<String, Integer> labels, int maxInteractions, int kMax, int id, Distance distance) {
        this.rows = rows;
        this.kMax = kMax;
        this.maxInteractions = maxInteractions;
        this.id = id;
        this.labels = labels;
        this.distance = distance;
    }


    public Double call() {

        Grouper grouper = new Grouper();

        ArrayList<Set<Row>> groups = grouper.group(rows, 0.25f, 0.25f, 0.5f);



        Comparator<String> comparator = Comparator.comparing(a -> labels.getOrDefault(a, 1));

        Set<Row> z1 = new HashSet<>(groups.get(0));
        Set<Row> z2 = new HashSet<>(groups.get(1));
        Set<Row> z3 = new HashSet<>(groups.get(2));

        KnnClassifier classifier = new KnnClassifier(z1, distance);

        int bestK = -1;
        int lastFailures = z2.size();

        for (int k = 3; k <= kMax; k += 2) {

            int failures = 0;
            for (Row row : z2) {
                String label = classifier.classify(row, k, comparator);
                if (!label.equals(row.getLabel())) {
                    failures++;
                }
            }

            if (failures <= lastFailures) {
                lastFailures = failures;
                bestK = k;
            }
        }

        System.out.println("#" + id + " Best k is " + bestK);

        Set<Row> bestZ1 = null;
        Integer z1Failures = null;

        for (int i = 0; i < maxInteractions; i++) {
            Set<Row> newZ1 = new HashSet<>(z1);
            classifier = new KnnClassifier(newZ1, distance);
            int failures = 0;

            for (Row row : new HashSet<>(z2)) {
                String label = classifier.classify(row, bestK, comparator);
                if (!label.equals(row.getLabel())) {
                    // encontra um elemento do mesmo tipo de errado e substitui em z2
                    z1.stream().filter(r -> r.getLabel().equals(label))
                            .findFirst()
                            .ifPresent(r -> {
                                z1.remove(r);
                                z1.add(row);
                                z2.remove(row);
                                z2.add(r);
                            });
                    failures++;
                }
            }
            if(z1Failures == null || z1Failures > failures) {
                z1Failures = failures;
                bestZ1 = newZ1;
            }
        }

        System.out.println("#" + id + " Best z1 had " + z1Failures + " failures on z2");

        // nesse ponto o z1 deve estar bom, hora de comparar com o z3.

        classifier = new KnnClassifier(bestZ1, distance);

        int z3failures = 0;
        for (Row row : z3) {

            String label = classifier.classify(row, bestK, comparator);
            if (!label.equals(row.getLabel())) {
                z3failures++;
            }
        }

        double hit = z3failures * 100 / groups.get(2).size();

        if (z3failures > 0) {
            System.out.println("#" + (id) + " [z3] Success " + (100 - hit) + "% (" + z3failures + " failures)");
        } else {
            System.out.println("#" + (id) + " Our z1 is perfect.");
        }

        return hit;
    }
}
