package com.example.simpletodolist;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ToDoList {

    @PrimaryKey @NonNull
    public String title;


    //public List<TodoItem> todoItems = new ArrayList<>();

    //public ToDoList(String newTitle, List<TodoItem> list) {
    //    title = newTitle;
        //todoItems = list;
  //  }

    public ToDoList(@NonNull String title) {
        //title = newTitle;
        this.title = title;
    }
}
