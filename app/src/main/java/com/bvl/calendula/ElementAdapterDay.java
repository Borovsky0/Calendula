package com.bvl.calendula;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bvl.calendula.ui.DayFragment;

import java.util.ArrayList;

public class ElementAdapterDay extends RecyclerView.Adapter<ElementAdapterDay.ViewHolder> {

    Context context;
    DayFragment dayFragment; 
    ArrayList<String> id, name, date, day_repeat, week_repeat, time_start, time_finish, tags, text_note, pic_note, audio_note, done;

    public ElementAdapterDay(DayFragment dayFragment, ArrayList<String> id, ArrayList<String> name, ArrayList<String> date, ArrayList<String> day_repeat,
                             ArrayList<String> week_repeat, ArrayList<String> time_start, ArrayList<String> time_finish,
                             ArrayList<String> tags, ArrayList<String> text_note, ArrayList<String> pic_note,
                             ArrayList<String> audio_note, ArrayList<String> done){
        this.dayFragment = dayFragment;
        this.context = dayFragment.getContext();
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
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.element_day, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tags.removeAllViews();
        holder.time.setText("");
        holder.time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 0);
        holder.done.setOnCheckedChangeListener(null);

        String[] tagListId = String.valueOf(tags.get(position)).split(",");
        String[] tagListNames = context.getResources().getStringArray(R.array.tag_names);
        String[] tagListColors = context.getResources().getStringArray(R.array.tag_colors);
        String[] tagTextColors = context.getResources().getStringArray(R.array.tag_text_colors);

        holder.name.setText(String.valueOf(name.get(position)));

        holder.done.setChecked(done.get(position).equals("TRUE") ? true : false);

        if(!time_start.get(position).equals("NULL")){
        holder.time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            holder.time.setText(String.valueOf(time_finish.get(position)).equals("NULL") ? String.valueOf(time_start.get(position))
                : String.valueOf(time_start.get(position)) + " - " + String.valueOf(time_finish.get(position)));}

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
                tag.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tag.setBackground(drawable);
                tag.setTextColor(Color.parseColor(tagTextColors[Integer.parseInt(tagListId[i])]));

                holder.tags.addView(tag);
            }
        }

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateBottomSheet updateBottomSheet = new UpdateBottomSheet(String.valueOf(id.get(position)),String.valueOf(name.get(position)),
                        String.valueOf(date.get(position)),String.valueOf(day_repeat.get(position)),String.valueOf(week_repeat.get(position)),
                        String.valueOf(time_start.get(position)),String.valueOf(time_finish.get(position)),String.valueOf(tags.get(position)),
                        String.valueOf(text_note.get(position)),String.valueOf(pic_note.get(position)),String.valueOf(audio_note.get(position)),
                        ElementAdapterDay.this.dayFragment);
                updateBottomSheet.show(((FragmentActivity) context).getSupportFragmentManager(), "TAG");
            }
        });

        holder.done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                DatabaseHelper db_helper = new DatabaseHelper(context);
                db_helper.done(String.valueOf(id.get(position)),b == true ? "TRUE" : "FALSE");
            }
        });
    }

    @Override
    public int getItemCount() {
        return id.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, time;
        LinearLayout mainLayout, tags;
        CheckBox done;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            time = itemView.findViewById(R.id.time);
            mainLayout = itemView.findViewById(R.id.main_layout);
            tags = itemView.findViewById(R.id.tags);
            done = itemView.findViewById(R.id.done);

            name.setSelected(true);
        }
    }
}
