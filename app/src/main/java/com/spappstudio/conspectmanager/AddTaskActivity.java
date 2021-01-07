package com.spappstudio.conspectmanager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.spappstudio.conspectmanager.dialogs.BackDialog;
import com.spappstudio.conspectmanager.dialogs.SelectConspectDialog;
import com.spappstudio.conspectmanager.dialogs.SelectSubjectDialog;
import com.spappstudio.conspectmanager.interfaces.SelectConspectInterface;
import com.spappstudio.conspectmanager.interfaces.SelectSubjectInterface;
import com.spappstudio.conspectmanager.objects.CheckListItem;
import com.spappstudio.conspectmanager.objects.Conspect;
import com.spappstudio.conspectmanager.objects.Task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.stream.Collectors;

public class AddTaskActivity extends AppCompatActivity implements SelectSubjectInterface, SelectConspectInterface {

    DBHelper dbHelper;

    Task task;
    int id;

    EditText editTextTitle;
    EditText editTextText;
    String type;

    String subject = "";
    String dateOfCreate = "";
    String deadLine = "";

    int checkListCount = 0;
    int conspectsCount = 0;

    ArrayList<String> subjectsArrayList;
    ArrayList<String> conspectsArrayList;
    ArrayList<Integer> conspectsId;

    ArrayList<Conspect> conspects;
    ArrayList<CheckListItem> checkListItems;

    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        Intent intent = getIntent();
        type =  intent.getExtras().getString("type");

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTextTitle = findViewById(R.id.editTextTitle);
        editTextText = findViewById(R.id.editTextText);

        dbHelper = new DBHelper(this);

        if (type.equals("edit")) {
            id = intent.getExtras().getInt("id");
            task = dbHelper.getTask(id);
            editTextTitle.setText(task.title);
            editTextText.setText(task.text);
            setTitle(getString(R.string.edit_task));
        } else {
            setTitle(getString(R.string.add_task));
        }

        subjectsArrayList = dbHelper.getSubjectsFromConspects();
        conspectsArrayList = dbHelper.getAllConspectsToString();
        conspectsId = dbHelper.getAllConspectsId();

        conspects = new ArrayList<>();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.item_ok:
                if (editTextTitle.getText().toString().equals(""))
                {
                    editTextTitle.setError(getString(R.string.enter_a_title));
                    return false;
                } else {
                    if (editTextText.getText().toString().equals("")) {
                        editTextText.setError(getString(R.string.enter_text));
                        return false;
                    } else {
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
                        if (conspectsCount > 0) {
                            task.addConspects(conspects);
                        }
                        if (checkListCount > 0) {
                            task.addCheckList(checkListItems);
                        }
                        if (type.equals("add")) {
                            dbHelper.insertTask(task);
                            Toast.makeText(this, getString(R.string.task_added), Toast.LENGTH_SHORT).show();
                        } else {
                            task.id = id;
                            dbHelper.updateTask(task);
                            Toast.makeText(this, getString(R.string.task_edited), Toast.LENGTH_SHORT).show();
                        }
                        finish();
                        return true;
                    }
                }
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
        SelectConspectDialog conspectDialog = new SelectConspectDialog(conspectsArrayList.toArray(arr), conspectsId);
        conspectDialog.show(getSupportFragmentManager(), "Conspects");
    }

    public void onClickDeadline(View view) {
        new DatePickerDialog(AddTaskActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            deadLine = dayOfMonth + "." + month + "." + year;
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    };

    @Override
    public void selectSubject(String subjectName) {
        this.subject = subjectName;
        Log.d("Log", subjectName);
    }

    @Override
    public void selectConspect(int id) {
        Conspect conspect = dbHelper.getConspectById(id);
        Boolean cons = false;
        for (Conspect c:conspects) {
            if (conspect.id == c.id) {
                cons = true;
            }
        }
        if (!cons) {
            conspects.add(conspect);
        }
        this.conspectsCount = conspects.size();
        Log.d("LOG", "CONSPECTS: " + conspects.size());
    }
}