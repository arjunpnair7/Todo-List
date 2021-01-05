package com.example.simpletodolist;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class TodoItem {

    public String title;
    public boolean isCompleted;
    public Date creationDate;

    public TodoItem(String inputTitle, boolean status, Date created) {
        title = inputTitle;
        isCompleted = status;
        creationDate = created;
    }


}
