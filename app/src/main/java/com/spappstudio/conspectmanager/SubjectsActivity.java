package com.spappstudio.conspectmanager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);
        subjects = dbHelper.getSubjectWithConspectCount();

        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new SubjectsRecyclerAdapter(subjects);
        recyclerAdapter.setOnItemClickListener(new SubjectsRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(SubjectsActivity.this, ConspectsBySubjectsActivity.class);
                intent.putExtra("subject", subjects.get(position).name);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}