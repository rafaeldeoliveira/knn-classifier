package com.github.rafaeldeoliveira.knn;

import com.github.rafaeldeoliveira.knn.classifier.KnnClassifier;
import com.github.rafaeldeoliveira.knn.distances.DistanceMethod;
import com.github.rafaeldeoliveira.knn.grouper.Grouper;
import com.github.rafaeldeoliveira.knn.model.Row;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class ClassifyTask implements Callable<Double> {


    private DistanceMethod distanceMethod;
    private int maxInteractions;
    private int kMax;
    private Set<Row> rows;
    private int id;


    public void setDistanceMethod(DistanceMethod distanceMethod) {
        this.distanceMethod = distanceMethod;
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

    public ClassifyTask(Set<Row> rows, Map<String, Integer> labels, int maxInteractions, int kMax, int id, DistanceMethod distanceMethod) {
        this.rows = rows;
        this.kMax = kMax;
        this.maxInteractions = maxInteractions;
        this.id = id;
        this.labels = labels;
        this.distanceMethod = distanceMethod;
    }


    public Double call() {

        Grouper grouper = new Grouper();

        ArrayList<Set<Row>> groups = grouper.group(rows, 0.25f, 0.25f, 0.5f);



        Comparator<String> comparator = Comparator.comparing(a -> labels.getOrDefault(a, 1));

        Set<Row> z1 = new HashSet<>(groups.get(0));
        Set<Row> z2 = new HashSet<>(groups.get(1));
        Set<Row> z3 = new HashSet<>(groups.get(2));

        KnnClassifier classifier = new KnnClassifier(z1, distanceMethod);

        int bestK = -1;
        int lastFailures = z2.size();

        for (int k = 1; k <= kMax; k++) {

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

        log("Best k is " + bestK);

        Set<Row> bestZ1 = null;
        Integer z1Failures = null;

        for (int i = 0; i < maxInteractions; i++) {
            Set<Row> newZ1 = new HashSet<>(z1);
            classifier = new KnnClassifier(newZ1, distanceMethod);
            int failures = 0;

            for (Row row : new HashSet<>(z2)) {
                String label = classifier.classify(row, bestK, comparator);
                if (!label.equals(row.getLabel())) {
                    // encontra um elemento do mesmo tipo de errado e substitui em z2
                    List<Row> sameLabelElements = z1.stream().filter(r -> r.getLabel().equals(label)).collect(Collectors.toList());
                    if(!sameLabelElements.isEmpty()) {
                        int element = ThreadLocalRandom.current().nextInt(0, sameLabelElements.size());
                        Row elementToReplace = sameLabelElements.get(element);

                        z1.remove(elementToReplace);
                        z1.add(row);
                        z2.remove(row);
                        z2.add(elementToReplace);

                    }
                    failures++;
                }
            }
            if(z1Failures == null || z1Failures > failures) {
                z1Failures = failures;
                bestZ1 = newZ1;
            }
        }

        log("Best z1 had " + z1Failures + " failures on z2");

        // nesse ponto o z1 deve estar bom, hora de comparar com o z3.

        classifier = new KnnClassifier(bestZ1, distanceMethod);

        int z3failures = 0;
        for (Row row : z3) {

            String label = classifier.classify(row, bestK, comparator);
            if (!label.equals(row.getLabel())) {
                z3failures++;
            }
        }

        double hit = z3failures * 100 / groups.get(2).size();

        if (z3failures > 0) {
            success("[z3] Success " + (100 - hit) + "% (" + z3failures + " failures)");
        } else {
            success("Our z1 is perfect.");
        }

        return hit;
    }

    private void log(String message, String color) {
        if (color != null) {
            System.out.print(color);
        }
        System.out.println("[Thread-" + id + "] " + message);
        if (color != null) {
            System.out.print(ANSI_RESET);
        }
    }
    private void log(String message) {
        log(message, null);
    }
    private void success(String message) {
        log(message, ANSI_GREEN);
    }

    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_RESET = "\u001B[0m";
}
