package com.bioterra.fuzzy.model;

public class FuzzySet {

    private final String name;

    private final double a;
    private final double b;
    private final double c;
    private final double d;

    public FuzzySet(String name, double a, double b, double c){
        this(name, a, b, b, c);
    }

    public FuzzySet(String name, double a, double b, double c, double d) {
        this.name = name;
        this.a    = a;
        this.b    = b;
        this.c    = c;
        this.d    = d;
    }

    public double membership(double x) {
        if (x <= a || x >= d) return 0.0;
        if (x >= b && x <= c) return 1.0;   

        if (x < b) return (x - a) / (b - a);
        return (d - x) / (d - c); 
    }

    public String  getName() { return name; }
    public double getA()     { return a; }
    public double getB()     { return b; }
    public double getC()     { return c; }
    public double getD()     { return d; }
}

    
