package com.bvl.calendula.ui;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bvl.calendula.AddActivity;
import com.bvl.calendula.DatabaseHelper;
import com.bvl.calendula.ElementAdapter;
import com.bvl.calendula.R;
import com.bvl.calendula.ScrollAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DayFragment extends Fragment {

    RecyclerView recyclerView, scroll;
    FloatingActionButton button;

    DatabaseHelper db_helper;
    ArrayList<String> id, name, date, day_of_week, periodicity, time_start, time_finish, tags, text_note, pic_note, audio_note;

    ElementAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_day, container, false);

        recyclerView = v.findViewById(R.id.recycler);
        button = v.findViewById(R.id.add_button);
        scroll = v.findViewById(R.id.scroll);
        scroll.setLayoutManager(new LinearLayoutManager(DayFragment.this.getActivity(), RecyclerView.HORIZONTAL, false));
        scroll.setAdapter(new ScrollAdapter());
        scroll.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DayFragment.this.getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });

        db_helper = new DatabaseHelper(DayFragment.this.getActivity());
        id = new ArrayList<>();
        name = new ArrayList<>();
        date = new ArrayList<>();
        day_of_week = new ArrayList<>();
        periodicity = new ArrayList<>();
        time_start = new ArrayList<>();
        time_finish = new ArrayList<>();
        tags = new ArrayList<>();
        text_note = new ArrayList<>();
        pic_note = new ArrayList<>();
        audio_note = new ArrayList<>();

        toArrays();

        adapter = new ElementAdapter(DayFragment.this.getActivity(),
                id, name, date, day_of_week, periodicity, time_start,
                time_finish, tags, text_note, pic_note, audio_note);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DayFragment.this.getActivity()));

        return v;
    }

    void toArrays(){
        Cursor cursor = db_helper.read();
        if(cursor.getCount() != 0)
        {
            while(cursor.moveToNext())
            {
                id.add(cursor.getString(0));
                name.add(cursor.getString(1));
                date.add(cursor.getString(2));
                day_of_week.add(cursor.getString(3));
                periodicity.add(cursor.getString(4));
                time_start.add(cursor.getString(5));
                time_finish.add(cursor.getString(6));
                tags.add(cursor.getString(7));
                text_note.add(cursor.getString(8));
                pic_note.add(cursor.getString(9));
                audio_note.add(cursor.getString(10));
            }
        }
    }
}
