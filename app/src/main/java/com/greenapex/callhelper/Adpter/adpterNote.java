package com.greenapex.callhelper.Adpter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greenapex.callhelper.Activity.NoteCreate;
import com.greenapex.callhelper.Model.contactNote;
import com.greenapex.callhelper.R;
import com.greenapex.callhelper.dbCallHelper.MyDBHandler;

import java.util.List;

import static com.greenapex.callhelper.Activity.ContactList.PREFS_NAME;

/**
 * Created by pc01 on 30/11/17.
 */

public class adpterNote extends RecyclerView.Adapter<adpterNote.RecyclerViewHolder> {

    Context context;
    List<contactNote> arrayNote;
    LayoutInflater inflater;
    MyDBHandler dbHandler;
    SharedPreferences preferences;
    int Id;
    String contactName, contactNote, contactNumber, type;

    public adpterNote(Context context, List<contactNote> arrayNote) {
        this.arrayNote = arrayNote;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_contact_note, null, false);
        adpterNote.RecyclerViewHolder holder = new adpterNote.RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
        dbHandler = new MyDBHandler(context);
        Id = arrayNote.get(position).getContactId();
        contactName = arrayNote.get(position).getContactName();
        contactNote = arrayNote.get(position).getContactNote();
        contactNumber = arrayNote.get(position).getContactNumber();
        type = arrayNote.get(position).getType();


        holder.txtContactName.setText(contactName);
        holder.txtContactNote.setText(contactNote);
        holder.txtContactDateTime.setText("created: " + arrayNote.get(position).getContactNoteDateTime());
        holder.btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final contactNote note = arrayNote.get(position);

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.app_edit, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog editDelete = dialogBuilder.create();
                editDelete.show();

                RelativeLayout relativeLayoutEdit = (RelativeLayout) dialogView.findViewById(R.id.relative_top);
                RelativeLayout relativeLayoutDelete = (RelativeLayout) dialogView.findViewById(R.id.relative_bottom);

                relativeLayoutEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        editDelete.cancel();
                        //clear sharedprefrece
                        preferences = context.getSharedPreferences(PREFS_NAME, 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("contactNumber");
                        editor.remove("contactName");
                        editor.remove("contactNumber1");
                        editor.remove("contactName1");
                        editor.commit();

                        Intent intent = new Intent(context, NoteCreate.class);
                        intent.putExtra("note", "editNote");
                        intent.putExtra("contactId", note.getContactId());
                        context.startActivity(intent);
                    }
                });
                relativeLayoutDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        editDelete.cancel();
                        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View dialogView = inflater.inflate(R.layout.app_delete, null);
                        dialogBuilder.setView(dialogView);
                        final AlertDialog delete = dialogBuilder.create();
                        delete.show();
                        Button btnDelete = (Button) dialogView.findViewById(R.id.btnDelete);
                        btnDelete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dbHandler.deleteNote(note.getContactId());
                                delete.cancel();
                            }
                        });
                        Button btnClose = (Button) dialogView.findViewById(R.id.btnClose);
                        btnClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                delete.cancel();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayNote.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtContactName, txtContactNote, txtContactDateTime;
        Button btnSetting;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            btnSetting = (Button) itemView.findViewById(R.id.btnNoteSetting);
            txtContactName = (TextView) itemView.findViewById(R.id.txtNoteName);
            txtContactNote = (TextView) itemView.findViewById(R.id.txtNote);
            txtContactDateTime = (TextView) itemView.findViewById(R.id.txtNoteDateTime);
        }
    }
}
