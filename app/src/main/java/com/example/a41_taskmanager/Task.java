package com.example.a41_taskmanager;

import java.io.Serializable;

// Task class implements Serializable to allow objects to be passed between activities
public class Task implements Serializable {
    // Task properties
    long id;
    String title;
    String description;
    String dueDate;

    // Constructor to initialize Task object
    public Task(long id, String title, String description, String dueDate) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }
}
