package com.greenapex.callhelper.Adpter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.greenapex.callhelper.Activity.NoteCreate;
import com.greenapex.callhelper.Model.contactNote;
import com.greenapex.callhelper.Model.contactReminder;
import com.greenapex.callhelper.R;
import com.greenapex.callhelper.dbCallHelper.MyDBHandler;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by pc01 on 5/12/17.
 */

public class adpterReminder extends RecyclerView.Adapter<adpterReminder.RecyclerViewHolder> {
    Context context;
    List<contactNote> arrayReminder;
    LayoutInflater inflater;
    MyDBHandler dbHandler;
    int Id;
    String contactName, cNote, contactNumber,type;

    public adpterReminder(Context context, List<contactNote> arrayReminder) {
        this.arrayReminder = arrayReminder;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_contact_reminder, null, false);
        RecyclerViewHolder holder = new RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
        dbHandler = new MyDBHandler(context);
        Id = arrayReminder.get(position).getContactId();
        contactName = arrayReminder.get(position).getContactName();
        cNote = arrayReminder.get(position).getContactNote();
        contactNumber = arrayReminder.get(position).getContactNumber();
        type=arrayReminder.get(position).getType();
            holder.txtReminderName.setText(contactName);
            holder.txtReminderNote.setText(cNote);
            holder.txtReminderCreateDateTime.setText("created: "+arrayReminder.get(position).getContactNoteDateTime());
            holder.txtReminderDateTime.setText(arrayReminder.get(position).getDate() + " " + arrayReminder.get(position).getTime());

            holder.btnReminderSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final contactNote reminder = arrayReminder.get(position);

                    final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                    final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View dialogView = inflater.inflate(R.layout.app_reminder_edit, null);
                    dialogBuilder.setView(dialogView);
                    final AlertDialog editDelete = dialogBuilder.create();
                    editDelete.show();
                    RelativeLayout relativeLayoutDelete = (RelativeLayout) dialogView.findViewById(R.id.relative_bottom);

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
                                dbHandler.deleteNote(reminder.getContactId());
                                delete.cancel();
                                }
                            });
                            Button btnClose=(Button)dialogView.findViewById(R.id.btnClose);
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
        return arrayReminder.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtReminderName, txtReminderNote, txtReminderCreateDateTime, txtReminderDateTime;
        Button btnReminderSetting;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            btnReminderSetting = (Button) itemView.findViewById(R.id.btnReminderSetting);
            txtReminderName = (TextView) itemView.findViewById(R.id.txtReminderName);
            txtReminderNote = (TextView) itemView.findViewById(R.id.txtReminderNote);
            txtReminderCreateDateTime = (TextView) itemView.findViewById(R.id.txtReminderCreateDateTime);
            txtReminderDateTime = (TextView) itemView.findViewById(R.id.txtReminderDateTime);
        }
    }
}
