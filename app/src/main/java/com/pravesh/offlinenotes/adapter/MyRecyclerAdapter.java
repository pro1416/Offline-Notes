package com.pravesh.offlinenotes.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.pravesh.offlinenotes.R;
import com.pravesh.offlinenotes.data.DatabaseHelper;
import com.pravesh.offlinenotes.model.Note;

import java.util.List;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<Note> noteList;
    private AlertDialog.Builder builder;
    private LayoutInflater inflater;
    private AlertDialog dialog;

    // creating constructor
    public MyRecyclerAdapter(Context context, List<Note> noteList) {
        this.context = context;
        this.noteList = noteList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_note, parent, false);
        return new MyViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerAdapter.MyViewHolder holder, int position) {
        Note note = noteList.get(position);
        holder.txt_title.setText(note.getTitle());
        holder.txt_subTitle.setText(note.getSubtitle());
        holder.txt_content.setText(note.getContent());


    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_title, txt_subTitle, txt_content;
        ImageButton btnUpdate, delete;

        MyViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;
// getting id of each view inside the view
            txt_title = itemView.findViewById(R.id.txt_title);
            txt_subTitle = itemView.findViewById(R.id.subTitle);
            txt_content = itemView.findViewById(R.id.txt_content);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            delete = itemView.findViewById(R.id.btnDelete);
// registering listeners
            btnUpdate.setOnClickListener(this);
            delete.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Note note = noteList.get(position);
            switch (v.getId()) {
                case R.id.btnUpdate:
                    updateNote(note);
                    break;
                case R.id.btnDelete:
                    deleteNote(note.getId());
                    break;
            }
        }

        private void updateNote(final Note note) {
            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.update_dialog, null);
// getting data from previous note and setting to dialog
            Button update = view.findViewById(R.id.btnChange);
            final EditText txtTitle = view.findViewById(R.id.change_title);
            final EditText txtSubtitle = view.findViewById(R.id.change_subtitle);
            final EditText txtContent = view.findViewById(R.id.change_content);

            txtTitle.setText(note.getTitle());
            txtSubtitle.setText(note.getSubtitle());
            txtContent.setText(note.getContent());

            builder.setView(view);
            dialog = builder.create();
            dialog.show();


            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//updation occurs here
                    DatabaseHelper db = new DatabaseHelper(context);
                    String newTitle = txtTitle.getText().toString().trim();
                    String newSubtitle = txtSubtitle.getText().toString().trim();
                    String new_content = txtContent.getText().toString().trim();

                    if (!newTitle.equals("") && !newSubtitle.equals("") && !new_content.equals("")) {
                        note.setTitle(newTitle);
                        note.setSubtitle(newSubtitle);
                        note.setContent(new_content);

                        db.updateNote(note);

                        Snackbar.make(v, "Updated", Snackbar.LENGTH_SHORT).show();
                        // in order to show the SnackBar
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();


                            }
                        }, 1000);
                        notifyItemChanged(getAdapterPosition(), note);
                    } else {
                        Snackbar.make(v, "Fields Empty!", Snackbar.LENGTH_SHORT).show();
                    }
                }
            });


        }

        private void deleteNote(final int id) {
            // creating the dialog
            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.delete_dialog, null);

            Button yes = view.findViewById(R.id.btnConfirm);
            Button no = view.findViewById(R.id.btnCancel);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHelper db = new DatabaseHelper(context);

                    db.deleteNote(id);
// deletion occurs here
                    noteList.remove(getAdapterPosition());
                    //for recycler view to know that object has been deleted
                    notifyItemRemoved(getAdapterPosition());
                    dialog.dismiss();

                }
            });

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });


        }
    }
}
