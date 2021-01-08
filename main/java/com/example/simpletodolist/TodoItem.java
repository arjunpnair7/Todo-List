package com.example.simpletodolist;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.jarjarred.org.antlr.v4.runtime.misc.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Entity
public class TodoItem {


    public String title;

    @PrimaryKey(autoGenerate = true)
    public int identifier;

    @Nullable
    public Bitmap mapper;


    public boolean isCompleted;
    public Date creationDate;
    public String id;

    public TodoItem(String title, boolean isCompleted, Date creationDate) {
        this.title = title;
        this.isCompleted = isCompleted;
        this.creationDate = creationDate;
        //mapper = BitmapFactory.de
       // Bitmap b = BitmapFactory.decode
       // this.identifier = Integer.valueOf(UUID.randomUUID().toString());
    }


}
