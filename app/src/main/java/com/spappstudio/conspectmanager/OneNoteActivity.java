package com.spappstudio.conspectmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.spappstudio.conspectmanager.adapters.RecyclerAdapeter;

import java.util.ArrayList;

public class OneNoteActivity extends AppCompatActivity {

    TextView textViewTitle1;
    TextView textViewTitle2;
    RecyclerView recyclerView;
    RecyclerView.Adapter recyclerViewAdapter;
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
        textViewTitle1.setText(name);
        if (!date.equals("") || !subject.equals("")) {
            textViewTitle2.setVisibility(View.VISIBLE);
        }
        if (date.equals("")) {
            textViewTitle2.setText(subject);
        } else if (subject.equals("")) {
            textViewTitle2.setText(date);
        } else {
            textViewTitle2.setText(subject + " - " + date);
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
            case R.id.item_edit:
                Intent intent = new Intent(OneNoteActivity.this, EditNoteActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("subject", subject);
                intent.putExtra("date", date);
                intent.putExtra("about", about);
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
}
