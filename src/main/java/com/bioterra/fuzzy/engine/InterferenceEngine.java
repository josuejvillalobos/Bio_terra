package com.bioterra.fuzzy.engine;

import com.bioterra.fuzzy.model.FuzzyRule;
import java.util.List;
import java.util.Map;

public interface InterferenceEngine {
    
    Map<String, Double> infer(
        List<FuzzyRule> rules,
        Map<String, Map<String, Double>> membershipDegrees
    );
}
