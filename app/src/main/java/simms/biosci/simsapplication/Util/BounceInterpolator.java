package simms.biosci.simsapplication.Util;

/**
 * Created by topyttac on 10/19/2016 AD.
 */
class BounceInterpolator implements android.view.animation.Interpolator {
    double mAmplitude = 0.2;
    double mFrequency = 20;

    BounceInterpolator(double amplitude, double frequency) {
        mAmplitude = amplitude;
        mFrequency = frequency;
    }

    public float getInterpolation(float time) {
        return (float) (-1 * Math.pow(Math.E, -time/ mAmplitude) *
                Math.cos(mFrequency * time) + 1);
    }
}
