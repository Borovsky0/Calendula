package com.bvl.calendula;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ScrollAdapter extends RecyclerView.Adapter<ScrollAdapter.ViewHolder> {

    Context context;
    int centerPos = Integer.MAX_VALUE / 2;
    int row_index = centerPos;
    private OnDateClickListener onDateClickListener;
    
    public ScrollAdapter(OnDateClickListener onDateClickListener){
        this.onDateClickListener = onDateClickListener;
    }

    @Override
    public ScrollAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_scroll, parent, false);
        context = parent.getContext();
        return new ViewHolder(rowItem, onDateClickListener);
    }

    @Override
    public void onBindViewHolder(ScrollAdapter.ViewHolder holder, int position) {
        int dif = centerPos - position; //day difference
        Calendar calendar = Calendar.getInstance(); //get today date
        calendar.add(Calendar.DATE, -dif);
        holder.dateText.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
        holder.monthText.setText(new SimpleDateFormat("MMM").format(calendar.getTime()));

        TypedValue value = new TypedValue(); //get color from attr
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        int colorPrimary = value.data;
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        @ColorInt int colorOnBackground = value.data;

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDateClickListener.onDateClick(holder.getAdapterPosition());
                row_index=holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });

        if (row_index==holder.getAdapterPosition()) {
            holder.cardView.setCardBackgroundColor(colorPrimary);
        } else {
            holder.cardView.setCardBackgroundColor(colorOnBackground);
        }
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView dateText;
        private TextView monthText;
        private LinearLayout linearLayout;
        private CardView cardView;

        private OnDateClickListener onDateClickListener;
        
        public ViewHolder(View view, OnDateClickListener onDateClickListener) {
            super(view);
            this.dateText = view.findViewById(R.id.date);
            this.monthText = view.findViewById(R.id.month);
            this.linearLayout = view.findViewById(R.id.linear);
            this.cardView = view.findViewById(R.id.card);
            this.onDateClickListener = onDateClickListener;
        }
    }

    public interface OnDateClickListener {
        void onDateClick(int position);
    }
}