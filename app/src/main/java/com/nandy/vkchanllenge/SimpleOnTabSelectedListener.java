package com.nandy.vkchanllenge;

import android.support.design.widget.TabLayout;

/**
 * Created by yana on 14.09.17.
 */

public abstract class SimpleOnTabSelectedListener implements TabLayout.OnTabSelectedListener {

    public abstract void onTabItemSelected(TabLayout.Tab tab);

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        onTabItemSelected(tab);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }
}
