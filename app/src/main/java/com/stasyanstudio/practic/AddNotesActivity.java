package com.stasyanstudio.practic;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.os.Environment.getExternalStoragePublicDirectory;

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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_GALLERY);
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
                            Intent intent = new Intent(AddNotesActivity.this, CreateNoteActivity.class);
                            intent.putExtra("type", "gallery");
                            intent.putStringArrayListExtra("imagesPath", imagesEncodetList);
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Somthing went wrong", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Log.d("LOG", "Image path: " + currentPhotoPath);
                    Intent intent = new Intent(AddNotesActivity.this, CreateNoteActivity.class);
                    intent.putExtra("type", "photo");
                    intent.putExtra("photoPath", currentPhotoPath);
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
