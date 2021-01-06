package com.example.simpletodolist.database;

import androidx.room.TypeConverter;

import java.util.Date;
import java.util.UUID;

public class ToDoListsTypeConvertors {

    @TypeConverter
    public Long fromDate(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public Date toDate(Long input) {
        return new Date(input);
    }
    @TypeConverter
    public UUID toUUID(String uuid) {
        return UUID.fromString(uuid);
    }

    @TypeConverter
    public String fromUUID(UUID uuid) {
        return uuid.toString();
    }
}
