package com.nassaty.hireme.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.nassaty.hireme.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nassaty.hireme.FavoritesAdapter;
import com.nassaty.hireme.model.Job;
import com.nassaty.hireme.room.FavListViewModel;

import java.util.ArrayList;
import java.util.List;

public class Favorites extends AppCompatActivity implements View.OnLongClickListener{

    private RecyclerView fav_list;
    private FavListViewModel favListViewModel;
    private FavoritesAdapter adapter;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        fav_list = findViewById(R.id.fav_list);
        adapter = new FavoritesAdapter(this, new ArrayList<Job>(), this);
        fav_list.setLayoutManager(new LinearLayoutManager(this));
        fav_list.setAdapter(adapter);

        favListViewModel = ViewModelProviders.of(this).get(FavListViewModel.class);
        favListViewModel.getFavList().observe(Favorites.this, new Observer<List<Job>>() {
            @Override
            public void onChanged(@Nullable List<Job> jobs) {
                adapter.addJobs(jobs);
            }
        });

    }

    @Override
    public boolean onLongClick(View view) {
        Job job = (Job)view.getTag();
        favListViewModel.deleteFav(job);
        return true;
    }
}
