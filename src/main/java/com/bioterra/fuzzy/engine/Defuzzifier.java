package com.bioterra.fuzzy.engine;

import com.bioterra.fuzzy.model.LinguisticVariable;
import java.util.Map;

public interface Defuzzifier {

    double defuzzify(
        LinguisticVariable outputVariable,
        Map<String, Double> firingStrengths
    );
}
