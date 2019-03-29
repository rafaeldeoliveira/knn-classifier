package com.github.rafaeldeoliveira.knn;

import com.github.rafaeldeoliveira.knn.distances.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {

    private enum Mode {
        DYN, FIXED
    }

    private static class Result {
        double dp;
        double rate;
    }
    public static void main(String[] args) throws Exception {

        long start = System.currentTimeMillis();
        Mode mode = Mode.DYN;
        int numThreads = 4, repetitions = 30, maxInteractions = 100, kMax = 13;

        Class<? extends ClassifyTask> taskClass = WineQualityClassifyTask.class;


        switch (mode) {
            case DYN:
                dynMethod(numThreads, repetitions, maxInteractions, kMax, taskClass);
                break;
            case FIXED:
                fixedMethod(numThreads, repetitions, maxInteractions, kMax, taskClass);
                break;
        }

        System.out.println("\nExit after " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");
        System.exit(0);
    }

    private static void fixedMethod(int numThreads, int repetitions, int maxInteractions, int kMax, Class<? extends ClassifyTask> classifyTaskClass) throws Exception {
        Result result = run(numThreads, repetitions, maxInteractions, kMax, new EuclideanDistance(), classifyTaskClass);
        System.out.println("\nSuccess rate is " + result.rate + "%, dp is " + result.dp);
    }

    private static Result run(int numThreads, int repetitions, int maxInteractions, int kMax, DistanceMethod distanceMethod, Class<? extends ClassifyTask> classifyTaskClass) throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(numThreads);
        Collection<Callable<Double>> tasks = new ArrayList<>();
        for (int i = 0; i < repetitions; i++) {
            ClassifyTask task = classifyTaskClass.getConstructor().newInstance();
            task.setMaxInteractions(maxInteractions);
            task.setkMax(kMax);
            task.setDistanceMethod(distanceMethod);
            task.setId(i);
            tasks.add(task);
        }
        var futures = pool.invokeAll(tasks);

        double rate = 0d;
        for (var future : futures) {
            rate += future.get();
        }

        rate = rate / (double) futures.size();

        double dp = 0d;

        for (var future : futures) {
            dp += Math.pow(future.get() - rate, 2);
        }

        dp = Math.sqrt(dp / (double) futures.size());

        Result result = new Result();
        result.dp = dp;
        result.rate = rate;
        return result;
    }

    private static void dynMethod(int numThreads, int repetitions, int maxInteractions, int kMax, Class<? extends ClassifyTask> classifyTaskClass) throws Exception {

        DistanceMethod[] distanceMethods = {
                new CanberraDistance(),
                new ChebychevDistance(),
                new CorrelationalDistance(),
                new EuclideanDistance(),
                new ManhattanDistance(),
                new MinkowskiDistance()
        };

        String bestDistanceMethod = null;
        double bestRate = -1d;

        for (DistanceMethod distanceMethod : distanceMethods) {

            Result result = run(numThreads, repetitions, maxInteractions, kMax, distanceMethod, classifyTaskClass);
            System.out.println("\n[" + distanceMethod.getClass().getSimpleName() + "] Success rate is " + result.rate + "%, dp is " + result.dp + "\n");
            if (bestRate <= result.rate) {
                bestRate = result.rate;
                bestDistanceMethod = distanceMethod.getClass().getSimpleName();
            }

        }
        System.out.println("\nBest distance method is " + bestDistanceMethod + " with success rate of " + bestRate + "%");

    }


}
