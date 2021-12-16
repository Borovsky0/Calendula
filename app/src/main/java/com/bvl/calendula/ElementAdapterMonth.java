package com.bvl.calendula;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ElementAdapterMonth extends BaseAdapter {

    Context context;
    ArrayList id, name, date, day_repeat, week_repeat, time_start, time_finish, tags, text_note, pic_note, audio_note, done;
    LayoutInflater inflater;
    Calendar calendar;

    public ElementAdapterMonth(Context context, ArrayList id, ArrayList name, ArrayList date, ArrayList day_repeat,
                          ArrayList week_repeat, ArrayList time_start, ArrayList time_finish,
                          ArrayList tags, ArrayList text_note, ArrayList pic_note, ArrayList audio_note, ArrayList done){
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

        calendar = Calendar.getInstance();
        try { calendar.setTime(new SimpleDateFormat("yyyy/MM/dd").parse(String.valueOf(time_start.get(0)))); }
        catch (ParseException e) { e.printStackTrace(); }
    }


    @Override
    public int getCount() {
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
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
}
