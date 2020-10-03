package com.spappstudio.conspectmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.spappstudio.conspectmanager.objects.Task;

public class OneTaskActivity extends AppCompatActivity {

    Task task;
    DBHelper dbHelper;

    TextView textViewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_task);

        textViewText = findViewById(R.id.textViewText);

        dbHelper = new DBHelper(this);

        int id = getIntent().getExtras().getInt("id");
        task = dbHelper.getTask(id);

        setTitle(task.title);
        textViewText.setText(task.text);

    }
}