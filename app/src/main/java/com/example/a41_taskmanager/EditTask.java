package com.example.a41_taskmanager;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class EditTask extends AppCompatActivity {

    private TextView heading;
    private EditText titleEditText, descEditText;
    private SQLiteDatabase database;
    private MySQLiteHelper mySQLiteHelper;
    private Task task = null;
    private String selectedDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        // Initialize views and database
        heading = findViewById(R.id.tv_heading);
        titleEditText = findViewById(R.id.et_title);
        descEditText = findViewById(R.id.et_description);
        Button addButton = findViewById(R.id.btn_add_task);
        mySQLiteHelper = new MySQLiteHelper(this);
        database = mySQLiteHelper.getWritableDatabase();

        // Get task data from Intent
        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra("task");

        // Set initial values if editing an existing task
        if (task != null) {
            heading.setText("Editing Task: " + task.getId());
            titleEditText.setText(task.getTitle());
            descEditText.setText(task.getDescription());
            selectedDate = task.getDueDate();
            addButton.setText("Update");
        }

        // Set click listener for add/update button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addOrUpdateTask();
            }
        });

        // Set click listener for date picker button
        findViewById(R.id.btn_pick_date).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    // Method to add or update task in the database
    private void addOrUpdateTask() {
        String titleText = titleEditText.getText().toString();
        String descText = descEditText.getText().toString();

        // Check if all fields are filled
        if (titleText.isEmpty() || descText.isEmpty() || selectedDate.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Prepare data to be inserted or updated
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COLUMN_TITLE, titleText);
        values.put(TaskContract.TaskEntry.COLUMN_DESCRIPTION, descText);
        values.put(TaskContract.TaskEntry.COLUMN_DUE_DATE, selectedDate);

        // Perform insertion or update based on task existence
        if (task == null) {
            database.insert(TaskContract.TaskEntry.TABLE_NAME, null, values);
            Toast.makeText(this, "Task created", Toast.LENGTH_SHORT).show();
        } else {
            database.update(TaskContract.TaskEntry.TABLE_NAME, values, TaskContract.TaskEntry._ID + " = ?",
                    new String[]{String.valueOf(task.getId())});
            Toast.makeText(this, "Task updated", Toast.LENGTH_SHORT).show();
        }

        finish(); // Close activity after adding/updating task
    }

    // Method to show date picker dialog
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create date picker dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Format selected date
                selectedDate = String.format("%02d/%02d/%d", dayOfMonth, month + 1, year);
            }
        }, year, month, day);

        datePickerDialog.show(); // Show date picker dialog
    }
}