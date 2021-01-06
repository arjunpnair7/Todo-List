package com.example.simpletodolist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TodoItemsViewModel extends ViewModel {

    public static LiveData<List<TodoItem>> myItems;


    public static void retrieveItems(String id) {
        myItems = TodoListFragment.database.toDoListDao().getItemsForList(id);
    }



}
