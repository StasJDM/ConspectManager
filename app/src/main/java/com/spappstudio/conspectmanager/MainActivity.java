package com.spappstudio.conspectmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MainActivity extends AppCompatActivity {

    static final int REQUEST_ID_READ_WRITE_PERMISSION = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        if (android.os.Build.VERSION.SDK_INT >= 23) {

            int readPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE);
            int writePermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int cameraPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.CAMERA);
            int internetPermission = ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.INTERNET);

            if (writePermission != PackageManager.PERMISSION_GRANTED ||
                    readPermission != PackageManager.PERMISSION_GRANTED ||
                    cameraPermission != PackageManager.PERMISSION_GRANTED ||
                    internetPermission != PackageManager.PERMISSION_GRANTED) {

                this.requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.INTERNET},
                        REQUEST_ID_READ_WRITE_PERMISSION
                );
            }
        }
    }

    public void onClickAdd(View view) {
        Intent intent = new Intent(MainActivity.this, AddNotesActivity.class);
        startActivity(intent);
    }

    public void onClickAll(View view) {
        Intent intent = new Intent(MainActivity.this, AllNotesActivity.class);
        startActivity(intent);
    }

    public void onClickTasks(View view) {
        Intent intent = new Intent(MainActivity.this, TasksActivity.class);
        startActivity(intent);
    }

    public void onClickSubjects(View view) {
        Intent intent = new Intent(MainActivity.this, SubjectsActivity.class);
        startActivity(intent);
    }
}
