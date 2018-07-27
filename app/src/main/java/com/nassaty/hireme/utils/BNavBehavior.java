package com.nassaty.hireme.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BNavBehavior extends CoordinatorLayout.Behavior<BottomNavigationViewEx> {

    public BNavBehavior() {
        super();
    }

    public BNavBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, BottomNavigationViewEx child, View dependency) {
        boolean dependsOn = dependency instanceof FrameLayout;
        return dependsOn;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationViewEx child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        if (dy < 0)
            showBNav(child);
        else {
            hideBNav(child);
        }
    }

    private void hideBNav(BottomNavigationViewEx child) {
        child.animate().translationY(child.getHeight());
    }

    private void showBNav(BottomNavigationViewEx child) {
        child.animate().translationY(0);
    }
}
