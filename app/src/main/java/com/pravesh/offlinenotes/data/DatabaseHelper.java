package com.pravesh.offlinenotes.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.pravesh.offlinenotes.model.Note;
import com.pravesh.offlinenotes.util.Database;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private final Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, Database.DATABASE_NAME, null, Database.DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + Database.TABLE_NAME + "(" + Database.COLUMN_ID + " INTEGER PRIMARY KEY," + Database.COLUMN_TITLE + " TEXT," +
                Database.COLUMN_SUBTITLE + " TEXT," + Database.COLUMN_CONTENT + " TEXT);";
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + Database.TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);

    }

    //CRUD OPERATIONS
    public void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Database.COLUMN_TITLE, note.getTitle());
        cv.put(Database.COLUMN_SUBTITLE, note.getSubtitle());
        cv.put(Database.COLUMN_CONTENT, note.getContent());
        db.insert(Database.TABLE_NAME, null, cv);
        db.close();
    }


    public List<Note> getAllNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> noteList = new ArrayList<>();
        String getAll = "SELECT * FROM " + Database.TABLE_NAME;
        Cursor cursor = db.rawQuery(getAll, null);


        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(cursor.getColumnIndex(Database.COLUMN_TITLE));
                String subtitle = cursor.getString(cursor.getColumnIndex(Database.COLUMN_SUBTITLE));
                String content = cursor.getString(cursor.getColumnIndex(Database.COLUMN_CONTENT));
                noteList.add(new Note(title, subtitle, content));


            } while (cursor.moveToNext());
        }
        return noteList;
    }

    public int updateNote(Note note) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Database.COLUMN_TITLE, note.getTitle());
        contentValues.put(Database.COLUMN_SUBTITLE, note.getSubtitle());
        contentValues.put(Database.COLUMN_CONTENT, note.getContent());
        return db.update(Database.TABLE_NAME, contentValues,
                Database.COLUMN_ID + "=? ", new String[]{String.valueOf(note.getId())});
    }

    public void deleteNote(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Database.TABLE_NAME, Database.COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        db.close();
    }
}


