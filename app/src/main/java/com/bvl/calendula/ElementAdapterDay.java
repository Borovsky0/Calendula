package com.bvl.calendula;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ElementAdapterDay extends RecyclerView.Adapter<ElementAdapterDay.ViewHolder> {

    Context context;
    ArrayList id, name, date, day_repeat, week_repeat, time_start, time_finish, tags, text_note, pic_note, audio_note;

    public ElementAdapterDay(Context context, ArrayList id, ArrayList name, ArrayList date, ArrayList day_repeat,
                             ArrayList week_repeat, ArrayList time_start, ArrayList time_finish,
                             ArrayList tags, ArrayList text_note, ArrayList pic_note, ArrayList audio_note){
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
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.element_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.tags.removeAllViews();

        String[] tagListId = String.valueOf(tags.get(position)).split(",");
        String[] tagListNames = context.getResources().getStringArray(R.array.tag_names);
        String[] tagListColors = context.getResources().getStringArray(R.array.tag_colors);

        holder.nameTxt.setText(String.valueOf(name.get(position)));

        if(!time_start.get(position).equals("NULL")){
        holder.time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
            holder.time.setText(String.valueOf(time_finish.get(position)).equals("NULL") ? String.valueOf(time_start.get(position))
                : String.valueOf(time_start.get(position)) + " - " + String.valueOf(time_finish.get(position)));}


        String[] emptyTags = {"-1", "-1", "-1"};
        if(!tagListId.equals(emptyTags)) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    4, context.getResources().getDisplayMetrics()),0,0); //convert pixels to dp
            holder.tags.setLayoutParams(params);
        }

        for (int i = 0; i < 3; i++)
        {
            if(!tagListId[i].equals("-1"))
            {
                TextView tag = new TextView(context);

                Drawable drawable = context.getResources().getDrawable(R.drawable.rounded_corner_text);
                drawable.setColorFilter(Color.parseColor(tagListColors[Integer.parseInt(tagListId[i])]), PorterDuff.Mode.SRC_IN);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                if(i != 0){params.setMarginStart((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                        8, context.getResources().getDisplayMetrics()));} //convert pixels to dp
                tag.setLayoutParams(params);
                tag.setText(tagListNames[Integer.parseInt(tagListId[i])]);
                tag.setBackground(drawable);

                holder.tags.addView(tag);
            }
        }

    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt, time;
        LinearLayout tags;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTxt = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            tags = itemView.findViewById(R.id.tags);

            nameTxt.setSelected(true);
        }
    }
}
