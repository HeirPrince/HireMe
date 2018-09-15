package com.nassaty.hireme.intentActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.nassaty.hireme.R;

public class replyReview extends AppCompatActivity {

    Toolbar toolbar;
    TextView reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_review);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Enter your Reply");

        reply = findViewById(R.id.reply_text);

        String data = reply.getText().toString();

        Intent returnIntent = new Intent();
        returnIntent.putExtra("reply", data);
        setResult(100);

        finish();

    }
}
