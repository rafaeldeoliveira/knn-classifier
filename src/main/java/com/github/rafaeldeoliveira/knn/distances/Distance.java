package com.github.rafaeldeoliveira.knn.distances;

import com.github.rafaeldeoliveira.knn.model.Row;

public interface Distance {
    double calculate(Row element, Row refElement);
}
