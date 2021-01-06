package com.example.simpletodolist.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.simpletodolist.ToDoList;
import com.example.simpletodolist.TodoItem;

import java.util.List;

@Dao
public interface ToDoListDao {

    @Insert
    public void addToDoList(ToDoList list);

    @Query("SELECT * FROM todolist")
    public LiveData<List<ToDoList>> getToDoLists();

    @Query("SELECT * FROM todoitem WHERE id = :listName")
    public LiveData<List<TodoItem>> getItemsForList(String listName);

    @Insert
    public void addItemToList(TodoItem item);






}
