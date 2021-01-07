package com.spappstudio.conspectmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.spappstudio.conspectmanager.adapters.ConspectsRecyclerAdapter;
import com.spappstudio.conspectmanager.adapters.SubjectsRecyclerAdapter;
import com.spappstudio.conspectmanager.objects.Conspect;

import java.util.ArrayList;

public class ConspectsBySubjectsActivity extends AppCompatActivity {

    DBHelper dbHelper;

    String subject;
    ConspectsRecyclerAdapter recyclerAdapter;
    ArrayList<Conspect> conspects;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conspects_by_subjects);

        dbHelper = new DBHelper(this);
        subject = getIntent().getExtras().getString("subject");

        conspects = dbHelper.getConspectsBySubject(subject);

        recyclerView = findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter = new ConspectsRecyclerAdapter(conspects);
        recyclerAdapter.setOnItemClickListener(new ConspectsRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(ConspectsBySubjectsActivity.this, OneNoteActivity.class);
                intent.putExtra("id", conspects.get(position).id);
                intent.putExtra("name", conspects.get(position).name);
                intent.putExtra("subject", conspects.get(position).subject);
                intent.putExtra("date", conspects.get(position).date);
                intent.putExtra("about", conspects.get(position).about);
                intent.putExtra("n_photos", conspects.get(position).n_photos);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });
        recyclerView.setAdapter(recyclerAdapter);
    }
}