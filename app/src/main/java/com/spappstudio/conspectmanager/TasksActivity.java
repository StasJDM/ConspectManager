package com.spappstudio.conspectmanager;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.spappstudio.conspectmanager.adapters.TasksRecyclerAdapter;
import com.spappstudio.conspectmanager.dialogs.DeleteTaskDialog;
import com.spappstudio.conspectmanager.dialogs.SelectSubjectDialog;
import com.spappstudio.conspectmanager.dialogs.TasksLongClickDialog;
import com.spappstudio.conspectmanager.objects.Task;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TasksActivity.this, AddTaskActivity.class);
                intent.putExtra("type", "add");
                startActivityForResult(intent, REQUES_ADD_TASK);
            }
        });

        dbHelper = new DBHelper(this);
        tasks = dbHelper.getAllTasks();
        Collections.reverse(tasks);

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        recyclerLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerLayoutManager);

        recyclerAdapter = new TasksRecyclerAdapter(tasks);

        recyclerAdapter.setOnItemClickListener(new TasksRecyclerAdapter.ItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                Intent intent = new Intent(TasksActivity.this, OneTaskActivity.class);
                intent.putExtra("id", tasks.get(position).id);
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                TasksLongClickDialog dialog = new TasksLongClickDialog(new DeleteTaskDialog.NotifyListener() {
                    @Override
                    public void onDelete(int position) {
                        tasks = dbHelper.getAllTasks();
                        recyclerAdapter.updateDataset(tasks);
                       recyclerAdapter.notifyItemRemoved(position);
                    }
                });
                Bundle args = new Bundle();
                args.putInt("id", tasks.get(position).id);
                args.putInt("position", position);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "task_long_dialog");
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

    @Override
    protected void onResume() {
        super.onResume();
        tasks = dbHelper.getAllTasks();
        recyclerAdapter.updateDataset(tasks);
        recyclerAdapter.notifyDataSetChanged();
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