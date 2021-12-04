package com.bvl.calendula.ui;

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

import com.bvl.calendula.R;
import com.bvl.calendula.ScrollAdapter;

public class MonthFragment extends Fragment implements ScrollAdapter.OnDateClickListener {

    RecyclerView scroll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_month, container, false);

        scroll = v.findViewById(R.id.scroll);
        scroll.setLayoutManager(new LinearLayoutManager(MonthFragment.this.getActivity(), RecyclerView.HORIZONTAL, false));
        scroll.setAdapter(new ScrollAdapter(this, "MONTH"));
        scroll.getLayoutManager().scrollToPosition(Integer.MAX_VALUE / 2);

        return v;
    }

    @Override
    public void onDateClick(int position) {

    }
}
