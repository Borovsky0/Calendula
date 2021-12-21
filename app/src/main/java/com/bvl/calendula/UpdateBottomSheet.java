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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;

public class UpdateBottomSheet extends BottomSheetDialogFragment {

    String d_id, d_name, d_date, d_day_repeat, d_week_repeat, d_time_start, d_time_finish, d_tags, d_text_note, d_pic_note, d_audio_note;

    EditText name, date, timeStart, timeFinish, repeat, textNote;
    ImageView button_ok, button_cancel, button_delete;
    TextView [] tags = new TextView[3];
    TextView button_tag_add;

    Calendar calendar = Calendar.getInstance(), timeStartCalendar = Calendar.getInstance(), 
            timeFinishCalendar = Calendar.getInstance(), calendarChosen = Calendar.getInstance();
    String dataDate;
    String [] repeatList, tagListNames, tagListColors, tagTextColors;
    int dataDayRepeat = 0, dataWeekRepeat = 0, tagCount = 0, curTag = 0;
    Integer [] dataTags = {-1, -1, -1};
    boolean timeStartSet = false;
    
    private final OnOkButtonClickListener onOkButtonClickListener;

    public UpdateBottomSheet(String id, String name, String date, String day_of_week,
                             String periodicity, String time_start, String time_finish,
                             String tags, String text_note, String pic_note, String audio_note,
                             OnOkButtonClickListener onOkButtonClickListener)
    {
        this.d_id = id;
        this.d_name = name;
        this.d_date = date;
        this.d_day_repeat = day_of_week;
        this.d_week_repeat = periodicity;
        this.d_time_start = time_start;
        this.d_time_finish = time_finish;
        this.d_tags = tags;
        this.d_text_note = text_note;
        this.d_pic_note = pic_note;
        this.d_audio_note = audio_note;
        this.onOkButtonClickListener = onOkButtonClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet_update, container, false);

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
        button_delete = view.findViewById(R.id.button_delete);

        try { 
            calendar.setTime(new SimpleDateFormat("yyyy/MM/dd").parse(d_date));
            calendarChosen.setTime(calendar.getTime());
        } 
        catch (ParseException e) { e.printStackTrace(); }

        try { timeStartCalendar.setTime(new SimpleDateFormat("HH:mm").parse(d_time_start));
        } catch (ParseException e) { e.printStackTrace(); }

        try { timeFinishCalendar.setTime(new SimpleDateFormat("HH:mm").parse(d_time_finish));
        } catch (ParseException e) { e.printStackTrace(); }

        repeatList = getResources().getStringArray(R.array.repeat);
        tagListNames = getResources().getStringArray(R.array.tag_names);
        tagListColors = getResources().getStringArray(R.array.tag_colors);
        tagTextColors = getResources().getStringArray(R.array.tag_text_colors);

        name.setText(d_name);
        textNote.setText(d_text_note);

        dataDayRepeat = !d_day_repeat.equals("NULL") ? Integer.parseInt(d_day_repeat):0;
        dataWeekRepeat = !d_week_repeat.equals("NULL") ? Integer.parseInt(d_week_repeat)+1:0;
        repeat.setText(repeatList[dataWeekRepeat]);
        date.setText(dataWeekRepeat == 0? new SimpleDateFormat("d MMMM y, EEEE").format(calendar.getTime())
                : new SimpleDateFormat("EEEE").format(calendar.getTime()));


        if(d_time_start.equals("NULL")) { timeStart.setText("--:--"); }
        else
        {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            timeStart.setLayoutParams(params);
            timeStartSet = true;
            timeStart.setText(new SimpleDateFormat("HH:mm").format(timeStartCalendar.getTime()));
            timeFinish.setText(d_time_finish.equals("NULL") ? "--:--" : new SimpleDateFormat("HH:mm").format(timeFinishCalendar.getTime()));
        }

        //timeFinishCalendars set

        String[] tagListId = String.valueOf(d_tags).split(",");
        for(int i = 0; i < 3; i++)
        {
            dataTags[i] = Integer.parseInt(tagListId[i]);
            if(dataTags[i] != -1)
            {
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
            }
        }

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
                        date.setText(dataWeekRepeat == 0? new SimpleDateFormat("d MMMM y, EEEE").format(calendarChosen.getTime())
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
                    db_helper.update(
                            d_id,
                            name.getText().toString(),
                            dataDate,
                            dataWeekRepeat == 0 ? "NULL" : Integer.toString(dataDayRepeat),
                            dataWeekRepeat == 0 ? "NULL" : Integer.toString(dataWeekRepeat-1),
                            timeStart.getText().toString().equals("--:--") ? "NULL" : timeStart.getText().toString(),
                            timeFinish.getText().toString().equals("--:--") ? "NULL" : timeFinish.getText().toString(),
                            Arrays.toString(dataTags).replaceAll("[\\s\\[\\]]",""), //replace all " ", "[", "]" with ""
                            textNote.getText().toString(),
                            "NULL",
                            "NULL");
                    onOkButtonClickListener.onOkButtonClick(UpdateBottomSheet.this.calendar);
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

        button_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.CustomAlertDialogTheme);
                builder.setTitle(getString(R.string.delete) + " " + d_name + "?");
                builder.setMessage(getString(R.string.delete_confirm) + " " + d_name + "?");
                builder.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseHelper db_helper = new DatabaseHelper(getContext());
                        db_helper.delete(d_id);
                        onOkButtonClickListener.onOkButtonClick(UpdateBottomSheet.this.calendar);
                        dialogInterface.dismiss();
                        dismiss();
                    }
                });
                builder.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
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