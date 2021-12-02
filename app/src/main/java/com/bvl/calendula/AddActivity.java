package com.bvl.calendula;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AddActivity extends BottomSheetDialogFragment {

    EditText name, text_note;
    TextView date, time;
    Button button;
    String dateData;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_add, container, false);

        name = view.findViewById(R.id.editName);
        date = view.findViewById(R.id.editDate);
        time = view.findViewById(R.id.editTime);
        text_note = view.findViewById(R.id.editTextNote);
        button = view.findViewById(R.id.button);

        name.requestFocus();

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        date.setText(new SimpleDateFormat("d MMMM y").format(calendar.getTime()));
        dateData = new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime());

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        date.setText(new SimpleDateFormat("d MMMM y").format(calendar.getTime()));
                        dateData = new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime());
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        time.setText(new SimpleDateFormat("H:mm").format(calendar.getTime()));

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.setTimeZone(TimeZone.getDefault());
                        time.setText(new SimpleDateFormat("H:mm").format(calendar.getTime()));
                    }
                }, hour, minute, true);
                timePickerDialog.show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db_helper = new DatabaseHelper(getContext());
                db_helper.add(name.getText().toString(),
                        dateData,
                        "N",
                        "N",
                        time.getText().toString(),
                        "N",
                        "N",
                        text_note.getText().toString(),
                        "N",
                        "N");
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetTheme);
        super.onCreate(savedInstanceState);
    }
}