package com.example.simpletodolist.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.simpletodolist.ToDoList;
import com.example.simpletodolist.TodoItem;

@Database(entities = {ToDoList.class, TodoItem.class}, version = 1)
@TypeConverters({ToDoListsTypeConvertors.class})
public abstract class TodolistsDatabase extends RoomDatabase {

    public abstract ToDoListDao toDoListDao();


}
