package com.bvl.calendula;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class AddBottomSheet extends BottomSheetDialogFragment {

    EditText name, date, timeStart, timeFinish, repeat, textNote;
    ImageView button_ok, button_cancel;
    TextView [] tags = new TextView[3];
    TextView button_tag_add;

    Calendar calendar = Calendar.getInstance(), timeStartCalendar = Calendar.getInstance(), 
            timeFinishCalendar = Calendar.getInstance(), calendarChosen = Calendar.getInstance();
    String dataDate;
    String [] repeatList, tagListNames, tagListColors, tagTextColors;
    int dataDayRepeat = 0, dataWeekRepeat = 0, tagCount = 0, curTag = 0;
    Integer [] dataTags = {-1, -1, -1};
    boolean timeStartSet = false;
    String fragmentType;
    
    private final OnOkButtonClickListener onOkButtonClickListener;  
    
    public AddBottomSheet(OnOkButtonClickListener onOkButtonClickListener, Calendar calendar, String fragmentType){
        this.onOkButtonClickListener = onOkButtonClickListener;
        this.calendar.setTime(calendar.getTime());
        this.calendarChosen.setTime(calendar.getTime());
        this.fragmentType = fragmentType;
    } 
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_add, container, false);

        name = view.findViewById(R.id.editName);
        date = view.findViewById(R.id.editDate);
        repeat = view.findViewById(R.id.editRepeat);
        timeStart = view.findViewById(R.id.editTimeStart);
        timeFinish = view.findViewById(R.id.editTimeFinish);
        button_tag_add = view.findViewById(R.id.button_tag_add);
        tags[0] = view.findViewById(R.id.tag1);
        tags[1] = view.findViewById(R.id.tag2);
        tags[2] = view.findViewById(R.id.tag3);
        textNote = view.findViewById(R.id.editTextNote);
        button_ok = view.findViewById(R.id.button_ok);
        button_cancel = view.findViewById(R.id.button_cancel);

        //name.requestFocus();

        TypedValue value = new TypedValue(); //get color from attr
        getContext().getTheme().resolveAttribute(R.attr.colorSecondary, value, true);
        int colorSecondary = value.data;

        GradientDrawable drawable = (GradientDrawable) getContext().getResources().getDrawable(R.drawable.rounded_corner_tag_button);
        drawable.setStroke((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                2, getResources().getDisplayMetrics()), colorSecondary); //set stroke in dp

        button_tag_add.setBackground(drawable);

        date.setText(new SimpleDateFormat("d MMMM y, EEEE").format(calendar.getTime()));
        dataDate = new SimpleDateFormat("yyyy/MM/dd").format(calendar.getTime());

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), 
                        R.style.CustomDatePickerTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendarChosen.set(year, month, day);
                        date.setText(dataWeekRepeat == 0 ? 
                                new SimpleDateFormat("d MMMM y, EEEE").format(calendarChosen.getTime())
                                : new SimpleDateFormat("EEEE").format(calendarChosen.getTime()));
                        dataDate = new SimpleDateFormat("yyyy/MM/dd").format(calendarChosen.getTime());
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        repeatList = getResources().getStringArray(R.array.repeat);

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
                        date.setText(dataWeekRepeat == 0? new SimpleDateFormat("d MMMM y, EEEE").format(calendar.getTime())
                                : new SimpleDateFormat("EEEE").format(calendar.getTime()));
                        dialogInterface.dismiss();
                    }
                });

                builder.setNegativeButton(getString(R.string.cancel), null);

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        timeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hourStart = timeStartCalendar.get(Calendar.HOUR_OF_DAY);
                int minuteStart = timeStartCalendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.CustomTimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        timeStartCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        timeStartCalendar.set(Calendar.MINUTE, minute);
                        timeStartCalendar.setTimeZone(TimeZone.getDefault());
                        timeStart.setText(new SimpleDateFormat("HH:mm").format(timeStartCalendar.getTime()));

                        if(!timeStartSet)
                        {
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
                            timeStart.setLayoutParams(params);
                            timeStartSet = true;
                        }
                        else
                        {
                            if(timeStartCalendar.getTime().getTime() > timeFinishCalendar.getTime().getTime())
                            {
                                timeStart.setText(timeFinish.getText());
                                timeFinish.setText(new SimpleDateFormat("HH:mm").format(timeStartCalendar.getTime()));
                            }
                            else
                            {
                                timeStart.setText(new SimpleDateFormat("HH:mm").format(timeStartCalendar.getTime()));
                            }
                        }
                    }
                }, hourStart, minuteStart, true);
                //timePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE);
                timePickerDialog.show();
            }
        });

        timeFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hourFinish = timeFinishCalendar.get(Calendar.HOUR_OF_DAY);
                int minuteFinish = timeFinishCalendar.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), R.style.CustomTimePickerTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        timeFinishCalendar.set(Calendar.HOUR_OF_DAY, hour);
                        timeFinishCalendar.set(Calendar.MINUTE, minute);
                        timeFinishCalendar.setTimeZone(TimeZone.getDefault());

                        if(timeStartCalendar.getTime().getTime() > timeFinishCalendar.getTime().getTime())
                        {
                            timeFinish.setText(timeStart.getText());
                            timeStart.setText(new SimpleDateFormat("HH:mm").format(timeFinishCalendar.getTime()));
                        }
                        else
                        {
                            timeFinish.setText(new SimpleDateFormat("HH:mm").format(timeFinishCalendar.getTime()));
                        }
                    }
                }, hourFinish, minuteFinish, true);
                //timePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE);
                timePickerDialog.show();
            }
        });

        tagListNames = getResources().getStringArray(R.array.tag_names);
        tagListColors = getResources().getStringArray(R.array.tag_colors);
        tagTextColors = getResources().getStringArray(R.array.tag_text_colors);

        button_tag_add.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                curTag = 0;

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialogTheme);
                builder.setTitle(getString(R.string.add_tag));
                builder.setSingleChoiceItems(tagListNames, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(tagCount < 3)
                        {
                            curTag = i;
                        }
                    }
                });

                builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(!intContains(dataTags, curTag) && tagCount < 3)
                        {
                            dataTags[tagCount] = curTag;

                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button_tag_add.getLayoutParams();
                            params.setMarginStart((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                                    8, getResources().getDisplayMetrics())); //convert pixels to dp
                            button_tag_add.setLayoutParams(params);

                            Drawable drawable = getResources().getDrawable(R.drawable.rounded_corner_text);
                            drawable.setColorFilter(Color.parseColor(tagListColors[dataTags[tagCount]]), PorterDuff.Mode.SRC_IN);
                            tags[tagCount].setBackground(drawable);
                            tags[tagCount].setText(tagListNames[dataTags[tagCount]]);
                            tags[tagCount].setTextColor(Color.parseColor(tagTextColors[dataTags[tagCount]]));
                            tags[tagCount].setVisibility(View.VISIBLE);
                            if(tagCount>0) {tags[tagCount].setLayoutParams(params);}

                            tagCount+=1;

                            if(tagCount == 3) { button_tag_add.setVisibility(View.INVISIBLE);}

                            dialogInterface.dismiss();
                        }
                    }
                });

                builder.setNegativeButton(getString(R.string.cancel), null);

                builder.create().show();
            }
        });

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(name.getText().toString().equals(""))
                {
                    Toast.makeText(getContext(), getString(R.string.empty_error), Toast.LENGTH_SHORT).show();
                }
                else{
                    DatabaseHelper db_helper = new DatabaseHelper(getContext());
                    db_helper.add(
                            name.getText().toString(),
                            dataDate,
                            dataWeekRepeat == 0 ? "NULL" : Integer.toString(dataDayRepeat),
                            dataWeekRepeat == 0 ? "NULL" : Integer.toString(dataWeekRepeat-1),
                            timeStart.getText().toString().equals("--:--") ? "NULL" : timeStart.getText().toString(),
                            timeFinish.getText().toString().equals("--:--") ? "NULL" : timeFinish.getText().toString(),
                            Arrays.toString(dataTags).replaceAll("[\\s\\[\\]]",""), //replace all " ", "[" and "]" with ""
                            textNote.getText().toString(),
                            "NULL",
                            "NULL",
                            "FALSE");
                    if (AddBottomSheet.this.calendar.get(Calendar.YEAR) == calendarChosen.get(Calendar.YEAR) &&
                        AddBottomSheet.this.calendar.get(Calendar.MONTH) == calendarChosen.get(Calendar.MONTH)) {
                        switch (fragmentType){
                            case "DAY":
                                if (AddBottomSheet.this.calendar.get(Calendar.DAY_OF_MONTH) == calendarChosen.get(Calendar.DAY_OF_MONTH)){
                                    onOkButtonClickListener.onOkButtonClick(AddBottomSheet.this.calendar);
                                }
                                break;
                            case "MONTH":
                                onOkButtonClickListener.onOkButtonClick(AddBottomSheet.this.calendar);
                                break;
                            default:
                                break;
                        }
                    }
                    dismiss();
                }
            }
        });

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    boolean intContains(Integer[] array, int value)
    {
        for (int element : array) {
            if (element == value) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetTheme);
        super.onCreate(savedInstanceState);
    }
    
    public interface OnOkButtonClickListener {
        void onOkButtonClick(Calendar calendar);
    }
}