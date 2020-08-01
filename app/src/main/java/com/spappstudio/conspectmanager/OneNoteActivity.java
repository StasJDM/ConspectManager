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

import com.google.android.material.chip.Chip;
import com.spappstudio.conspectmanager.adapters.RecyclerAdapeter;
import com.spappstudio.conspectmanager.dialogs.DeleteDialog;
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

        Intent intent = getIntent();
        id = intent.getExtras().getInt("id");
        name = intent.getExtras().getString("name");
        subject = intent.getExtras().getString("subject");
        date = intent.getExtras().getString("date");
        about = intent.getExtras().getString("about");
        n_photos = intent.getExtras().getInt("n_photos");

        DBHelper dbHelper = new DBHelper(this);
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
        textViewTitle2.setText(date);
        chip.setText(subject);
        chip.setSelected(true);
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
                intent.putExtra("name", name);
                intent.putExtra("subject", subject);
                intent.putExtra("date", date);
                intent.putExtra("about", about);
                photos_path = recyclerViewAdapter.getDataset();
                intent.putStringArrayListExtra("imagesPath", photos_path);
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
