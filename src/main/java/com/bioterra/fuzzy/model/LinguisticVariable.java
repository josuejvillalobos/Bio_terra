package com.bioterra.fuzzy.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public class LinguisticVariable {

    private final String name;
    private final double minValue;
    private final double maxValue;
    private final Map<String, FuzzySet> sets;

    public LinguisticVariable(String name, double minValue, double maxValue) {

        this.name     = name;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.sets     = new LinkedHashMap<>();
    }

    public LinguisticVariable addSet (FuzzySet set) {
        sets.put (set.getName(), set);
        return this;
    }

    public double membership(double crispValue, String setName){
        FuzzySet set = sets.get(setName);
        if (set == null){
            throw new IllegalArgumentException(
                "FuzzySet '" + setName + "' not found in variable '" + name + "'"
            );
        }
        return set.membership(crispValue);
    }

    public Map<String, Double> fuzzify(double crispValue) {
        Map<String, Double> result = new LinkedHashMap<>();
        sets.forEach((name, set) -> result.put(name, set.membership(crispValue)));
        return Collections.unmodifiableMap(result);
    }

    public String getName()              { return name; }
    public double getMinValue()          { return minValue; }
    public double getMaxValue()          { return maxValue; }
    public Map<String, FuzzySet> getSets() { return Collections.unmodifiableMap(sets); }

    
}
