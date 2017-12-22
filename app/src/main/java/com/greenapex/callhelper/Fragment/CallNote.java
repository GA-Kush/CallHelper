package com.greenapex.callhelper.Fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.greenapex.callhelper.Activity.NoteCreate;
import com.greenapex.callhelper.Adpter.adpterNote;
import com.greenapex.callhelper.Model.contactNote;
import com.greenapex.callhelper.R;
import com.greenapex.callhelper.dbCallHelper.MyDBHandler;

import java.util.List;

import static com.greenapex.callhelper.Activity.ContactList.PREFS_NAME;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallNote extends Fragment {

    FloatingActionButton fabNote;
    RecyclerView recyclerNote;
    SharedPreferences preferences;
    MyDBHandler dbHandler;
    adpterNote adpter;


    public static CallNote newInstance() {
        CallNote fragment = new CallNote();
        return fragment;
    }

    public CallNote() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_note, container, false);
        fabNote = (FloatingActionButton) view.findViewById(R.id.fabNote);
        recyclerNote = (RecyclerView) view.findViewById(R.id.recyclerNote);
        dbHandler = new MyDBHandler(getContext());
        fabNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences = getContext().getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("contactNumber");
                editor.remove("contactName");
                editor.remove("contactNumber1");
                editor.remove("contactName1");
                editor.commit();
                startActivity(new Intent(getActivity(), NoteCreate.class));
            }
        });
        Note();
        return view;
    }

    public void Note() {


        List<contactNote> list = dbHandler.selectAll("note");
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerNote.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        adpter = new adpterNote(getContext(), list);
        recyclerNote.setAdapter(adpter);
        adpter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        Note();
    }

}
