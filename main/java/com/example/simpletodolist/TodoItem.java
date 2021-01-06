package com.example.simpletodolist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
public class TodoItem {


    public String title;

    @PrimaryKey(autoGenerate = true)
    public int identifier;

    public boolean isCompleted;
    public Date creationDate;
    public String id;

    public TodoItem(String title, boolean isCompleted, Date creationDate) {
        this.title = title;
        this.isCompleted = isCompleted;
        this.creationDate = creationDate;
       // this.identifier = Integer.valueOf(UUID.randomUUID().toString());
    }


}
