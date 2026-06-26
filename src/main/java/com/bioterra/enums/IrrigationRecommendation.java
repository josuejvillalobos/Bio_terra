package com.bioterra.enums;
public enum IrrigationRecommendation {
    NONE,
    LOW,
    MEDIUM,
    HIGH;
    public static IrrigationRecommendation fromCrispValue(double value) {
        if (value < 15.0)  return NONE;
        if (value < 40.0)  return LOW;
        if (value < 65.0)  return MEDIUM;
        return HIGH;
    }
}