package com.example.simpletodolist;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoListsViewModel extends ViewModel {

    List<ToDoList> myList = new ArrayList<>();

    public TodoListsViewModel() {
        List<TodoItem> fakeList = createListItems();
        for (int i = 0; i < 100; i++) {
            ToDoList list = new ToDoList("List " + i, fakeList);
            myList.add(list);
        }
    }


    private List<TodoItem> createListItems()  {
        List<TodoItem> returnList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            TodoItem item = new TodoItem("Item " + i, i %2 == 0, new Date());
            returnList.add(item);
        }
        return returnList;
    }
}
