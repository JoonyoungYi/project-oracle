package com.echo.holographlibrary;

import android.animation.Animator;
import android.view.animation.Interpolator;

/**
 * Created by DouglasW on 6/8/2014.
 */
public interface HoloGraphAnimate {

    int getDuration();
    void setDuration(int duration);

    Interpolator getInterpolator();
    void setInterpolator(Interpolator interpolator);

    void animateToGoalValues();
    void setAnimationListener(Animator.AnimatorListener animationListener);
}
