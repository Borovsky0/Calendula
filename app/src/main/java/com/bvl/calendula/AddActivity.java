package com.bvl.calendula;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    EditText name, date, tags;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        name = findViewById(R.id.editName);
        date = findViewById(R.id.editDate);
        tags = findViewById(R.id.editTags);
        button = findViewById(R.id.save);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db_helper = new DatabaseHelper(AddActivity.this);
                db_helper.add(name.getText().toString().trim(),
                        date.getText().toString().trim(),
                        tags.getText().toString().trim());
            }
        });
    }
}