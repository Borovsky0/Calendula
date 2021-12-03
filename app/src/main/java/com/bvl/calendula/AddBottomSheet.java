package com.bvl.calendula;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class AddBottomSheet extends BottomSheetDialogFragment {

    EditText name, date, time, repeat, text_note;
    Button button;

    Calendar calendar;
    String dataDate;
    String [] repeatList;
    int dataDayRepeat = 0, dataWeekRepeat = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add, container, false);

        name = view.findViewById(R.id.editName);
        date = view.findViewById(R.id.editDate);
        time = view.findViewById(R.id.editTime);
        repeat = view.findViewById(R.id.editRepeat);
        text_note = view.findViewById(R.id.editTextNote);
        button = view.findViewById(R.id.button);

        //name.requestFocus();

        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        date.setText(new SimpleDateFormat("d MMMM y").format(calendar.getTime()));
        dataDate = new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime());

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), R.style.CustomDatePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(year, month, day);
                        date.setText(new SimpleDateFormat("d MMMM y").format(calendar.getTime()));
                        dataDate = new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime());
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.CustomTimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hour);
                        calendar.set(Calendar.MINUTE, minute);
                        calendar.setTimeZone(TimeZone.getDefault());
                        time.setText(new SimpleDateFormat("H:mm").format(calendar.getTime()));
                    }
                }, hour, minute, true);
                //timePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE);
                timePickerDialog.show();
            }
        });

        repeatList = new String[]{getString(R.string.once), getString(R.string.weekly),
                getString(R.string.on_odd_weeks), getString(R.string.on_even_weeks)};

        repeat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialogTheme);
                builder.setTitle(getString(R.string.repeat));
                builder.setSingleChoiceItems(repeatList, dataWeekRepeat, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dataWeekRepeat = i;
                    }
                });

                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dataDayRepeat = calendar.get(Calendar.DAY_OF_WEEK);
                        repeat.setText(repeatList[dataWeekRepeat]);
                        dialogInterface.dismiss();
                    }
                });

                builder.setNegativeButton(getString(R.string.cancel), null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatabaseHelper db_helper = new DatabaseHelper(getContext());
                db_helper.add(
                        name.getText().toString(),
                        dataDate,
                        dataWeekRepeat == 0 ? "NULL" : Integer.toString(dataDayRepeat),
                        dataWeekRepeat == 0 ? "NULL" : Integer.toString(dataWeekRepeat-1),
                        time.getText().toString(),
                        "NULL",
                        "NULL",
                        text_note.getText().toString(),
                        "NULL",
                        "NULL");
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