package com.spappstudio.conspectmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.spappstudio.conspectmanager.adapters.SubjectsRecyclerAdapter;
import com.spappstudio.conspectmanager.objects.Subject;

import java.util.ArrayList;

public class SubjectsActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    SubjectsRecyclerAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    DBHelper dbHelper;

    ArrayList<Subject> subjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subjects);

        dbHelper = new DBHelper(this);
        subjects = dbHelper.getSubjectWithConspectCount();

        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new SubjectsRecyclerAdapter(subjects);
        recyclerView.setAdapter(recyclerAdapter);
    }
}