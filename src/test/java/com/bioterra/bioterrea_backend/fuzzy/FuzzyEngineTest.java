package com.bioterra.bioterrea_backend.fuzzy;

import com.bioterra.enums.FuzzyOperator;
import com.bioterra.fuzzy.engine.impl.CentroidDefuzzifier;
import com.bioterra.fuzzy.engine.impl.MamdaniFuzzifier;
import com.bioterra.fuzzy.engine.impl.MamdaniInferenceEngine;
import com.bioterra.fuzzy.model.FuzzyRule;
import com.bioterra.fuzzy.model.FuzzySet;
import com.bioterra.fuzzy.model.LinguisticVariable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Fuzzy Engine - Mamdani")
class FuzzyEngineTest {

    private LinguisticVariable soilMoisture;
    private LinguisticVariable airTemperature;
    private LinguisticVariable precipitation;
    private LinguisticVariable irrigationLevel;

    private MamdaniFuzzifier      fuzzifier;
    private MamdaniInferenceEngine inferenceEngine;
    private CentroidDefuzzifier    defuzzifier;

    private List<FuzzyRule> rules;

    @BeforeEach
    void setup() {
        // ── Input variables ───────────────────────────────
        soilMoisture = new LinguisticVariable("SoilMoisture", 0, 100)
            .addSet(new FuzzySet("Dry",      0,   0,  30,  50))
            .addSet(new FuzzySet("Optimal",  30,  50,  70))
            .addSet(new FuzzySet("Saturated",60,  80, 100, 100));

        airTemperature = new LinguisticVariable("AirTemperature", 0, 50)
            .addSet(new FuzzySet("Low",    0,   0,  10,  20))
            .addSet(new FuzzySet("Warm",  10,  20,  30))
            .addSet(new FuzzySet("High",  25,  35,  50,  50));

        precipitation = new LinguisticVariable("Precipitation", 0, 50)
            .addSet(new FuzzySet("None",   0,   0,   0,   5))
            .addSet(new FuzzySet("Light",  2,  10,  20))
            .addSet(new FuzzySet("Heavy", 15,  25,  50,  50));

        // ── Output variable ───────────────────────────────
        irrigationLevel = new LinguisticVariable("IrrigationLevel", 0, 100)
            .addSet(new FuzzySet("None",   0,   0,   0,  10))
            .addSet(new FuzzySet("Low",    5,  20,  35))
            .addSet(new FuzzySet("Medium", 30,  50,  70))
            .addSet(new FuzzySet("High",  60,  80, 100, 100));

        // ── Rules ─────────────────────────────────────────
        rules = List.of(
            new FuzzyRule("rule-1", FuzzyOperator.AND,
                new FuzzyRule.Consequent("IrrigationLevel", "High"))
                .when("SoilMoisture",   "Dry")
                .when("AirTemperature", "High")
                .when("Precipitation",  "None"),

            new FuzzyRule("rule-2", FuzzyOperator.AND,
                new FuzzyRule.Consequent("IrrigationLevel", "None"))
                .when("SoilMoisture",  "Optimal")
                .when("Precipitation", "Heavy"),

            new FuzzyRule("rule-3", FuzzyOperator.AND,
                new FuzzyRule.Consequent("IrrigationLevel", "None"))
                .when("SoilMoisture", "Saturated")
        );

        // ── Engine ────────────────────────────────────────
        fuzzifier       = new MamdaniFuzzifier();
        inferenceEngine = new MamdaniInferenceEngine();
        defuzzifier     = new CentroidDefuzzifier();
    }

    // ── FuzzySet ──────────────────────────────────────────

    @Test
    @DisplayName("FuzzySet: triangular membership at peak returns 1.0")
    void triangularMembership_atPeak_returnsOne() {
        FuzzySet optimal = new FuzzySet("Optimal", 30, 50, 70);
        assertEquals(1.0, optimal.membership(50), 0.001);
    }

    @Test
    @DisplayName("FuzzySet: triangular membership outside range returns 0.0")
    void triangularMembership_outsideRange_returnsZero() {
        FuzzySet optimal = new FuzzySet("Optimal", 30, 50, 70);
        assertEquals(0.0, optimal.membership(10),  0.001);
        assertEquals(0.0, optimal.membership(90),  0.001);
    }

