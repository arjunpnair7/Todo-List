package com.example.simpletodolist;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class TodoItemsViewModel extends ViewModel {

    public static LiveData<List<TodoItem>> myItems;
    public static LiveData<List<TodoItem>> myCompletedItems;



    public static void retrieveItems(String id, boolean completion) {
        myItems = TodoListFragment.database.toDoListDao().getItemsForList(id, false);
        Log.i("test", "retrieving items that have not been finished");
    }
    public static void retrieveCompletedItems(String id) {
        myCompletedItems = TodoListFragment.database.toDoListDao().getItemsForList(id, true);
        Log.i("test", "retrieving items that have been finished");
    }




}
