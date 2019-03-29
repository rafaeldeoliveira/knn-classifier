package com.github.rafaeldeoliveira.knn;

import com.github.rafaeldeoliveira.knn.distances.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {


    public static void main(String[] args) throws InterruptedException, ExecutionException {

        long start = System.currentTimeMillis();
        int numThreads = 3;
        int repetitions = 1;
        ExecutorService pool = Executors.newFixedThreadPool(numThreads);

        DistanceMethod distance = new EuclidianDistance();

        Collection<Callable<Double>> tasks = new ArrayList<>();
        for (int i = 0; i < repetitions; i++) {
            tasks.add(new WineQualityClassifyTask(100, 100, i, distance));
        }

        var futures = pool.invokeAll(tasks);

        double media = 0d;
        for (var future : futures) {
            media += future.get();
        }

        media = media / (double) futures.size();

        DecimalFormat format = new DecimalFormat(".##");

        System.out.println();
        System.out.println("Success rate is " + format.format(100 - media) + "%");
        System.out.println();
        System.out.println("Exit after " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");
        System.exit(0);

    }




}
