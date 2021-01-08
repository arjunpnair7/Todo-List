package com.example.simpletodolist.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.simpletodolist.ToDoList;
import com.example.simpletodolist.TodoItem;

@Database(entities = {ToDoList.class, TodoItem.class}, version = 8)
@TypeConverters({ToDoListsTypeConvertors.class})
public abstract class TodolistsDatabase extends RoomDatabase {

    public abstract ToDoListDao toDoListDao();

    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE TodoItem ADD COLUMN identifier INTEGER");
            //"ALTER TABLE TodoItem ADD COLUMN identifier UUID"

            database.execSQL("ALTER TABLE ToDoList ADD COLUMN identifier INTEGER");
           // "ALTER TABLE ToDoList ADD COLUMN identifier UUID"
        }
    };
               // database.execSQL("ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''");

    public static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE TodoItem ADD COLUMN imageViewID INTEGER");
            //"ALTER TABLE TodoItem ADD COLUMN identifier UUID"


        }
    };


}
