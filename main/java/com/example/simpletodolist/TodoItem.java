package com.example.simpletodolist;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;

import java.io.Serializable;
import java.util.Date;

@Entity
public class TodoItem {

    public String listItemBelongsTo;

    public String title;
    public boolean isCompleted;
    public Date creationDate;
    public String id;

    public TodoItem(String inputTitle, boolean status, Date created) {
        title = inputTitle;
        isCompleted = status;
        creationDate = created;
    }


}
