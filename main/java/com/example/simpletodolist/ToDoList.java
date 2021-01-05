package com.example.simpletodolist;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class ToDoList extends Object {
    public String title;
    public List<TodoItem> todoItems = new ArrayList<>();

    public ToDoList(String newTitle, List<TodoItem> list) {
        title = newTitle;
        todoItems = list;
    }
}
