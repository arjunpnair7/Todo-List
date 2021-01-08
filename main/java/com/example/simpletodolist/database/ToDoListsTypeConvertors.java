package com.example.simpletodolist.database;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.room.TypeConverter;
import androidx.room.processor.Context;

import com.example.simpletodolist.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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

    @TypeConverter
    public byte[] fromBitmap(Bitmap bitmap) {
       if (bitmap == null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
           // Bitmap map = BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.ic_list_camera);
            //map.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return outputStream.toByteArray();
        } else {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            return outputStream.toByteArray();
        }


    }
    @TypeConverter
    public Bitmap toBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }


}
