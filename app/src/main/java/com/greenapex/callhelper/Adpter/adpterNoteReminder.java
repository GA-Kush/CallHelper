package com.greenapex.callhelper.Adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.greenapex.callhelper.Model.contactNote;
import com.greenapex.callhelper.R;

import java.util.List;

/**
 * Created by pc01 on 8/12/17.
 */

public class adpterNoteReminder extends RecyclerView.Adapter<adpterNoteReminder.RecyclerViewHolder> {

    Context context;
    List<contactNote> arrayReminder;
    LayoutInflater inflater;
    int Id;
    String contactName, cNote, contactNumber, type;

    public adpterNoteReminder(Context context, List<contactNote> arrayReminder) {
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
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Id = arrayReminder.get(position).getContactId();
        contactName = arrayReminder.get(position).getContactName();
        cNote = arrayReminder.get(position).getContactNote();
        contactNumber = arrayReminder.get(position).getContactNumber();
        type = arrayReminder.get(position).getType();
        holder.txtReminderName.setText(contactName);
        holder.txtReminderNote.setText(cNote);
        holder.txtReminderCreateDateTime.setText("created: " + arrayReminder.get(position).getContactNoteDateTime());
        if (arrayReminder.get(position).getDate() == null && arrayReminder.get(position).getTime() == null) {
            holder.img.setVisibility(View.GONE);
            holder.txtReminderDateTime.setVisibility(View.GONE);
        }
        holder.txtReminderDateTime.setText(arrayReminder.get(position).getDate() + " " + arrayReminder.get(position).getTime());
    }

    @Override
    public int getItemCount() {
        return arrayReminder.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtReminderName, txtReminderNote, txtReminderCreateDateTime, txtReminderDateTime;
        Button btnReminderSetting;
        ImageView img;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            btnReminderSetting = (Button) itemView.findViewById(R.id.btnReminderSetting);
            txtReminderName = (TextView) itemView.findViewById(R.id.txtReminderName);
            txtReminderNote = (TextView) itemView.findViewById(R.id.txtReminderNote);
            txtReminderCreateDateTime = (TextView) itemView.findViewById(R.id.txtReminderCreateDateTime);
            txtReminderDateTime = (TextView) itemView.findViewById(R.id.txtReminderDateTime);
        }
    }

}
