package com.example.simpletodolist.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.simpletodolist.ToDoList;
import com.example.simpletodolist.TodoItem;
import com.example.simpletodolist.TodoListFragment;

import java.util.List;

@Dao
public interface ToDoListDao {

    @Insert
    public void addToDoList(ToDoList list);

    @Query("SELECT * FROM todolist")
    public LiveData<List<ToDoList>> getToDoLists();

    @Query("SELECT * FROM todolist WHERE listID = :listID")
    public ToDoList getToDoListByID(String listID);

    @Query("SELECT * FROM todoitem WHERE id = :listID AND isCompleted = :completed")
    public LiveData<List<TodoItem>> getItemsForList(String listID, boolean completed);

    @Insert
    public void addItemToList(TodoItem item);

    @Update
    public void updateItemStatus(TodoItem item);

    @Update
    public void updateList(ToDoList list);






}
