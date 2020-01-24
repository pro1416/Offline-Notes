package com.pravesh.offlinenotes.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.pravesh.offlinenotes.R;
import com.pravesh.offlinenotes.adapter.MyRecyclerAdapter;
import com.pravesh.offlinenotes.data.DatabaseHelper;
import com.pravesh.offlinenotes.model.Note;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView noNotes;
    AlertDialog dialog;
    AlertDialog.Builder builder;
    EditText edtTitle, edtSubtitle, edtContent;
    Button add;
    DatabaseHelper databaseHelper;
    FloatingActionButton btnAddNote;
    List<Note> recyclerNotes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recyclerView);
        btnAddNote = findViewById(R.id.addNote);
        noNotes = findViewById(R.id.noNotes);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseHelper = new DatabaseHelper(this);
        showRecyclerViewOrNot();


        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddNote();
            }
        });
    }

    public void showRecyclerViewOrNot() {
        recyclerNotes = databaseHelper.getAllNotes();
        if (recyclerNotes.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            noNotes.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            noNotes.setVisibility(View.GONE);
            MyRecyclerAdapter adapter = new MyRecyclerAdapter(MainActivity.this, recyclerNotes);
            recyclerView.setAdapter(adapter);

        }
    }

    public void createAddNote() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.add_note_dialog, null);
        edtTitle = view.findViewById(R.id.add_title);
        edtSubtitle = view.findViewById(R.id.add_subtitle);
        edtContent = view.findViewById(R.id.add_content);
        add = view.findViewById(R.id.btnAdd);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = edtTitle.getText().toString().trim();
                String subtitle = edtSubtitle.getText().toString().trim();
                String content = edtContent.getText().toString().trim();
                if (!title.equals("") && !content.equals("") && !subtitle.equals("")) {
                    Note note = new Note(title, subtitle, content);
                    databaseHelper.addNote(note);
                    Snackbar.make(v, "Added", Snackbar.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            showRecyclerViewOrNot();


                        }
                    }, 1000);
                } else {
                    Snackbar.make(v, "Empty fields not allowed", Snackbar.LENGTH_SHORT).show();
                }

            }

        });
        builder.setView(view);
        dialog = builder.create();
        dialog.show();

    }
}
