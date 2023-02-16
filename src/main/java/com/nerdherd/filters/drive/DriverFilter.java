package com.nerdherd.filters.drive;

import com.nerdherd.filters.ClampFilter;
import com.nerdherd.filters.DeadbandFilter;
import com.nerdherd.filters.ExponentialSmoothingFilter;
import com.nerdherd.filters.FilterSeries;
import com.nerdherd.filters.PowerFilter;
import com.nerdherd.filters.ScaleFilter;
import com.nerdherd.filters.WrapperFilter;

import edu.wpi.first.math.filter.SlewRateLimiter;

/**
 * The original driver filter used to filter swerve drive teleop input.
 * Accepts a value in [-1, 1]
 */
public class DriverFilter extends FilterSeries {
    private SlewRateLimiter slewRateLimiter;

    public DriverFilter(double deadband, double alpha, 
            double oneMinusAlpha, double maxSpeed, 
            double posRateLimit, int power, 
            double negRateLimit) {
        super();

        slewRateLimiter = new SlewRateLimiter(posRateLimit, negRateLimit, 0);
        
        super.setFilters(
            new DeadbandFilter(deadband),
            new ExponentialSmoothingFilter(alpha, oneMinusAlpha),
            new PowerFilter(power),
            new WrapperFilter(
                (x) -> {
                    return Math.signum(x) 
                        * slewRateLimiter.calculate(Math.abs(x));
                }
            ),
            new ScaleFilter(maxSpeed),
            new ClampFilter(maxSpeed, -maxSpeed)
        );
    }

    public DriverFilter(double deadband, double alpha, 
            double oneMinusAlpha, double maxSpeed, double rateLimit, int power) {
        this(deadband, alpha, oneMinusAlpha, maxSpeed, rateLimit, power, -rateLimit);
    }

    public DriverFilter(double deadband, double alpha, 
        double oneMinusAlpha, double maxSpeed, double rateLimit) {
        this(deadband, alpha, oneMinusAlpha, maxSpeed, rateLimit, 3, -rateLimit);
    }
}
