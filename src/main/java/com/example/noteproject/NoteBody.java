package com.example.noteproject;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class NoteBody extends AppCompatActivity {

    TextView noteDetail;
    Database db;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_body);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        noteDetail = findViewById(R.id.noteDetail);

        Intent i = getIntent();
        Long id = i.getLongExtra("ID", 0);

        db = new Database(this);
        note = db.getNote(id);
        getSupportActionBar().setTitle(note.getSubject());
        noteDetail.setText(note.getBody());


        Toast.makeText(this, "Subject -> " + note.getSubject(), Toast.LENGTH_SHORT).show();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.deleteNote(note.getID());
                Toast.makeText(getApplicationContext(), "Note deleted", Toast.LENGTH_SHORT).show();
                startActivity(new Intent (getApplicationContext(),MainActivity.class));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.edit) {
           //send user to edit activity
            Toast.makeText(this,"Editing note", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,edit_note.class);
            i.putExtra("ID",note.getID());
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

}
