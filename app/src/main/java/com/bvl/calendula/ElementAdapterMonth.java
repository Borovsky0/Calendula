package com.bvl.calendula;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ElementAdapterMonth extends BaseAdapter {

    Context context;
    ArrayList<String> id, name, date, day_repeat, week_repeat, time_start, time_finish, tags, text_note, pic_note, audio_note, done;
    LayoutInflater inflater;
    Calendar calendar;

    public ElementAdapterMonth(Context context, ArrayList<String> id, ArrayList<String> name, ArrayList<String> date, ArrayList<String> day_repeat,
                          ArrayList<String> week_repeat, ArrayList<String> time_start, ArrayList<String> time_finish,
                          ArrayList<String> tags, ArrayList<String> text_note, ArrayList<String> pic_note, ArrayList<String> audio_note, ArrayList<String> done){
        this.context = context;
        this.id = id;
        this.name = name;
        this.date = date;
        this.day_repeat = day_repeat;
        this.week_repeat = week_repeat;
        this.time_start = time_start;
        this.time_finish = time_finish;
        this.tags = tags;
        this.text_note = text_note;
        this.pic_note = pic_note;
        this.audio_note = audio_note;
        this.done = done;

        this.calendar = Calendar.getInstance();
    }


    @Override
    public int getCount() {
        return this.calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //inflater = LayoutInflater.from(context);
        }
        if(view == null){
            view = inflater.inflate(R.layout.element_month, null);
        }

        TextView textView = view.findViewById(R.id.day);
        textView.setText(Integer.toString(position+1));
        return view;
    }
    
    public void calendarChangeCurrentDate(Calendar calendar) {
        this.calendar = calendar;
    }
}
