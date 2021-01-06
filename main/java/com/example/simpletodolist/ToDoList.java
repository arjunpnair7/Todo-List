package com.example.simpletodolist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class ToDoList {


    public String title;

    @PrimaryKey(autoGenerate = true)
    public int identifier;

    //public List<TodoItem> todoItems = new ArrayList<>();

    //public ToDoList(String newTitle, List<TodoItem> list) {
    //    title = newTitle;
        //todoItems = list;
  //  }

    public ToDoList(String title) {
        //title = newTitle;
        this.title = title;
        //this.identifier = Integer.valueOf(UUID.randomUUID().toString());
    }
}
