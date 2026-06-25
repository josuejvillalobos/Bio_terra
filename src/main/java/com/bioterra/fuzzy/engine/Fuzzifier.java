package com.bioterra.fuzzy.engine;

import com.bioterra.fuzzy.model.LinguisticVariable;
import java.util.Map;

public interface Fuzzifier {

    Map<String, Map<String, Double>> fuzzify(
        Map<String, LinguisticVariable> variables,
        Map<String, Double> crsipInputs
    );
}
