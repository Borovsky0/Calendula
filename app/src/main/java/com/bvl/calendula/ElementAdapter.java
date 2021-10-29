package com.bvl.calendula;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ViewHolder> {

    Context context;
    ArrayList id, name, date, day_of_week, periodicity, time_start, time_finish, tags, text_note, pic_note, audio_note;

    public ElementAdapter(Context context, ArrayList id, ArrayList name, ArrayList date, ArrayList day_of_week,
                          ArrayList periodicity, ArrayList time_start, ArrayList time_finish,
                          ArrayList tags, ArrayList text_note, ArrayList pic_note, ArrayList audio_note){
        this.context = context;
        this.id = id;
        this.name = name;
        this.date = date;
        this.day_of_week = day_of_week;
        this.periodicity = periodicity;
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
        holder.ID_txt.setText(String.valueOf(id.get(position)));
        holder.Name_txt.setText(String.valueOf(name.get(position)));
        holder.Date_txt.setText(String.valueOf(date.get(position)));
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView ID_txt, Name_txt, Date_txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ID_txt = itemView.findViewById(R.id.textID);
            Name_txt = itemView.findViewById(R.id.textName);
            Date_txt = itemView.findViewById(R.id.textDate);
        }
    }
}
