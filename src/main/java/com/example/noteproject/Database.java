package com.example.noteproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "notedb";
    private static final String DATABASE_TABLE = "notestable";


    //Columns
    private static final String KEY_ID = "id";
    private static final String KEY_SUBJECT = "subject";
    private static final String KEY_BODY = "body";


    Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE TABLE name (id INT PRIMARY KEY, title TEXT, content text);
        String query = "CREATE TABLE " +DATABASE_TABLE+" ("+
                KEY_ID+" INTEGER PRIMARY KEY,"+
                KEY_SUBJECT+" TEXT,"+
                KEY_BODY+" TEXT"
                +" )";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            return;
        }
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
        onCreate(db);
    }

    public long addNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(KEY_SUBJECT,note.getSubject());
        c.put(KEY_BODY,note.getBody());

        long ID = db.insert(DATABASE_TABLE, null, c);
        Log.d("Inserted", "ID -> " + ID);
        return ID;
    }

    public Note getNote(long id){
        //select * from databaseTable where id=1
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DATABASE_TABLE,new String[]{KEY_ID,KEY_SUBJECT,KEY_BODY},KEY_ID+"=?",
                new String[]{String.valueOf(id)},null,null,null);

        if(cursor != null){
            cursor.moveToFirst();
            //0 = ID column, 1 = Subject column, 2 = Body column

        }
        Note note = new Note(cursor.getLong(0),cursor.getString(1),cursor.getString(2));

        return note;
    }

    public List<Note> getNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> allNotes = new ArrayList<>();
        // select * from databaseTable

        String query = "SELECT * FROM "+DATABASE_TABLE;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setID(cursor.getLong(0));
                note.setSubject(cursor.getString(1));
                note.setBody(cursor.getString(2));

                allNotes.add(note);


            }while (cursor.moveToNext());
        }

        return allNotes;
    }

    public int editNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        Log.d("Edited", "Edited Subject ->"+ note.getSubject()+ "\n ID -> "+note.getID());
        c.put(KEY_SUBJECT,note.getSubject());
        c.put(KEY_BODY,note.getBody());
        return db.update(DATABASE_TABLE,c,KEY_ID+"=?",new String[]{String.valueOf(note.getID())});
    }


    void deleteNote(long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, KEY_ID+"=?",new String[]{String.valueOf(id)});
        db.close();

    }

}
