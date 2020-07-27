package com.spappstudio.conspectmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class EditNoteActivity extends AppCompatActivity implements  TypeOfPhotoDialog.OnFragmentInteractionListener {

    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    EditText editTextName;
    EditText editTextSubject;
    EditText editTextDate;
    EditText editTextAbout;

    ArrayList<String> imagesPath;
    ArrayList<String> notes;
    ArrayList<String> photo_path;
    int pageCount;

    String imageEncopded;
    ArrayList<String> imagesEncodetList;
    ArrayList<Uri> mArrayUri;

    int code;

    int id;
    int n_photos;
    String name;
    String subject;
    String date;
    String about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        setTitle("Изменить конспект");
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        editTextName = (EditText)findViewById(R.id.editTextName);
        editTextSubject = (EditText)findViewById(R.id.editTextSubject);
        editTextDate = (EditText)findViewById(R.id.editTextDate);
        editTextAbout = (EditText)findViewById(R.id.editTextAbout);

        id = getIntent().getIntExtra("id", -1);
        editTextName.setText(getIntent().getStringExtra("name"));
        editTextSubject.setText(getIntent().getStringExtra("subject"));
        editTextDate.setText(getIntent().getStringExtra("date"));
        editTextAbout.setText(getIntent().getStringExtra("about"));

        imagesPath = getIntent().getStringArrayListExtra("imagesPath");
        pageCount = imagesPath.size();

        viewPager = (ViewPager)findViewById(R.id.pager);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                super.onBackPressed();
                return true;
            case R.id.item_ok:
                if (!editTextName.getText().toString().equals("")) {
                    DBHelper dbHelper = new DBHelper(this);
                    dbHelper.updateConspect(
                            id,
                            name = editTextName.getText().toString(),
                            subject = editTextSubject.getText().toString(),
                            date = editTextDate.getText().toString(),
                            about = editTextAbout.getText().toString(),
                            n_photos = imagesPath.size()
                    );
                    //int conspect_id = dbHelper.lastInsertedConspectId();
                    notes = new ArrayList<String>();

                    for (int i = 0; i < imagesPath.size(); i++) {
                        notes.add("");
                        //dbHelper.insertPhoto(conspect_id, i, imagesPath.get(i), notes.get(i));
                    }
                    Intent intent = new Intent(EditNoteActivity.this, OneNoteActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("name", name);
                    intent.putExtra("subject", subject);
                    intent.putExtra("date", date);
                    intent.putExtra("about", about);
                    intent.putExtra("n_photos", n_photos);
                    startActivity(intent);
                    Toast.makeText(this, "Конспект изменен", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    editTextName.setHintTextColor(Color.RED);
                    Toast.makeText(this, "Название коспекта обязательно для заполнения", Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class ImagesFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImagesFragmentPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position, imagesPath.get(position));
        }

        @Override
        public int getCount() {
            return pageCount;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.create_note_menu, menu);
        return true;
    }

    public void onClickAddPhoto(View view) {
        TypeOfPhotoDialog dialog = new TypeOfPhotoDialog();
        dialog.show(getSupportFragmentManager(), "custom");
        code = 0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (code == 0) {
            try {
                if (resultCode == Activity.RESULT_OK) {
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
                        sendRequestCode(2, imagesEncodetList);
                        for (int i = 0; i < photo_path.size(); i++) {
                            imagesPath.add(photo_path.get(i));
                            pageCount = imagesPath.size();
                        }
                        Toast.makeText(this, "Фото добавлены", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка галлереи!", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (resultCode == RESULT_OK) {
                for (int i = 0; i < photo_path.size(); i++) {
                    imagesPath.add(photo_path.get(i));
                    pageCount = imagesPath.size();
                }
                Toast.makeText(this, "Фото добавлено", Toast.LENGTH_SHORT).show();
            }
        }
        pagerAdapter.notifyDataSetChanged();
        viewPager.refreshDrawableState();
    }

    @Override
    public void sendRequestCode(int code, ArrayList<String> photo_path) {
        this.photo_path = photo_path;
        this.code = 1;
    }

}
