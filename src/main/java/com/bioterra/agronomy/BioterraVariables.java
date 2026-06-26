package com.bioterra.agronomy;

import com.bioterra.fuzzy.model.FuzzySet;
import com.bioterra.fuzzy.model.LinguisticVariable;

import java.util.Map;

public class BioterraVariables {

    public static final String SOIL_MOISTURE    = "SoilMoisture";
    public static final String AIR_TEMPERATURE  = "AirTemperature";
    public static final String PRECIPITATION    = "Precipitation";

    public static final String IRRIGATION_LEVEL = "IrrigationLevel";

    public static final String DRY       = "Dry";
    public static final String OPTIMAL   = "Optimal";
    public static final String SATURATED = "Saturated";

    public static final String LOW  = "Low";
    public static final String WARM = "Warm";
    public static final String HIGH = "High";

    public static final String NONE   = "None";
    public static final String LIGHT  = "Light";
    public static final String HEAVY  = "Heavy";

    public static final String IRRIGATION_NONE   = "None";
    public static final String IRRIGATION_LOW    = "Low";
    public static final String IRRIGATION_MEDIUM = "Medium";
    public static final String IRRIGATION_HIGH   = "High";


    public static LinguisticVariable soilMoisture() {
        return new LinguisticVariable(SOIL_MOISTURE, 0, 100)
            .addSet(new FuzzySet(DRY,       0,   0,  30,  50))
            .addSet(new FuzzySet(OPTIMAL,   30,  50,  70))
            .addSet(new FuzzySet(SATURATED, 60,  80, 100, 100));
    }

    public static LinguisticVariable airTemperature() {
        return new LinguisticVariable(AIR_TEMPERATURE, 0, 50)
            .addSet(new FuzzySet(LOW,   0,   0,  10,  20))
            .addSet(new FuzzySet(WARM,  10,  20,  30))
            .addSet(new FuzzySet(HIGH,  25,  35,  50,  50));
    }

    public static LinguisticVariable precipitation() {
        return new LinguisticVariable(PRECIPITATION, 0, 50)
            .addSet(new FuzzySet(NONE,  0,   0,   0,   5))
            .addSet(new FuzzySet(LIGHT, 2,  10,  20))
            .addSet(new FuzzySet(HEAVY, 15,  25,  50,  50));
    }

    public static LinguisticVariable irrigationLevel() {
        return new LinguisticVariable(IRRIGATION_LEVEL, 0, 100)
            .addSet(new FuzzySet(IRRIGATION_NONE,   0,   0,   0,  10))
            .addSet(new FuzzySet(IRRIGATION_LOW,    5,  20,  35))
            .addSet(new FuzzySet(IRRIGATION_MEDIUM, 30,  50,  70))
            .addSet(new FuzzySet(IRRIGATION_HIGH,   60,  80, 100, 100));
    }

    public static Map<String, LinguisticVariable> inputVariables() {
        return Map.of(
            SOIL_MOISTURE,   soilMoisture(),
            AIR_TEMPERATURE, airTemperature(),
            PRECIPITATION,   precipitation()
        );
    }
    
}
