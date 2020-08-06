package com.spappstudio.conspectmanager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddNotesActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_GALLERY = 2;
    String currentPhotoPath;
    String imageEncopded;
    ArrayList<String> imagesEncodetList;
    ArrayList<Uri> mArrayUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);
        setTitle("Добавить конспект");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    public void onClickPhoto(View view) {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            File file = new File(Environment.getExternalStorageDirectory(), timeStamp + ".png");
            Uri photo_dir = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photo_dir);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            currentPhotoPath = file.getAbsolutePath();

        } catch (ActivityNotFoundException e) {
            String errorMessage = "Ошибка камеры";
            Toast toast = Toast.makeText(AddNotesActivity.this, errorMessage, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void onClickGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Выберите фотографии"), REQUEST_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_GALLERY:
                try {
                    if (resultCode == RESULT_OK) {
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        imagesEncodetList = new ArrayList<String>();
                        mArrayUri = new ArrayList<Uri>();
                        if (data.getClipData() != null) {
                            ClipData mClipData = data.getClipData();
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
                        } else {
                            Uri uri = data.getData();
                            mArrayUri.add(uri);
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            cursor.moveToFirst();
                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncopded = cursor.getString(columnIndex);
                            imagesEncodetList.add(imageEncopded);
                            cursor.close();
                        }
                        Intent intent = new Intent(AddNotesActivity.this, CreateNoteActivity.class);
                        intent.putExtra("type", "gallery");
                        intent.putStringArrayListExtra("imagesPath", imagesEncodetList);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent(AddNotesActivity.this, CreateNoteActivity.class);
                    intent.putExtra("type", "photo");
                    intent.putExtra("photoPath", currentPhotoPath);
                    startActivity(intent);
                }
                break;
        }
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
    public void onBackPressed() {
        finish();
    }
}
