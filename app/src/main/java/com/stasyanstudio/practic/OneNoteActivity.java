package com.stasyanstudio.practic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class OneNoteActivity extends AppCompatActivity {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    TextView textViewTitle1;
    TextView textViewTitle2;

    int id;
    int n_photos;
    String name;
    String subject;
    String date;
    String about;

    //ArrayList<String> imagesPath;
    ArrayList<Photo> photos;
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
        Log.d("LOG", "PAGE COUNT: " + pageCount);
        Log.d("LOG", "PAGE COUNT: " + dbHelper.getPhotoCount());

        textViewTitle1 = (TextView)findViewById(R.id.textViewTitle1);
        textViewTitle2 = (TextView)findViewById(R.id.textViewTitle2);
        textViewTitle1.setText(name );
        if (date.equals("")) {
            textViewTitle2.setText(subject);
        } else if (subject.equals("")) {
            textViewTitle2.setText(date);
        } else {
            textViewTitle2.setText(subject + " - " + date);
        }


        viewPager = (ViewPager)findViewById(R.id.pagerOneConspect);
        pagerAdapter = new ImagesFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                Log.d("LOG", "onPageSelected, position = " + position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private class ImagesFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImagesFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            //return PageFragment.newInstance(position, photos.get(position).path_to_img);
            return PageFragment.newInstance(photos.get(position).number + 1, photos.get(position).path_to_img);
        }

        @Override
        public int getCount() {
            Log.d("LOG", String.valueOf(pageCount));
            return pageCount;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.one_note_menu, menu);
        return true;
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
                ArrayList<String> photos_path = new ArrayList<String>();
                for (int i = 0; i < photos.size(); i++) {
                    photos_path.add(photos.get(i).path_to_img);
                }
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
