package com.spappstudio.conspectmanager;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.spappstudio.conspectmanager.adapters.TasksRecyclerAdapter;
import com.spappstudio.conspectmanager.objects.Task;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;

import java.util.ArrayList;

public class TasksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerAdapter;
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        dbHelper = new DBHelper(this);
        //tasks = dbHelper.getAllTasks();
        tasks = new ArrayList<>();
        tasks.add(new Task(-1, "Task 1", "Math", "19.09.2020", "24.09.2020", "To do somthing\nSecond string\nAnd another string\nIt's the task", 0, 0, 0));
        tasks.add(new Task(-1, "Task 2", "Physics", "19.09.2020", "24.09.2020", "To do somthing\nSecond string\nAnd another string\nIt's the task", 0, 0, 0));

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        recyclerAdapter = new TasksRecyclerAdapter(tasks);
        recyclerView.setAdapter(recyclerAdapter);
    }
}