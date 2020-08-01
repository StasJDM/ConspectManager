package com.spappstudio.conspectmanager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.spappstudio.conspectmanager.objects.Conspect;

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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

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
}
