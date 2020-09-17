package com.spappstudio.conspectmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.spappstudio.conspectmanager.adapters.ItemMoveCallback;
import com.spappstudio.conspectmanager.adapters.RecyclerAdapeter;
import com.spappstudio.conspectmanager.dialogs.BackDialog;
import com.spappstudio.conspectmanager.dialogs.TypeOfPhotoDialog;

import java.util.ArrayList;
import java.util.Calendar;

public class CreateNoteActivity extends AppCompatActivity implements TypeOfPhotoDialog.OnFragmentInteractionListener {

    EditText editTextName;
    EditText editTextSubject;
    EditText editTextDate;
    EditText editTextAbout;
    RecyclerView recyclerView;
    RecyclerAdapeter recyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;

    ArrayList<String> imagesPath;
    ArrayList<String> notes;
    ArrayList<String> photo_path;
    int pageCount;

    String imageEncopded;
    ArrayList<String> imagesEncodetList;
    ArrayList<Uri> mArrayUri;

    Calendar calendar;
    String date;

    int code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        setTitle(getString(R.string.new_conspect_title));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        calendar = Calendar.getInstance();

        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextSubject = (EditText)findViewById(R.id.editTextSubject);
        editTextDate = (EditText)findViewById(R.id.editTextDate);
        editTextAbout = (EditText)findViewById(R.id.editTextAbout);

        String type = getIntent().getExtras().getString("type");
        if (type.equals("gallery")) {
            imagesPath = getIntent().getExtras().getStringArrayList("imagesPath");
            pageCount = imagesPath.size();
        } else if (type.equals("photo")) {
            imagesPath = new ArrayList<String>();
            imagesPath.add(getIntent().getExtras().getString("photoPath"));
            pageCount = 1;
        }

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerAdapeter(imagesPath);
        ItemTouchHelper.Callback callback = new ItemMoveCallback(recyclerViewAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.item_ok:
                if (!editTextName.getText().toString().equals("")) {
                    DBHelper dbHelper = new DBHelper(this);
                    dbHelper.insertConspect(
                            editTextName.getText().toString(),
                            editTextSubject.getText().toString(),
                            editTextDate.getText().toString(),
                            editTextAbout.getText().toString(),
                            imagesPath.size(),
                            imagesPath.get(0)
                    );
                    int conspect_id = dbHelper.lastInsertedConspectId();
                    notes = new ArrayList<String>();

                    for (int i = 0; i < imagesPath.size(); i++) {
                        notes.add("");
                        dbHelper.insertPhoto(conspect_id, i, imagesPath.get(i), notes.get(i));
                    }
                    Intent intent = new Intent(CreateNoteActivity.this, AllNotesActivity.class);
                    startActivity(intent);
                    Toast.makeText(this, getString(R.string.conspect_created), Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    editTextName.setHintTextColor(Color.RED);
                    Toast.makeText(this, getString(R.string.empty_conspect_title), Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickSetDate(View view) {
        new DatePickerDialog(CreateNoteActivity.this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show();
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

    public void onClickAddPhoto(View view) {
        TypeOfPhotoDialog dialog = new TypeOfPhotoDialog();
        dialog.show(getSupportFragmentManager(), "custom");
        code = 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (code == 0) {
            try {
                if (resultCode == Activity.RESULT_OK) {
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    imagesEncodetList = new ArrayList<String>();
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {
                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncopded = cursor.getString(columnIndex);
                            imagesEncodetList.add(imageEncopded);
                            cursor.close();
                        }
                        sendRequestCode(2, imagesEncodetList);
                        for (int i = 0; i < photo_path.size(); i++) {
                            imagesPath.add(photo_path.get(i));
                            pageCount = imagesPath.size();
                        }
                        Toast.makeText(this, getString(R.string.added_photos), Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, getString(R.string.gallery_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            if (resultCode == RESULT_OK) {
                for (int i = 0; i < photo_path.size(); i++) {
                    imagesPath.add(photo_path.get(i));
                    pageCount = imagesPath.size();
                }
                Toast.makeText(this, getString(R.string.added_photo), Toast.LENGTH_SHORT).show();
            }
        }
        recyclerViewAdapter.notifyDataSetChanged();
        recyclerView.refreshDrawableState();
    }

    @Override
    public void sendRequestCode(int code, ArrayList<String> photo_path) {
        this.photo_path = photo_path;
        this.code = 1;
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            monthOfYear++;
            String m;
            String d;
            if (monthOfYear < 9) {
                m = "0" + String.valueOf(monthOfYear);
            } else {
                m = String.valueOf(monthOfYear);
            }
            if (dayOfMonth < 9) {
                d = "0" + String.valueOf(dayOfMonth);
            } else {
                d = String.valueOf(dayOfMonth);
            }

            date = d + "." + m + "." + String.valueOf(year);
            editTextDate.setText(date);
        }
    };
}
