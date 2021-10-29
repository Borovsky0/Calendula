package com.bvl.calendula.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bvl.calendula.AddActivity;
import com.bvl.calendula.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class DayFragment extends Fragment {

    RecyclerView recyclerView;
    FloatingActionButton button;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_day, container, false);

        recyclerView = v.findViewById(R.id.recycler);
        button = v.findViewById(R.id.add_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DayFragment.this.getActivity(), AddActivity.class);
                startActivity(intent);
            }
        });
        return v;
    }
}
