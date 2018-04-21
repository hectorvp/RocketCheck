package com.example.patil.rocketcheck;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;

public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        android.support.v7.app.ActionBar actionbar=getSupportActionBar();
        actionbar.setTitle(Html.fromHtml("<font color='#30a5ff'>Rocketcheck</font>"));
    }
}
