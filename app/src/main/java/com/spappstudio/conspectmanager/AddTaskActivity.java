package com.spappstudio.conspectmanager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.spappstudio.conspectmanager.dialogs.BackDialog;
import com.spappstudio.conspectmanager.dialogs.SelectConspectDialog;
import com.spappstudio.conspectmanager.dialogs.SelectSubjectDialog;
import com.spappstudio.conspectmanager.objects.Task;

import java.util.ArrayList;

public class AddTaskActivity extends AppCompatActivity {

    DBHelper dbHelper;

    EditText editTextTitle;
    EditText editTextText;

    String subject = "";
    String dateOfCreate = "";
    String deadLine = "";

    int checkListCount = 0;
    int conspectsCount = 0;

    ArrayList<String> subjectsArrayList;
    ArrayList<String> conspectsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        setTitle(getString(R.string.add_task));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextText = findViewById(R.id.editTextText);

        dbHelper = new DBHelper(this);

        subjectsArrayList = dbHelper.getSubjectsFromConspects();
        conspectsArrayList = dbHelper.getAllConspectsToString();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.item_ok:
                dateOfCreate = dbHelper.getTodayDateString();
                Task task;
                task = new Task(-1,
                        editTextTitle.getText().toString(),
                        subject,
                        dateOfCreate,
                        deadLine,
                        editTextText.getText().toString(),
                        checkListCount,
                        conspectsCount,
                        0);
                dbHelper.insertTask(task);
                Toast.makeText(this, getString(R.string.task_added), Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        BackDialog backDialog = new BackDialog();
        backDialog.show(getSupportFragmentManager(), "Back");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_note_menu, menu);
        return true;
    }

    public void onClickSubjects(View view) {
        String[] arr = new String[subjectsArrayList.size()];
        SelectSubjectDialog subjectDialog = new SelectSubjectDialog(subjectsArrayList.toArray(arr));
        subjectDialog.show(getSupportFragmentManager(), "Subjects");
    }

    public void onClickConspects(View view) {
        String[] arr = new String[conspectsArrayList.size()];
        SelectConspectDialog conspectDialog = new SelectConspectDialog(conspectsArrayList.toArray(arr));
        conspectDialog.show(getSupportFragmentManager(), "Conspects");
    }

}