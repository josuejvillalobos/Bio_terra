package com.bioterra.fuzzy.model;

import com.bioterra.enums.FuzzyOperator;
import java.util.LinkedHashMap;
import java.util.Map;

public class FuzzyRule {

    public record Condition(String variableName, String setName) {}
    public record Consequent(String variableName, String setName) {}

    private final String name;
    private final FuzzyOperator operator;
    private final Map<String, Condition> antecedents;
    private final Consequent consequent;

    public FuzzyRule(String name, FuzzyOperator operator, Consequent consequent) {
        this.name        = name;
        this.operator    = operator;
        this.consequent  = consequent;
        this.antecedents = new LinkedHashMap<>();
    }

    public FuzzyRule when(String variableName, String setName) {
        antecedents.put(variableName, new Condition(variableName, setName));
        return this;
    }

    public double evaluate(Map<String, Map<String, Double>> membershipDegrees) {
        if (antecedents.isEmpty()) return 0.0;

        double strength = operator == FuzzyOperator.AND ? 1.0 : 0.0;

        for (Condition condition : antecedents.values()) {
            Map<String, Double> degrees = membershipDegrees.get(condition.variableName());

            if (degrees == null) {
                throw new IllegalArgumentException(
                    "Variable '" + condition.variableName() + "' not found in input"
                );
            }

            double degree = degrees.getOrDefault(condition.setName(), 0.0);

            strength = operator == FuzzyOperator.AND
                ? Math.min(strength, degree)
                : Math.max(strength, degree);
        }

        return strength;
    }

    public String getName()                        { return name; }
    public FuzzyOperator getOperator()             { return operator; }
    public Consequent getConsequent()              { return consequent; }
    public Map<String, Condition> getAntecedents() { return antecedents; }
}