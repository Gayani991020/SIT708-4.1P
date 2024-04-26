package com.example.a41_taskmanager;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<Task> taskList;
    ImageView addTaskBtn;
    SQLiteDatabase database;
    TaskDbHelper dbHelper;
    TaskAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Initialize database and views
        dbHelper = new TaskDbHelper(this);
        database = dbHelper.getWritableDatabase();
        recyclerView = findViewById(R.id.recycler_view);
        addTaskBtn = findViewById(R.id.btn_add_task);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize task list and adapter
        taskList = new ArrayList<>();
        adapter = new TaskAdapter(this, taskList);
        fetchDataFromDB(); // Fetch data from the database

        // Set click listeners for buttons
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), EditTaskActivity.class);
                startActivity(i);
            }
        });

        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchDataFromDB(); // Refresh data from the database
            }
        });
    }

    // Method to fetch data from the database and update the UI
    public void fetchDataFromDB() {
        taskList.clear();
        Cursor cursor = database.query(
                TaskEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TaskEntry.COLUMN_DUE_DATE + " DESC"
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                taskList.add(new Task(
                        cursor.getLong(cursor.getColumnIndex(TaskEntry._ID)),
                        cursor.getString(cursor.getColumnIndex(TaskEntry.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(TaskEntry.COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(TaskEntry.COLUMN_DUE_DATE))
                ));
            } while (cursor.moveToNext());
            cursor.close();
        }
        recyclerView.setAdapter(adapter); // Set adapter to RecyclerView
    }

    // RecyclerView Adapter class
    private class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {
        private List<Task> dataList;
        private Context context;

        public TaskAdapter(Context context, List<Task> taskList) {
            this.context = context;
            this.dataList = taskList;
        }

        @NonNull
        @Override
        public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            return new TaskViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
            Task task = dataList.get(position);
            holder.taskTitle.setText(task.getTitle());
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        public class TaskViewHolder extends RecyclerView.ViewHolder {
            public TextView taskTitle;

            public TaskViewHolder(View itemView) {
                super(itemView);
                taskTitle = itemView.findViewById(R.id.task_title);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Handle item click, open ExploreTaskActivity
                        Intent i = new Intent(context, ExploreTaskActivity.class);
                        i.putExtra("task", dataList.get(getAdapterPosition()));
                        context.startActivity(i);
                    }
                });
            }
        }
    }
}