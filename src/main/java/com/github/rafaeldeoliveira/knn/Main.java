package com.github.rafaeldeoliveira.knn;

import com.github.rafaeldeoliveira.knn.distances.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {


    public static void main(String[] args) throws InterruptedException, ExecutionException {


        int numThreads = 3;
        int repetitions = 20;
        ExecutorService pool = Executors.newFixedThreadPool(numThreads);

        Distance distance = new CamberraDistance();

        Collection<Callable<Double>> tasks = new ArrayList<>();
        for (int i = 0; i < repetitions; i++) {
            tasks.add(new WineQualityClassifyTask(100, 13, i, distance));
        }

        var futures = pool.invokeAll(tasks);

        double media = 0d;
        for (var future : futures) {
            media += future.get();
        }

        media = media / futures.size();

        System.out.println("Success rate = " + (100 - media) + "%");

        System.exit(0);

    }


}
