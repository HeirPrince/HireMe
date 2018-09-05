package com.nassaty.hireme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.nassaty.hireme.R;
import com.nassaty.hireme.fragments.Discover;
import com.nassaty.hireme.fragments.Mine;
import com.nassaty.hireme.fragments.Nearby;
import com.nassaty.hireme.listeners.ProfileListener;
import com.nassaty.hireme.utils.AuthUtils;
import com.nassaty.hireme.utils.NonSwipableViewPager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    private AuthUtils authUtils;
    FloatingActionButton addNew;
    BottomNavigationViewEx bottomNav;
    NonSwipableViewPager viewPager;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authUtils = new AuthUtils(this);

        toolbar = findViewById(R .id.toolbar);
        setSupportActionBar(toolbar);

        bottomNav = findViewById(R.id.bnve);
        viewPager = findViewById(R.id.pager);
        setupBottomNavigation(bottomNav);

        addNew = findViewById(R.id.addNew);
        addNew.setOnClickListener(MainActivity.this);

        if (authUtils.checkAuth()){
            //Adding fragment
            authUtils.checkRegister(authUtils.getCurrentUser().getUid(), new ProfileListener() {
                @Override
                public void isRegistered(Boolean state) {
                    if (state)
                        Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                    else
                        authUtils.doSignIn(MainActivity.this);
                }
            });

        }else {
            finish();
            authUtils.doSignIn(this);
        }

    }

    public void ui(){

    }


    public void setupBottomNavigation(BottomNavigationViewEx bottomNav){
        bottomNav.enableAnimation(true);
        bottomNav.enableItemShiftingMode(false);
        bottomNav.enableShiftingMode(true);

        SlideAdapter adapter = new SlideAdapter(getSupportFragmentManager());
        adapter.addFragment(new Discover());
        adapter.addFragment(new Nearby());
        adapter.addFragment(new Mine());

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(3);

        bottomNav.setupWithViewPager(viewPager);

        ui();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.addNew:
                startActivity(new Intent(MainActivity.this, AddNewJob.class));
                //init frag
                break;
        }
    }

    @Override
    public boolean onLongClick(View view) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_notify:
                startActivity(new Intent(MainActivity.this, Notifications.class));
                break;
        }
        return true;
    }

    class SlideAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();

        public SlideAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment){
            fragments.add(fragment);
        }
    }
}
