package com.greenapex.callhelper.Adpter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.greenapex.callhelper.Model.conctactPojo;
import com.greenapex.callhelper.R;

import java.util.List;

import static com.greenapex.callhelper.Activity.ContactList.PREFS_NAME;

/**
 * Created by pc01 on 24/11/17.
 */

public class adpterContactSelect extends RecyclerView.Adapter<adpterContactSelect.RecyclerViewHolder> {

    Context context;
    List<conctactPojo> arrayContact;
    LayoutInflater inflater;
    SharedPreferences preferences;

    public adpterContactSelect(Context context, List<conctactPojo> arrayContact) {
        this.arrayContact = arrayContact;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.app_contactrow, null, false);
        RecyclerViewHolder holder = new adpterContactSelect.RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder viewHolder, final int position) {
        final conctactPojo conctactPojo = arrayContact.get(position);
        viewHolder.txtContactName.setText(arrayContact.get(position).getContactName());
        viewHolder.txtContactNumber.setText(arrayContact.get(position).getContactNumber());

        if (conctactPojo.isSelected()) {
            viewHolder.itemView.setBackgroundColor(Color.GREEN);
            String number = arrayContact.get(position).getContactNumber();
            String name = arrayContact.get(position).getContactName();

            preferences = context.getSharedPreferences(PREFS_NAME, 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("contactNumber", number);
            editor.putString("contactName", name);
            editor.commit();


        }else {
            viewHolder.itemView.setBackgroundColor(Color.WHITE);

        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for (conctactPojo conctactPojo : arrayContact) {
                    conctactPojo.setSelected(false);
                }
                arrayContact.get(position).setSelected(true);
                notifyDataSetChanged();

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
