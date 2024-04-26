package com.example.a41_taskmanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ExploreTask extends AppCompatActivity {

    private TextView taskTitleTextView, taskDescriptionTextView, taskDueDateTextView, taskIdTextView;
    private MySQLiteHelper mySQLiteHelper;
    private SQLiteDatabase database;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explore_task);

        // Initialize views and database helper
        taskTitleTextView = findViewById(R.id.tv_task_title);
        taskDescriptionTextView = findViewById(R.id.tv_task_description);
        taskIdTextView = findViewById(R.id.tv_task_id);
        taskDueDateTextView = findViewById(R.id.tv_task_due_date);

        // Get task data from Intent
        Intent intent = getIntent();
        task = (Task) intent.getSerializableExtra("task");

        // Initialize database helper
        mySQLiteHelper = new MySQLiteHelper(this);
        database = mySQLiteHelper.getWritableDatabase();

        // Set task details to the views
        taskIdTextView.setText("Task ID: " + task.getId());
        taskTitleTextView.setText("Title: " + task.getTitle());
        taskDescriptionTextView.setText("Description: " + task.getDescription());
        taskDueDateTextView.setText("Due Date: " + task.getDueDate());

        // Set click listener for edit button
        findViewById(R.id.btn_edit_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToEditTask();
            }
        });

        // Set click listener for delete button
        findViewById(R.id.btn_delete_task).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTask();
            }
        });
    }

    // Method to navigate to EditTask activity
    private void navigateToEditTask() {
        Intent intent = new Intent(getApplicationContext(), EditTask.class);
        intent.putExtra("task", task);
        startActivity(intent);
    }

    // Method to delete the current task
    private void deleteTask() {
        database.delete(TaskContract.TaskEntry.TABLE_NAME, TaskContract.TaskEntry._ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        Toast.makeText(getApplicationContext(), "Task Deleted Successfully", Toast.LENGTH_SHORT).show();
        finish(); // Close the ExploreTask activity after deletion
    }
}