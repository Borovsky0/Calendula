package com.bvl.calendula;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;

public class ScrollAdapter extends RecyclerView.Adapter<ScrollAdapter.ViewHolder> {

    public ScrollAdapter (){
    }

    @Override
    public ScrollAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowItem = LayoutInflater.from(parent.getContext()).inflate(R.layout.element_scroll, parent, false);
        return new ViewHolder(rowItem);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(ScrollAdapter.ViewHolder holder, int position) {
        int dif = Integer.MAX_VALUE/2 - position;
        LocalDate date = LocalDate.now().minusDays(dif);
        holder.datetext.setText(Integer.toString(date.getDayOfMonth()));
        holder.monthtext.setText(date.getMonth().toString() + Integer.toString(date.getYear()));
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView datetext;
        private TextView monthtext;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            this.datetext = view.findViewById(R.id.date);
            this.monthtext = view.findViewById(R.id.month);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "position : " + getLayoutPosition() + " text : " + this.datetext.getText(), Toast.LENGTH_SHORT).show();
        }
    }
}