package com.example.noteproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class edit_note extends AppCompatActivity {

    Toolbar toolbar;
    EditText noteSubject, noteBody;
    Database db;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Intent i = getIntent();
        Long id = i.getLongExtra("ID",0);
        Database db = new Database(this);
        note = db.getNote(id);



        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(note.getSubject());

        noteSubject = findViewById(R.id.noteSubject);
        noteBody = findViewById(R.id.noteBody);

        noteSubject.setText(note.getSubject());
        noteBody.setText(note.getBody());

        noteSubject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.delete) {
            Toast.makeText(this, "Note Discarded", Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
        if(item.getItemId() == R.id.save) {
           if (noteSubject.getText().length() != 0){
               note.setSubject(noteSubject.getText().toString());
               note.setBody(noteBody.getText().toString());
               int id = db.editNote(note);
               if(id == note.getID()){
                   Toast.makeText(this,"Note Updated",Toast.LENGTH_SHORT).show();
               }else {
                   Toast.makeText(this,"Error Updating",Toast.LENGTH_SHORT).show();
               }

               Intent i = new Intent(getApplicationContext(),NoteBody.class);
               i.putExtra("ID",note.getID());
               startActivity(i);
           }
        }
        return super.onOptionsItemSelected(item);
    }

}
