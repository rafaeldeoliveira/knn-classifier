package com.github.rafaeldeoliveira.knn.tests;


import com.github.rafaeldeoliveira.knn.reader.DataReader;
import com.github.rafaeldeoliveira.knn.model.Row;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

public class DataReaderTest {

    @Test
    public void read() throws MalformedURLException {
        DataReader reader = new DataReader();
        Set<Row> rows = reader.read(new URL("http://archive.ics.uci.edu/ml/machine-learning-databases/iris/iris.data"), ",", 4);

        Assertions.assertFalse(rows.isEmpty());

    }
}
