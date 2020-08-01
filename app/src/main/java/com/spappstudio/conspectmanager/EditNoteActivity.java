package com.spappstudio.conspectmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
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
import android.widget.EditText;
import android.widget.Toast;

import com.spappstudio.conspectmanager.adapters.ItemMoveCallback;
import com.spappstudio.conspectmanager.adapters.RecyclerAdapeter;
import com.spappstudio.conspectmanager.dialogs.TypeOfPhotoDialog;

import java.util.ArrayList;

public class EditNoteActivity extends AppCompatActivity implements  TypeOfPhotoDialog.OnFragmentInteractionListener {

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

    int code;

    int id;
    int n_photos;
    String name;
    String subject;
    String date;
    String about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        setTitle("Изменить конспект");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextSubject = (EditText)findViewById(R.id.editTextSubject);
        editTextDate = (EditText)findViewById(R.id.editTextDate);
        editTextAbout = (EditText)findViewById(R.id.editTextAbout);

        id = getIntent().getIntExtra("id", -1);
        editTextName.setText(getIntent().getStringExtra("name"));
        editTextSubject.setText(getIntent().getStringExtra("subject"));
        editTextDate.setText(getIntent().getStringExtra("date"));
        editTextAbout.setText(getIntent().getStringExtra("about"));

        imagesPath = getIntent().getStringArrayListExtra("imagesPath");
        pageCount = imagesPath.size();

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
                super.onBackPressed();
                return true;
            case R.id.item_ok:
                if (!editTextName.getText().toString().equals("")) {
                    DBHelper dbHelper = new DBHelper(this);
                    imagesPath = recyclerViewAdapter.getDataset();
                    if (imagesPath.size() == 0) {
                        Toast.makeText(this, "Конспект должен содержать хотя бы одну фотографию", Toast.LENGTH_SHORT).show();
                        return true;
                    }
                    dbHelper.updateConspect(
                            id,
                            name = editTextName.getText().toString(),
                            subject = editTextSubject.getText().toString(),
                            date = editTextDate.getText().toString(),
                            about = editTextAbout.getText().toString(),
                            n_photos = imagesPath.size(),
                            imagesPath.get(0)
                    );
                    dbHelper.deletePhotosInConspect(id);
                    notes = new ArrayList<String>();

                    for (int i = 0; i < imagesPath.size(); i++) {
                        notes.add("");
                        dbHelper.insertPhoto(id, i, imagesPath.get(i), notes.get(i));
                    }
                    Intent intent = new Intent(EditNoteActivity.this, OneNoteActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("subject", subject);
                    intent.putExtra("date", date);
                    intent.putExtra("about", about);
                    intent.putExtra("n_photos", n_photos);
                    startActivity(intent);
                    Toast.makeText(this, "Конспект изменен", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    editTextName.setHintTextColor(Color.RED);
                    Toast.makeText(this, "Название коспекта обязательно для заполнения", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
                        Toast.makeText(this, "Фото добавлены", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка галлереи!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (resultCode == RESULT_OK) {
                for (int i = 0; i < photo_path.size(); i++) {
                    imagesPath.add(photo_path.get(i));
                    pageCount = imagesPath.size();
                }
                Toast.makeText(this, "Фото добавлено", Toast.LENGTH_SHORT).show();
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

}
