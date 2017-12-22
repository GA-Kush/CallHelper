package com.greenapex.callhelper.Adpter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.greenapex.callhelper.Activity.NoteCreate;
import com.greenapex.callhelper.Activity.ReminderCreate;
import com.greenapex.callhelper.Model.conctactPojo;
import com.greenapex.callhelper.Model.contactNote;
import com.greenapex.callhelper.R;
import com.greenapex.callhelper.dbCallHelper.MyDBHandler;

import java.util.ArrayList;
import java.util.List;

import static com.greenapex.callhelper.Activity.ContactList.PREFS_NAME;

/**
 * Created by pc01 on 24/11/17.
 */

public class adpterContact extends RecyclerView.Adapter<adpterContact.RecyclerViewHolder> {

    Context context;
    List<conctactPojo> arrayContact;
    LayoutInflater inflater;
    MyDBHandler dbHandler;
    adpterNoteReminder adpter;
    SharedPreferences preferences;
    String number, name;

    public adpterContact(Context context, List<conctactPojo> arrayContact) {
        this.arrayContact = arrayContact;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_contactrow, null, false);
        RecyclerViewHolder holder = new adpterContact.RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder viewHolder, int position) {

        viewHolder.txtContactName.setText(arrayContact.get(position).getContactName());
        viewHolder.txtContactNumber.setText(arrayContact.get(position).getContactNumber());
        number = arrayContact.get(position).getContactNumber();

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txt_logInfoName, txt_logInfoNumber, txt_addNote, txt_setReminder;
                Button btn_call;
                RecyclerView recyclerView;
                dbHandler = new MyDBHandler(context);
                preferences = context.getSharedPreferences(PREFS_NAME, 0);
                ArrayList<conctactPojo> mItems = new ArrayList<>();

                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.app_loginfo, null);

                dialogBuilder.setView(dialogView);

                final AlertDialog a = dialogBuilder.create();

                txt_logInfoName = (TextView) dialogView.findViewById(R.id.txt_logInfoName);
                txt_logInfoNumber = (TextView) dialogView.findViewById(R.id.txt_logInfoNumber);
                txt_addNote = (TextView) dialogView.findViewById(R.id.txtaddNote);
                txt_setReminder = (TextView) dialogView.findViewById(R.id.txtsetReminder);
                btn_call = (Button) dialogView.findViewById(R.id.btnCall);
                txt_logInfoName.setText(viewHolder.txtContactName.getText());
                txt_logInfoNumber.setText(viewHolder.txtContactNumber.getText());
                name = txt_logInfoName.getText().toString();
                number = txt_logInfoNumber.getText().toString();
                txt_addNote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, NoteCreate.class);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("contactNumber");
                        editor.remove("contactName");
                        editor.remove("contactNumber1");
                        editor.remove("contactName1");
                        editor.commit();
                        intent.putExtra("popup", "popup");
                        intent.putExtra("contactName", name);
                        intent.putExtra("contactNumber", number);
                        context.startActivity(intent);

                    }
                });
                txt_setReminder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, ReminderCreate.class);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.remove("contactNumber");
                        editor.remove("contactName");
                        editor.remove("contactNumber1");
                        editor.remove("contactName1");
                        editor.commit();
                        intent.putExtra("popup", "popup");
                        intent.putExtra("contactName", name);
                        intent.putExtra("contactNumber", number);
                        context.startActivity(intent);
                    }
                });

                recyclerView = (RecyclerView) dialogView.findViewById(R.id.log_contactInfo);
                btn_call.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                        context.startActivity(i);
                    }
                });
                List<contactNote> list = dbHandler.noteReminder(number);

                if (list.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                }

                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                adpter = new adpterNoteReminder(context, list);
                recyclerView.setAdapter(adpter);
                adpter.notifyDataSetChanged();

                Button btnCancel = (Button) dialogView.findViewById(R.id.btnInfoClose);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        a.cancel();
                    }
                });
                a.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return arrayContact.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView txtContactName, txtContactNumber;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            txtContactName = (TextView) itemView.findViewById(R.id.txt_contactName);
            txtContactNumber = (TextView) itemView.findViewById(R.id.txt_contactNumber);

        }
    }
}
