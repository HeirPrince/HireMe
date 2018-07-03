package com.nassaty.hireme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.nassaty.hireme.R;
import com.nassaty.hireme.fragments.JobsList;
import com.nassaty.hireme.listeners.ProfileListener;
import com.nassaty.hireme.utils.AuthUtils;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {

    private AuthUtils authUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        authUtils = new AuthUtils(this);

        if (authUtils.checkAuth()){
            //Adding fragment
            authUtils.checkRegister(authUtils.getCurrentUser().getPhoneNumber(), new ProfileListener() {
                @Override
                public void isRegistered(Boolean state) {
                    if (state){
                        JobsList fragment = new JobsList();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();

                        transaction.replace(R.id.frag_container, fragment);
                        transaction.addToBackStack(null);

                        transaction.commit();
                    }else {
                        startActivity(new Intent(MainActivity.this, Register.class));
                        finish();
                    }
                }
            });

        }else {
            finish();
            authUtils.doSignIn(this);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_favorites) {
            startActivity(new Intent(MainActivity.this, Favorites.class));
        }else if (id == R.id.action_profile){
            startActivity(new Intent(MainActivity.this, Profile.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onLongClick(View view) {
//        Job job = (Job)view.getTag();
//        jobListViewModel.deleteJob(job);
        return true;
    }

    @Override
    public void onClick(View view) {
//        int id = view.getId();
//
//        switch (id){
//            case R.id.fab:
//                startActivity(new Intent(MainActivity.this, AddNewJob.class));
//                break;
//        }
    }
}
