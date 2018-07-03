
package com.nassaty.hireme.activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.loicteillard.easytabs.EasyTabs;
import com.nassaty.hireme.R;
import com.nassaty.hireme.utils.MyFragmentAdapter;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        EasyTabs easyTabs = findViewById(R.id.easytabs);
        ViewPager viewpager = findViewById(R.id.viewpager);
        MyFragmentAdapter pagerAdapter = new MyFragmentAdapter(getSupportFragmentManager());
        viewpager.setAdapter(pagerAdapter);

        easyTabs.setViewPager(viewpager, 0); // Set viewPager to EasyTabs with default index


//        easyTabs.setPagerListener(new EasyTabs.PagerListener() { // Optional, add a listener
//            @Override
//            public void onTabSelected(int position) {
//                Toast.makeText(Profile.this, "tab selected:"+position, Toast.LENGTH_SHORT).show();
//            }
//        });

    }
}
