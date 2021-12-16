package com.bvl.calendula.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bvl.calendula.DatabaseHelper;
import com.bvl.calendula.ElementAdapterMonth;
import com.bvl.calendula.R;
import com.bvl.calendula.ScrollAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class MonthFragment extends Fragment implements ScrollAdapter.OnDateClickListener {

    RecyclerView scroll;
    GridView gridView;

    DatabaseHelper db_helper;
    ArrayList<String> id, name, date, day_repeat, week_repeat, time_start, time_finish, tags, text_note, pic_note, audio_note, done;
    ElementAdapterMonth adapter;

    public static MonthFragment newInstance(Calendar date) {

        Bundle args = new Bundle();
        args.putLong("date", date.getTime().getTime()); //getTime to calendar->date and getTime to date->milliseconds
        MonthFragment fragment = new MonthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_month, container, false);

        scroll = v.findViewById(R.id.scroll);
        scroll.setLayoutManager(new LinearLayoutManager(MonthFragment.this.getActivity(), RecyclerView.HORIZONTAL, false));
        scroll.setAdapter(new ScrollAdapter(this, "MONTH"));
        scroll.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);

        gridView = v.findViewById(R.id.monthTable);

        db_helper = new DatabaseHelper(MonthFragment.this.getActivity());
        id = new ArrayList<>();
        name = new ArrayList<>();
        date = new ArrayList<>();
        day_repeat = new ArrayList<>();
        week_repeat = new ArrayList<>();
        time_start = new ArrayList<>();
        time_finish = new ArrayList<>();
        tags = new ArrayList<>();
        text_note = new ArrayList<>();
        pic_note = new ArrayList<>();
        audio_note = new ArrayList<>();
        done = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(getArguments().getLong("date"));
        toArrays(calendar);;

        adapter = new ElementAdapterMonth(MonthFragment.this.getActivity(),
                id, name, date, day_repeat, week_repeat, time_start,
                time_finish, tags, text_note, pic_note, audio_note, done);
        gridView.setAdapter(adapter);

        return v;
    }

    void toArrays(Calendar desiredDate){
        Cursor cursor = db_helper.read(desiredDate);
        if(cursor.getCount() != 0)
        {
            while(cursor.moveToNext())
            {
                this.id.add(cursor.getString(0));
                this.name.add(cursor.getString(1));
                this.date.add(cursor.getString(2));
                this.day_repeat.add(cursor.getString(3));
                this.week_repeat.add(cursor.getString(4));
                this.time_start.add(cursor.getString(5));
                this.time_finish.add(cursor.getString(6));
                this.tags.add(cursor.getString(7));
                this.text_note.add(cursor.getString(8));
                this.pic_note.add(cursor.getString(9));
                this.audio_note.add(cursor.getString(10));
                this.done.add(cursor.getString(11));
            }
        }
    }

    @Override
    public void onDateClick(int position) {
        this.id.clear();
        this.name.clear();
        this.date.clear();
        this.day_repeat.clear();
        this.week_repeat.clear();
        this.time_start.clear();
        this.time_finish.clear();
        this.tags.clear();
        this.text_note.clear();
        this.pic_note.clear();
        this.audio_note.clear();
        this.done.clear();
        int dif = Integer.MAX_VALUE / 2 - position;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -dif);
        toArrays(calendar);
        adapter.notifyDataSetChanged();
    }

}
