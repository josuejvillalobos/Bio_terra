package com.bioterra.fuzzy.engine.impl;

import com.bioterra.fuzzy.engine.Defuzzifier;
import com.bioterra.fuzzy.model.FuzzySet;
import com.bioterra.fuzzy.model.LinguisticVariable;

import java.util.Map;

public class CentroidDefuzzifier  implements Defuzzifier {

    private static final int RESOLUTION = 1000;

    @Override
    public double defuzzify(
            LinguisticVariable outputVariable,
            Map<String, Double> firingStrengths) {

        double numerator   = 0.0;
        double denominator = 0.0;

        double min  = outputVariable.getMinValue();
        double max  = outputVariable.getMaxValue();
        double step = (max - min) / RESOLUTION;

        for (int i = 0; i <= RESOLUTION; i++) {
            double x             = min + i * step;
            double aggregated    = aggregate(x, outputVariable, firingStrengths);

            numerator   += x * aggregated;
            denominator += aggregated;
        }

        if (denominator == 0.0) return 0.0;

        return numerator / denominator;
    }

    private double aggregate(
            double x,
            LinguisticVariable outputVariable,
            Map<String, Double> firingStrengths) {

        double maxDegree = 0.0;

        for (Map.Entry<String, Double> entry : firingStrengths.entrySet()) {
            String setName       = entry.getKey();
            double firingStrength = entry.getValue();

            FuzzySet set = outputVariable.getSets().get(setName);
            if (set == null) continue;

            double clipped = Math.min(firingStrength, set.membership(x));

            maxDegree = Math.max(maxDegree, clipped);
        }

        return maxDegree;
    }
}

