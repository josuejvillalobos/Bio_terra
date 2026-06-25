package com.bioterra.fuzzy.engine.impl;

import com.bioterra.fuzzy.engine.Fuzzifier;
import com.bioterra.fuzzy.model.LinguisticVariable;

import java.util.LinkedHashMap;
import java.util.Map;

public class MamdaniFuzzifier implements Fuzzifier {

    @Override
    public Map<String, Map<String, Double>> fuzzify(
            Map<String, LinguisticVariable> variables,
            Map<String, Double> crispInputs) {

        Map<String, Map<String, Double>> result = new LinkedHashMap<>();

        for (Map.Entry<String, Double> input : crispInputs.entrySet()) {
            String variableName = input.getKey();
            double crispValue   = input.getValue();

            LinguisticVariable variable = variables.get(variableName);

            if (variable == null) {
                throw new IllegalArgumentException(
                    "Variable '" + variableName + "' not found in variable definitions"
                );
            }

            result.put(variableName, variable.fuzzify(crispValue));
        }

        return result;
    }
}