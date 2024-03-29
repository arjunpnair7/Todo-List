package com.example.simpletodolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements TodoListFragment.callbacks {
    private String TAG = "mainactivity";
    Fragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Get current fragment and make sure that it is not null
         currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        //Create a new fragment if fragment is null
        if (currentFragment == null) {
            currentFragment = TodoListFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, currentFragment).commit();
        }
    }

    @Override
    public void onListClicked(String listId) {
        Log.i(TAG, "Name of current list: " + listId);
        //TodoListItemsFragment fragment = TodoListItemsFragment.newInstance(itemNames, status, creationDates);
        TodoListItemsFragment fragment = TodoListItemsFragment.newInstance(listId);
//        TodoItemsViewModel.retrieveItems(listId);
        getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.fade_out).replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        Log.i(TAG, "transitioned to todolistitemsfragment");

    }


}