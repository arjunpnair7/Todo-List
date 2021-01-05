package com.example.simpletodolist.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.simpletodolist.ToDoList;

@Database(entities = {ToDoList.class}, version = 1)
public abstract class TodolistsDatabase extends RoomDatabase {


}
