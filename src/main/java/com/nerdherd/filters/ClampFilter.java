package com.nerdherd.filters;

import edu.wpi.first.math.MathUtil;

/** Clamps an input between two values. */
public class ClampFilter implements Filter {
    private double max;
    private double min;

    public ClampFilter(double max, double min) {
        this.max = max;
        this.min = min;
    }

    public ClampFilter(double bound) {
        this.max = bound;
        this.min = -bound;
    }

    public double calculate(double input) {
        return MathUtil.clamp(input, min, max);
    }
    

    public double getMax() {
        return max;
    }
    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }
    public void setMin(double min) {
        this.min = min;
    }

    
}
