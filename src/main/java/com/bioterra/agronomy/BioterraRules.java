package com.bioterra.agronomy;

import com.bioterra.enums.FuzzyOperator;
import com.bioterra.fuzzy.model.FuzzyRule;

import java.util.List;

public class BioterraRules {

    public static List<FuzzyRule> all() {
        return List.of(

            new FuzzyRule("rule-dry-hot-no-rain", FuzzyOperator.AND,
                new FuzzyRule.Consequent(BioterraVariables.IRRIGATION_LEVEL,
                    BioterraVariables.IRRIGATION_HIGH))
                .when(BioterraVariables.SOIL_MOISTURE,   BioterraVariables.DRY)
                .when(BioterraVariables.AIR_TEMPERATURE, BioterraVariables.HIGH)
                .when(BioterraVariables.PRECIPITATION,   BioterraVariables.NONE),

            new FuzzyRule("rule-dry-warm-no-rain", FuzzyOperator.AND,
                new FuzzyRule.Consequent(BioterraVariables.IRRIGATION_LEVEL,
                    BioterraVariables.IRRIGATION_MEDIUM))
                .when(BioterraVariables.SOIL_MOISTURE,   BioterraVariables.DRY)
                .when(BioterraVariables.AIR_TEMPERATURE, BioterraVariables.WARM)
                .when(BioterraVariables.PRECIPITATION,   BioterraVariables.NONE),

            new FuzzyRule("rule-dry-light-rain", FuzzyOperator.AND,
                new FuzzyRule.Consequent(BioterraVariables.IRRIGATION_LEVEL,
                    BioterraVariables.IRRIGATION_LOW))
                .when(BioterraVariables.SOIL_MOISTURE,  BioterraVariables.DRY)
                .when(BioterraVariables.PRECIPITATION,  BioterraVariables.LIGHT),

            // IF SoilMoisture IS Optimal AND Precipitation IS Heavy
            // THEN IrrigationLevel IS None
            new FuzzyRule("rule-optimal-heavy-rain", FuzzyOperator.AND,
                new FuzzyRule.Consequent(BioterraVariables.IRRIGATION_LEVEL,
                    BioterraVariables.IRRIGATION_NONE))
                .when(BioterraVariables.SOIL_MOISTURE,  BioterraVariables.OPTIMAL)
                .when(BioterraVariables.PRECIPITATION,  BioterraVariables.HEAVY),


            new FuzzyRule("rule-optimal-no-rain", FuzzyOperator.AND,
                new FuzzyRule.Consequent(BioterraVariables.IRRIGATION_LEVEL,
                    BioterraVariables.IRRIGATION_LOW))
                .when(BioterraVariables.SOIL_MOISTURE,  BioterraVariables.OPTIMAL)
                .when(BioterraVariables.PRECIPITATION,  BioterraVariables.NONE),

            new FuzzyRule("rule-saturated", FuzzyOperator.AND,
                new FuzzyRule.Consequent(BioterraVariables.IRRIGATION_LEVEL,
                    BioterraVariables.IRRIGATION_NONE))
                .when(BioterraVariables.SOIL_MOISTURE, BioterraVariables.SATURATED)
        );
    }
}