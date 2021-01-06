package com.example.simpletodolist.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.simpletodolist.ToDoList;

import java.util.List;

@Dao
public interface ToDoListDao {

    @Insert
    public void addToDoList(ToDoList list);

    @Query("SELECT * FROM todolist")
    public LiveData<List<ToDoList>> getToDoLists();






}