    @Test
    @DisplayName("FuzzySet: trapezoidal membership in flat top returns 1.0")
    void trapezoidalMembership_inFlatTop_returnsOne() {
        FuzzySet high = new FuzzySet("High", 60, 80, 100, 100);
        assertEquals(1.0, high.membership(85), 0.001);
        assertEquals(1.0, high.membership(95), 0.001);
    }

    // ── Fuzzifier ─────────────────────────────────────────

    @Test
    @DisplayName("Fuzzifier: dry soil at 28% has high Dry degree")
    void fuzzifier_drySoil_hasHighDryDegree() {
        Map<String, LinguisticVariable> variables = Map.of("SoilMoisture", soilMoisture);
        Map<String, Double> inputs = Map.of("SoilMoisture", 28.0);

        var result = fuzzifier.fuzzify(variables, inputs);

        assertTrue(result.get("SoilMoisture").get("Dry") > 0.5);
    }

    @Test
    @DisplayName("Fuzzifier: unknown variable throws IllegalArgumentException")
    void fuzzifier_unknownVariable_throwsException() {
        Map<String, LinguisticVariable> variables = Map.of("SoilMoisture", soilMoisture);
        Map<String, Double> inputs = Map.of("UnknownVar", 50.0);

        assertThrows(IllegalArgumentException.class,
            () -> fuzzifier.fuzzify(variables, inputs));
    }

    // ── Full pipeline ─────────────────────────────────────

    @Test
    @DisplayName("Pipeline: dry + hot + no rain recommends high irrigation")
    void pipeline_dryHotNoRain_recommendsHighIrrigation() {
        Map<String, LinguisticVariable> variables = Map.of(
            "SoilMoisture",   soilMoisture,
            "AirTemperature", airTemperature,
            "Precipitation",  precipitation
        );
        Map<String, Double> inputs = Map.of(
            "SoilMoisture",   28.0,
            "AirTemperature", 38.0,
            "Precipitation",   1.0
        );

        var membership      = fuzzifier.fuzzify(variables, inputs);
        var firingStrengths = inferenceEngine.infer(rules, membership);
        var crisp           = defuzzifier.defuzzify(irrigationLevel, firingStrengths);

        assertTrue(crisp > 50.0,
            "Expected high irrigation (>50%) but got: " + crisp);
    }

    @Test
    @DisplayName("Pipeline: saturated soil recommends no irrigation")
    void pipeline_saturatedSoil_recommendsNoIrrigation() {
        Map<String, LinguisticVariable> variables = Map.of(
            "SoilMoisture",   soilMoisture,
            "AirTemperature", airTemperature,
            "Precipitation",  precipitation
        );
        Map<String, Double> inputs = Map.of(
            "SoilMoisture",   90.0,
            "AirTemperature", 20.0,
            "Precipitation",   5.0
        );

        var membership      = fuzzifier.fuzzify(variables, inputs);
        var firingStrengths = inferenceEngine.infer(rules, membership);
        var crisp           = defuzzifier.defuzzify(irrigationLevel, firingStrengths);

        assertTrue(crisp < 30.0,
            "Expected low irrigation (<30%) but got: " + crisp);
    }

    @Test
    @DisplayName("Pipeline: optimal soil with heavy rain recommends no irrigation")
    void pipeline_optimalSoilHeavyRain_recommendsNoIrrigation() {
        Map<String, LinguisticVariable> variables = Map.of(
            "SoilMoisture",   soilMoisture,
            "AirTemperature", airTemperature,
            "Precipitation",  precipitation
        );
        Map<String, Double> inputs = Map.of(
            "SoilMoisture",   50.0,
            "AirTemperature", 20.0,
            "Precipitation",  30.0
        );

        var membership      = fuzzifier.fuzzify(variables, inputs);
        var firingStrengths = inferenceEngine.infer(rules, membership);
        var crisp           = defuzzifier.defuzzify(irrigationLevel, firingStrengths);

        assertTrue(crisp < 30.0,
            "Expected low irrigation (<30%) but got: " + crisp);
    }
}