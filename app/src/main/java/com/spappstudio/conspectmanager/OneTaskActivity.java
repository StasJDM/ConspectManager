package com.spappstudio.conspectmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.spappstudio.conspectmanager.dialogs.DeleteDialog;
import com.spappstudio.conspectmanager.dialogs.DeleteTaskDialog;
import com.spappstudio.conspectmanager.objects.Conspect;
import com.spappstudio.conspectmanager.objects.Task;

import java.util.ArrayList;

public class OneTaskActivity extends AppCompatActivity {

    int id;

    Task task;
    ArrayList<Conspect> conspects;
    DBHelper dbHelper;

    TextView textViewText;
    TextView textViewConspect;
    TextView textViewDeadline;
    Chip chipConspects;
    Chip chipDeadline;
    View viewLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_task);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        textViewText = findViewById(R.id.textViewText);
        textViewConspect = findViewById(R.id.textViewConpect);
        textViewDeadline = findViewById(R.id.textViewDeadline);
        chipConspects = findViewById(R.id.chipConspect);
        chipDeadline = findViewById(R.id.chipDeadline);
        viewLine = findViewById(R.id.line);

        dbHelper = new DBHelper(this);

        id = getIntent().getExtras().getInt("id");
        task = dbHelper.getTask(id);
        if (task.conspectsCount > 0) {
            conspects = task.getConspects();
            chipConspects.setText(conspects.get(0).name);
            chipConspects.setVisibility(View.VISIBLE);
            textViewConspect.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.VISIBLE);
        }
        if (!task.deadline.equals("")) {
            chipDeadline.setText(task.deadline);
            chipDeadline.setVisibility(View.VISIBLE);
            textViewDeadline.setVisibility(View.VISIBLE);
            viewLine.setVisibility(View.VISIBLE);
        }

        setTitle(task.title);
        textViewText.setText(task.text);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.item_edit:
                Intent intent = new Intent(OneTaskActivity.this, AddTaskActivity.class);
                intent.putExtra("type", "edit");
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
                return true;
            case R.id.item_delete:
                DeleteTaskDialog dialog = new DeleteTaskDialog();
                Bundle args = new Bundle();
                args.putInt("id", id);
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "custom");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.one_task_menu, menu);
        return true;
    }

    public void onClickOpenConspect(View view) {
        Intent intent = new Intent(OneTaskActivity.this, OneNoteActivity.class);
        intent.putExtra("id", conspects.get(0).id);
        startActivity(intent);
    }
}