package com.bvl.calendula.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bvl.calendula.AddBottomSheet;
import com.bvl.calendula.DatabaseHelper;
import com.bvl.calendula.ElementAdapterDay;
import com.bvl.calendula.R;
import com.bvl.calendula.ScrollAdapter;

import java.util.ArrayList;
import java.util.Calendar;

public class DayFragment extends Fragment implements ScrollAdapter.OnDateClickListener {

    RecyclerView recyclerView, scroll;
    CardView cardViewButton;

    DatabaseHelper db_helper;
    ArrayList<String> id, name, date, day_repeat, week_repeat, time_start, time_finish, tags, text_note, pic_note, audio_note;
    ElementAdapterDay adapter;

    Calendar cDate;

    public DayFragment(Calendar newDate)
    {
        this.cDate = newDate;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_day, container, false);

        recyclerView = v.findViewById(R.id.recycler);
        cardViewButton = v.findViewById(R.id.add_button);
        scroll = v.findViewById(R.id.scroll);
        scroll.setLayoutManager(new LinearLayoutManager(DayFragment.this.getActivity(), RecyclerView.HORIZONTAL, false));
        scroll.setAdapter(new ScrollAdapter(this, "DAY"));
        scroll.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);

        cardViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddBottomSheet addBottomSheet = new AddBottomSheet();
                addBottomSheet.show(getActivity().getSupportFragmentManager(), "TAG");
            }
        });

        db_helper = new DatabaseHelper(DayFragment.this.getActivity());
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

        toArrays(cDate);

        adapter = new ElementAdapterDay(DayFragment.this.getActivity(),
                id, name, date, day_repeat, week_repeat, time_start,
                time_finish, tags, text_note, pic_note, audio_note);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(DayFragment.this.getActivity()));

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
        int dif = Integer.MAX_VALUE / 2 - position;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -dif);
        toArrays(calendar);
        adapter.notifyDataSetChanged();
    }
}
