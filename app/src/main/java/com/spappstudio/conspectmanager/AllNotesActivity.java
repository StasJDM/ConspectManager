package com.spappstudio.conspectmanager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.spappstudio.conspectmanager.objects.Conspect;

import java.util.ArrayList;

public class AllNotesActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<Conspect> conspects;
    ArrayList<String> subjects;
    CustomAdapter adapter;
    ChipGroup chipGroup;

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_notes);

        setTitle(getString(R.string.all_conspects_title));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this);
        conspects = dbHelper.getAllConspects();

        adapter = new CustomAdapter(this, conspects);
        listView = (ListView)findViewById(R.id.listViewAllNotes);
        listView.setAdapter(adapter);

        subjects = dbHelper.getSubjectsFromConspects();
        subjects.add(0, getString(R.string.all));
        subjects.add(getString(R.string.other));
        chipGroup = findViewById(R.id.chip_group);
        for (int i = 0; i < subjects.size(); i++) {
            Chip chip = (Chip)this.getLayoutInflater().inflate(R.layout.layout_chip_choice, null, false);
            chip.setText(subjects.get(i));
            chipGroup.addView(chip, i);
        }
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup group, int checkedId) {
                Chip chip_checked = group.findViewById(checkedId);
                if (chip_checked != null) {
                    if (chip_checked.getText().equals(getString(R.string.all))) {
                        conspects = dbHelper.getAllConspects();
                    } else if (chip_checked.getText().equals(getString(R.string.other))) {
                        conspects = dbHelper.getConspectsBySubject("");
                    } else {
                        conspects = dbHelper.getConspectsBySubject(chip_checked.getText().toString());
                    }
                    adapter.updateDataset(conspects);
                    adapter.notifyDataSetChanged();
                    listView.refreshDrawableState();
                }
            }
        });
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
