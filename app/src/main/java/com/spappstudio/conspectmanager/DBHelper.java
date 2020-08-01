package com.spappstudio.conspectmanager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.spappstudio.conspectmanager.objects.Conspect;
import com.spappstudio.conspectmanager.objects.Photo;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ConspectManagerDB";

    public static final String CONSPECT_TABLE_NAME = "ConspectManagerConspect";
    public static final String PHOTO_TABLE_NAME = "ConspectManagerPhoto";

    public static final String CONSPECT_TABLE_COLUMN_ID = "id";
    public static final String CONSPECT_TABLE_COLUMN_N_PHOTOS = "n_photos";
    public static final String CONSPECT_TABLE_COLUMN_NAME = "name";
    public static final String CONSPECT_TABLE_COLUMN_SUBJECT = "subject";
    public static final String CONSPECT_TABLE_COLUMN_DATE = "date";
    public static final String CONSPECT_TABLE_COLUMN_ABOUT = "about";
    public static final String CONSPECT_TABLE_COLUMN_FIRST_IMAGE_PATH = "first_image_path";

    public static final String PHOTO_TABLE_COLUMN_ID = "id";
    public static final String PHOTO_TABLE_COLUMN_ID_CONSPECT = "id_conspect";
    public static final String PHOTO_TABLE_COLUMN_NUMBER_IN_CONSPECT = "number_in_conspect";
    public static final String PHOTO_TABLE_COLUMN_PATH = "path";
    public static final String PHOTO_TABLE_COLUMN_NOTE = "note";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + CONSPECT_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, n_photos INTEGER, name TEXT, subject TEXT, date TEXT, about TEXT, first_image_path TEXT)");
        db.execSQL("CREATE TABLE " + PHOTO_TABLE_NAME + "(id INTEGER PRIMARY KEY AUTOINCREMENT, id_conspect INTEGER, number_in_conspect INTEGER, path TEXT, note TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
                break;
            default:
                break;
        }
    }

    public boolean insertConspect(String name, String subject, String date, String about, int n_photos, String first_image_path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONSPECT_TABLE_COLUMN_NAME, name);
        contentValues.put(CONSPECT_TABLE_COLUMN_SUBJECT, subject);
        contentValues.put(CONSPECT_TABLE_COLUMN_DATE, date);
        contentValues.put(CONSPECT_TABLE_COLUMN_ABOUT, about);
        contentValues.put(CONSPECT_TABLE_COLUMN_N_PHOTOS, n_photos);
        contentValues.put(CONSPECT_TABLE_COLUMN_FIRST_IMAGE_PATH, first_image_path);
        db.insert(CONSPECT_TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateConspect(int id, String name, String subject, String date, String about, int n_photos, String first_image_path) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CONSPECT_TABLE_COLUMN_NAME, name);
        contentValues.put(CONSPECT_TABLE_COLUMN_SUBJECT, subject);
        contentValues.put(CONSPECT_TABLE_COLUMN_DATE, date);
        contentValues.put(CONSPECT_TABLE_COLUMN_ABOUT, about);
        contentValues.put(CONSPECT_TABLE_COLUMN_N_PHOTOS, n_photos);
        contentValues.put(CONSPECT_TABLE_COLUMN_FIRST_IMAGE_PATH, first_image_path);
        db.update(CONSPECT_TABLE_NAME, contentValues, "id = ?", new String[] {String.valueOf(id)});
        return true;
    }

    public boolean insertPhoto(int id_conspect, int number_in_conspect, String path, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PHOTO_TABLE_COLUMN_ID_CONSPECT, id_conspect);
        contentValues.put(PHOTO_TABLE_COLUMN_NUMBER_IN_CONSPECT, number_in_conspect);
        contentValues.put(PHOTO_TABLE_COLUMN_PATH, path);
        contentValues.put(PHOTO_TABLE_COLUMN_NOTE, note);
        db.insert(PHOTO_TABLE_NAME, null, contentValues);
        return true;
    }

    public int getConspectCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + CONSPECT_TABLE_NAME + ";", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getPhotoCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + PHOTO_TABLE_NAME + ";", null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public ArrayList<Conspect> getAllConspects() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Conspect> conspects = new ArrayList<Conspect>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CONSPECT_TABLE_NAME + ";", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            conspects.add(new Conspect(
                    cursor.getInt(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_N_PHOTOS)),
                    cursor.getString(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_SUBJECT)),
                    cursor.getString(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_ABOUT)),
                    cursor.getString(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_FIRST_IMAGE_PATH))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return conspects;
    }

    public ArrayList<Conspect> getConspectsBySubject(String subject) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Conspect> conspects = new ArrayList<Conspect>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + CONSPECT_TABLE_NAME + " WHERE " + CONSPECT_TABLE_COLUMN_SUBJECT + " = '" + subject + "';", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            conspects.add(new Conspect(
                    cursor.getInt(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_N_PHOTOS)),
                    cursor.getString(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_SUBJECT)),
                    cursor.getString(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_ABOUT)),
                    cursor.getString(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_FIRST_IMAGE_PATH))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return conspects;
    }

    public ArrayList<Photo> getPhotos(int conspect_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Photo> photos = new ArrayList<Photo>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PHOTO_TABLE_NAME + " WHERE " + PHOTO_TABLE_COLUMN_ID_CONSPECT + " = " + conspect_id + ";", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            photos.add(new Photo(
                    cursor.getInt(cursor.getColumnIndex(PHOTO_TABLE_COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndex(PHOTO_TABLE_COLUMN_ID_CONSPECT)),
                    cursor.getInt(cursor.getColumnIndex(PHOTO_TABLE_COLUMN_NUMBER_IN_CONSPECT)),
                    cursor.getString(cursor.getColumnIndex(PHOTO_TABLE_COLUMN_PATH)),
                    cursor.getString(cursor.getColumnIndex(PHOTO_TABLE_COLUMN_NOTE))
            ));
            cursor.moveToNext();
        }
        cursor.close();
        return photos;
    }

    public boolean deleteConspect(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        deletePhotosInConspect(id);
        db.delete(CONSPECT_TABLE_NAME, "id = " + id,null);
        return true;
    }

    public boolean deletePhoto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PHOTO_TABLE_NAME, "id = " + id,null);
        return true;
    }

    public boolean deletePhotosInConspect(int conspect_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(PHOTO_TABLE_NAME, PHOTO_TABLE_COLUMN_ID_CONSPECT + " = " + conspect_id,null);
        return true;
    }

    public int lastInsertedConspectId() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT last_insert_rowid();", null);
        cursor.moveToFirst();
        int id = cursor.getInt(0);
        cursor.close();
        return id;
    }

    public ArrayList<String> getSubjects() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<String> subjects = new ArrayList<String>();
        Cursor cursor = db.rawQuery("SELECT DISTINCT("+ CONSPECT_TABLE_COLUMN_SUBJECT +") FROM " + CONSPECT_TABLE_NAME + ";", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            if (!cursor.getString(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_SUBJECT)).isEmpty()) {
                subjects.add(cursor.getString(cursor.getColumnIndex(CONSPECT_TABLE_COLUMN_SUBJECT)));
            }
            cursor.moveToNext();
        }
        cursor.close();
        return subjects;
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + CONSPECT_TABLE_NAME);
        db.execSQL("DELETE FROM " + PHOTO_TABLE_NAME);
    }
}
