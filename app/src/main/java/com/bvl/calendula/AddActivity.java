package com.bvl.calendula;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText name, date, day_of_week, periodicity, time_start, time_finish, tags, text_note, pic_note, audio_note;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name = findViewById(R.id.editName);
        date = findViewById(R.id.editDate);
        day_of_week = findViewById(R.id.editDay);
        periodicity = findViewById(R.id.editPeriodicity);
        time_start = findViewById(R.id.editTimeStart);
        time_finish = findViewById(R.id.editTimeFinish);
        tags = findViewById(R.id.editTags);
        text_note = findViewById(R.id.editTextNote);
        pic_note = findViewById(R.id.editPicNote);
        audio_note = findViewById(R.id.editAudioNote);

        button = findViewById(R.id.save);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db_helper = new DatabaseHelper(AddActivity.this);
                db_helper.add(name.getText().toString().trim(),
                        date.getText().toString().trim(),
                        day_of_week.getText().toString().trim(),
                        periodicity.getText().toString().trim(),
                        time_start.getText().toString().trim(),
                        time_finish.getText().toString().trim(),
                        tags.getText().toString().trim(),
                        text_note.getText().toString().trim(),
                        pic_note.getText().toString().trim(),
                        audio_note.getText().toString().trim());
            }
        });
    }
}