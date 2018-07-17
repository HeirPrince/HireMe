package com.nassaty.hireme.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.nassaty.hireme.R;
import com.nassaty.hireme.fragments.JobsList;
import com.nassaty.hireme.listeners.ProfileListener;
import com.nassaty.hireme.utils.AuthUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    private AuthUtils authUtils;
    private View filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        authUtils = new AuthUtils(this);

        filter = findViewById(R.id.filter);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                MenuInflater inflater = popupMenu.getMenuInflater();
                inflater.inflate(R.menu.filter_popup_menu, popupMenu.getMenu());

                try {
                    Field[] fields = popupMenu.getClass().getDeclaredFields();
                    for (Field field : fields) {
                        if ("mPopup".equals(field.getName())) {
                            field.setAccessible(true);
                            Object menuPopupHelper = field.get(popupMenu);
                            Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                            Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                            setForceIcons.invoke(menuPopupHelper, true);
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                popupMenu.setOnMenuItemClickListener(MainActivity.this);
                popupMenu.show();
            }
        });

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
    public boolean onLongClick(View view) {
        return true;
    }

    @Override
    public void onClick(View view) {
        //on click
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.action_profile:
                Toast.makeText(this, "clicked me", Toast.LENGTH_SHORT).show();
                break;
            default:
                return false;
        }

        return false;
    }
}
