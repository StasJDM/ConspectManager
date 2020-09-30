package com.spappstudio.conspectmanager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.spappstudio.conspectmanager.adapters.TasksRecyclerAdapter;
import com.spappstudio.conspectmanager.objects.Task;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {

    private static final int REQUES_ADD_TASK = 0;

    private RecyclerView recyclerView;
    private TasksRecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager recyclerLayoutManager;

    private ArrayList<Task> tasks;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TasksActivity.this, AddTaskActivity.class);
                startActivityForResult(intent, REQUES_ADD_TASK);
            }
        });

        dbHelper = new DBHelper(this);
        tasks = dbHelper.getAllTasks();

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        recyclerAdapter = new TasksRecyclerAdapter(tasks);

        recyclerAdapter.setOnItemClickListener(new TasksRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Log.d("LOG", String.valueOf(position));
                Intent intent = new Intent(TasksActivity.this, OneTaskActivity.class);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        recyclerView.setAdapter(recyclerAdapter);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUES_ADD_TASK && resultCode == RESULT_OK) {
            tasks = dbHelper.getAllTasks();
            recyclerAdapter.updateDataset(tasks);
            recyclerAdapter.notifyDataSetChanged();
        }
    }
}