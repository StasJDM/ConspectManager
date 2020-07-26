package com.stasyanstudio.practic;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.ArrayList;

public class AllNotesActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Conspect> conspects;
    CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);
        setTitle("Все конспекты");

        DBHelper dbHelper = new DBHelper(this);
        conspects = dbHelper.getAllConspects();

        adapter = new CustomAdapter(this, conspects);
        listView = (ListView)findViewById(R.id.listViewAllNotes);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
