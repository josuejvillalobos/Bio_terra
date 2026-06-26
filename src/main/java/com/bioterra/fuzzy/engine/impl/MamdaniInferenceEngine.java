package com.bioterra.fuzzy.engine.impl;

import com.bioterra.fuzzy.engine.InferenceEngine;
import com.bioterra.fuzzy.model.FuzzyRule;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
public class MamdaniInferenceEngine implements InferenceEngine {

    @Override
    public Map<String, Double> infer(
            List<FuzzyRule> rules,
            Map<String, Map<String, Double>> membershipDegrees) {

        Map<String, Double> result = new LinkedHashMap<>();

        for (FuzzyRule rule : rules) {
            double firingStrength = rule.evaluate(membershipDegrees);

            if (firingStrength <= 0.0) continue;

            String consequentSet = rule.getConsequent().setName();

            // Aggregation: keep the maximum firing strength per output set
            result.merge(consequentSet, firingStrength, Math::max);
        }

        return result;
    }
}

