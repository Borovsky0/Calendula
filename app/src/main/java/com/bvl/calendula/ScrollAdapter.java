package com.bvl.calendula;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ScrollAdapter extends RecyclerView.Adapter<ScrollAdapter.ViewHolder> {

    Context context;
    int centerPos = Integer.MAX_VALUE / 2;
    int row_index = centerPos;
    private final OnDateClickListener onDateClickListener;
    String fragmentType;
    String[] monthNamesRU;
    String[] shortMonthNamesRU;

    public ScrollAdapter(OnDateClickListener onDateClickListener, String fragmentType){
        this.onDateClickListener = onDateClickListener;
        this.fragmentType = fragmentType;
    }

    @Override
    public ScrollAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem;
        switch (fragmentType)
        {
            case "DAY":
                rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_scroll_day, parent, false);
                break;

            case "MONTH":
                rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_scroll_month, parent, false);
                break;

            default:
                rowItem = null;
                break;
        }
        context = parent.getContext();
        return new ViewHolder(rowItem, onDateClickListener);
    }

    @Override
    public void onBindViewHolder(ScrollAdapter.ViewHolder holder, int position) {
        int dif = centerPos - position; //day difference
        Calendar calendar = Calendar.getInstance(); //get today date

        monthNamesRU = context.getResources().getStringArray(R.array.month_names_ru);
        shortMonthNamesRU = context.getResources().getStringArray(R.array.short_month_names_ru);

        switch (fragmentType)
        {
            case "DAY":
                calendar.add(Calendar.DATE, -dif);
                holder.dayOfWeekText.setText(new SimpleDateFormat("E").format(calendar.getTime()).toUpperCase());

                if(Locale.getDefault().getLanguage() == "ru") {
                    holder.dateText.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                            shortMonthNamesRU[calendar.get(Calendar.MONTH)]);
                }
                else {
                    holder.dateText.setText(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                            new SimpleDateFormat("MMM").format(calendar.getTime()));
                }
                break;

            case "MONTH":
                calendar.add(Calendar.MONTH, -dif);

                if(Locale.getDefault().getLanguage() == "ru") {
                    holder.monthText.setText(monthNamesRU[calendar.get(Calendar.MONTH)]);
                }
                else {
                    holder.monthText.setText(new SimpleDateFormat("MMMM").format(calendar.getTime()));
                }
                holder.yearText.setText(new SimpleDateFormat("yyyy").format(calendar.getTime()));
                break;

            default:

                break;
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                switch (fragmentType)
                {
                    case "DAY": {
                        ((MainActivity) context).setToolbarDay(calendar);
                        int dif = centerPos - holder.getAdapterPosition();
                        Calendar calendarNewSelectedDay = Calendar.getInstance();
                        calendarNewSelectedDay.add(Calendar.DATE, -dif);
                        onDateClickListener.onDateClick(calendarNewSelectedDay);
                        break;
                    }
                    case "MONTH": {
                        ((MainActivity) context).setToolbarMonth(calendar);
                        int dif = centerPos - holder.getAdapterPosition();
                        Calendar calendarNewSelectedMonth = Calendar.getInstance();
                        calendarNewSelectedMonth.add(Calendar.MONTH, -dif);
                        onDateClickListener.onDateClick(calendarNewSelectedMonth);
                        break;
                    }
                    default:
                        break;
                }

                row_index=holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });

        TypedValue value = new TypedValue(); //get color from attr
        context.getTheme().resolveAttribute(R.attr.colorPrimary, value, true);
        int colorPrimary = value.data;
        context.getTheme().resolveAttribute(R.attr.colorAccent, value, true);
        int colorOnBackground = value.data;

        if (row_index == holder.getAdapterPosition()) {
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

        private final TextView dayOfWeekText;
        private final TextView dateText;
        private final TextView monthText;
        private final TextView yearText;
        private final LinearLayout linearLayout;
        private final CardView cardView;

        public ViewHolder(View view, OnDateClickListener onDateClickListener) {
            super(view);
            this.dayOfWeekText = view.findViewById(R.id.dayOfWeek);
            this.dateText = view.findViewById(R.id.date);
            this.monthText = view.findViewById(R.id.month);
            this.yearText = view.findViewById(R.id.year);
            this.linearLayout = view.findViewById(R.id.linear);
            this.cardView = view.findViewById(R.id.card);
        }
    }

    public interface OnDateClickListener {
        void onDateClick(Calendar calendar);
    }

    public void setNewPosition(int position)
    {
        this.row_index = position;
    }
}