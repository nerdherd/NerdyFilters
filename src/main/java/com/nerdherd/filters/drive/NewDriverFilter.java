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
 * New driver filter for swerve drive teleop input. Accepts a value in [-1, 1]
 */
public class NewDriverFilter extends FilterSeries {
    private SlewRateLimiter slewRateLimiter;
    private boolean belowDeadband;
    private final double deadbandScaler;

    /**
     * Construct a new driver filter
     * @param deadband
     * @param motorDeadband
     * @param scale
     * @param alpha
     * @param posRateLimit
     * @param negRateLimit
     */
    public NewDriverFilter(double deadband, double motorDeadband, double scale, 
            double alpha, double posRateLimit, double negRateLimit) {
        super();

        slewRateLimiter = new SlewRateLimiter(posRateLimit, negRateLimit, 0);
        deadbandScaler = (1 - motorDeadband) * (1 - motorDeadband);

        super.setFilters(
            new DeadbandFilter(deadband),
            new WrapperFilter(
                (x) -> {
                    if (x == 0) {
                        belowDeadband = true;
                        return 0.0;
                    } 
                    if (x > 0) {
                        belowDeadband = false;
                        return x - deadband;
                    } else {
                        belowDeadband = false;
                        return x + deadband;
                    }
                }
            ),
            new ExponentialSmoothingFilter(alpha),
            new PowerFilter(3),
            new WrapperFilter(
                (x) -> {
                    if (belowDeadband) return 0.0;
                    else {
                        return Math.signum(x) * ((Math.abs(x) / deadbandScaler) + motorDeadband);
                    }
                }
            ),
            new WrapperFilter(
                (x) -> {
                    return Math.signum(x) 
                        * slewRateLimiter.calculate(Math.abs(x));
                }
            ),
            new ScaleFilter(scale),
            new ClampFilter(scale)
        );
    }
}
