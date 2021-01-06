package com.example.simpletodolist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity
public class TodoItem {

    @PrimaryKey @NonNull
    public String title;


    public boolean isCompleted;
    public Date creationDate;
    public String id;

    public TodoItem(String title, boolean isCompleted, Date creationDate) {
        this.title = title;
        this.isCompleted = isCompleted;
        this.creationDate = creationDate;
    }


}
