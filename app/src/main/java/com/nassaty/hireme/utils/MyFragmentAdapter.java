package com.nassaty.hireme.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.nassaty.hireme.fragments.myApplications;
import com.nassaty.hireme.fragments.myJobs;

public class MyFragmentAdapter extends FragmentStatePagerAdapter{

    private static final int TAB_1 = 0, TAB_2 = 1;

    private static final int NM_TABS = 2;

    public MyFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case TAB_1:
                myJobs tab1 = new myJobs();
                return tab1;

            case TAB_2:
                myApplications tab2 = new myApplications();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NM_TABS;
    }
}
