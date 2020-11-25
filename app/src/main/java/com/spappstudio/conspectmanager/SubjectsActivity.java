package com.spappstudio.conspectmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class SubjectsActivity extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        dbHelper = new DBHelper(this);
    }
}