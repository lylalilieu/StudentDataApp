package com.example.studentdataappp;


import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    EditText editName, editId;
    TextView textAge, textResult;
    SeekBar seekAge;
    Button buttonAdd, buttonEdit, buttonDelete, buttonView;
    DBHelper db;
    int age = 17; // minimum age

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBHelper(this);

        editName = findViewById(R.id.editName);
        editId = findViewById(R.id.editId);
        textAge = findViewById(R.id.textAge);
        textResult = findViewById(R.id.textResult);
        seekAge = findViewById(R.id.seekAge);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonEdit = findViewById(R.id.buttonEdit);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonView = findViewById(R.id.buttonView);

        // SeekBar: progress 0..33 -> age 17..50
        seekAge.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                age = progress + 17;
                textAge.setText("Age: " + age);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        // ADD
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInputValid(true)) return;
                boolean ok = db.addStudent(editId.getText().toString().trim(),
                        editName.getText().toString().trim(), age);
                showMessage(ok ? "Student added" : "Student ID already exists");
                if (ok) showAll();
            }
        });

        // EDIT
        buttonEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInputValid(true)) return;
                boolean ok = db.editStudent(editId.getText().toString().trim(),
                        editName.getText().toString().trim(), age);
                showMessage(ok ? "Student updated" : "Student ID not found");
                if (ok) showAll();
            }
        });

        // DELETE (only needs Student ID)
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isInputValid(false)) return;
                boolean ok = db.deleteStudent(editId.getText().toString().trim());
                showMessage(ok ? "Student deleted" : "Student ID not found");
                if (ok) showAll();
            }
        });

        // VIEW
        buttonView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAll();
            }
        });
    }

    // Check that the required fields are filled in
    private boolean isInputValid(boolean needName) {
        if (editId.getText().toString().trim().isEmpty()) {
            showMessage("Student ID is required");
            return false;
        }
        if (needName && editName.getText().toString().trim().isEmpty()) {
            showMessage("Student Name is required");
            return false;
        }
        return true;
    }

    // Read all records from SQLite and show them in the TextView
    private void showAll() {
        Cursor cursor = db.getAllStudents();
        StringBuilder sb = new StringBuilder();
        while (cursor.moveToNext()) {
            sb.append("ID: ").append(cursor.getString(0)).append("\n")
                    .append("Name: ").append(cursor.getString(1)).append("\n")
                    .append("Age: ").append(cursor.getInt(2)).append("\n\n");
        }
        cursor.close();
        textResult.setText(sb.length() == 0 ? "No data" : sb.toString());
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}