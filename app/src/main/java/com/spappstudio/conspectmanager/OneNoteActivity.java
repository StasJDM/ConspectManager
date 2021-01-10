package com.spappstudio.conspectmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.chip.Chip;
import com.spappstudio.conspectmanager.adapters.RecyclerAdapeter;
import com.spappstudio.conspectmanager.dialogs.DeleteDialog;
import com.spappstudio.conspectmanager.objects.Conspect;
import com.spappstudio.conspectmanager.objects.Photo;

import java.util.ArrayList;

public class OneNoteActivity extends AppCompatActivity {

    Chip chip;
    TextView textViewTitle1;
    TextView textViewTitle2;
    TextView textViewAbout;
    RecyclerView recyclerView;
    RecyclerAdapeter recyclerViewAdapter;
    RecyclerView.LayoutManager layoutManager;

    AdView adView;

    int id;
    int n_photos;
    String name;
    String subject;
    String date;
    String about;
    int page_now = 0;

    ArrayList<Photo> photos;
    ArrayList<String> photos_path = new ArrayList<String>();
    ArrayList<String> notes;
    int pageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_note);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");

        DBHelper dbHelper = new DBHelper(this);
        Conspect conspect = dbHelper.getConspectById(id);
        name = conspect.name;
        subject = conspect.subject;
        date = conspect.date;
        about = conspect.about;
        n_photos = conspect.n_photos;

        photos = dbHelper.getPhotos(id);
        pageCount = photos.size();

        photos_path = new ArrayList<String>();
        for (int i = 0; i < photos.size(); i++) {
            photos_path.add(photos.get(i).path_to_img);
        }

        textViewTitle1 = (TextView)findViewById(R.id.textViewTitle1);
        textViewTitle2 = (TextView)findViewById(R.id.textViewTitle2);
        textViewAbout = (TextView)findViewById(R.id.textViewAbout);
        chip = (Chip)findViewById(R.id.chip);

        textViewTitle1.setText(name);
        if (!date.isEmpty()) {
            textViewTitle2.setText(date);
        } else {
            textViewTitle2.setVisibility(View.GONE);
        }
        if (!subject.isEmpty()) {
            chip.setText(subject);
            chip.setSelected(true);
        } else {
            chip.setVisibility(View.GONE);
        }
        if (!about.equals("")) {
            textViewAbout.setText(about);
        } else {
            textViewAbout.setVisibility(View.GONE);
        }

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RecyclerAdapeter(photos_path);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.item_edit:
                Intent intent = new Intent(OneNoteActivity.this, EditNoteActivity.class);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
                return true;
            case R.id.item_delete:
                DeleteDialog dialog = new DeleteDialog();
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
        getMenuInflater().inflate(R.menu.one_note_menu, menu);
        return true;
    }
}
