package com.github.rafaeldeoliveira.knn.reader;

import com.github.rafaeldeoliveira.knn.model.Row;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class DataReader {


    public Set<Row> read(URL url, String separator, int labelIndex) {

        Set<Row> rows = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()))) {

            String line;
            while((line = reader.readLine()) != null) {

                if(line.isEmpty()) {
                    continue;
                }
                Row row = new Row();
                String[] cols = line.split(separator);

                for (int i = 0; i < cols.length; i++) {
                    if(i == labelIndex) {
                        row.setLabel(cols[i]);
                    } else {
                        row.addColumn(Double.parseDouble(cols[i]));
                    }
                }
                rows.add(row);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return rows;
    }

}
