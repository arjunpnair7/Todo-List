package com.example.simpletodolist;

import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodoListItemsFragment extends Fragment implements NewListAlertDialogFragment.NewListDialogListener {
    public static String ITEMNAMES = "itemnames";
    public static String STATUS = "status";
    public static String CREATIONDATE = "creationdate";
    private String TAG = "todolistitemsfragment";
    private RecyclerView recyclerView;
    private RecyclerView completedRecyclerView;
    private String associatedId;
    private ToDoItemAdapter adapter;
    private ToDoItemAdapter completedItemsAdapter;
    private LiveData<List<TodoItem>> itemsList;
    private FloatingActionButton fab;
    public static String ASSOCIATEDID = "associatedid";
    private TodoItemsViewModel todoItemsViewModel;
    public static String NEWITEMTAG = "newitem";
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    public static int currentItemIdentifier;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        associatedId = getArguments().getString(ASSOCIATEDID);
        itemsList = TodoListFragment.database.toDoListDao().getItemsForList(associatedId, false);
        //List<String> titles = getArguments().getStringArrayList(ITEMNAMES);
       // Object[] status  = (Object[]) getArguments().getSerializable(STATUS);
       // Object[] creationDate  = (Object[]) getArguments().getSerializable(CREATIONDATE);
        //Log.i(TAG, "Length " + status.length);
        //Log.i(TAG, "Length " + creationDate.length);
        //Log.i(TAG, "Length " + titles.size());
        //createItems(titles, status, creationDate);


        // Object <Date> creationDate = (List<Date>) getArguments().getSerializable(CREATIONDATE);
       // for (String item: titles) {
        //    Log.i(TAG, item.toString());
       // }
        Log.i(TAG, "created todolistitemsfragment");
    }

  /*  private void createItems(List<String> titles, Object[] status, Object[] creationDates) {
        for (int i = 0; i < 100; i++) {
            itemsList.add(new TodoItem(titles.get(i), (Boolean)status[i], (Date)creationDates[i]));

        }

    }
    *
   */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TodoItemsViewModel.myItems.observe(getViewLifecycleOwner(), new Observer<List<TodoItem>>() {
            @Override
            public void onChanged(List<TodoItem> todoItems) {
                Log.i(TAG, "new uncompleted item livedata");
                TodoItemsViewModel.retrieveItems(associatedId, false);
                adapter = new ToDoItemAdapter(todoItems);
                recyclerView.setAdapter(adapter);
                //completedItemsAdapter = new ToDoItemAdapter(todoItems);
                //completedRecyclerView.setAdapter(completedItemsAdapter);
            }
        });
        TodoItemsViewModel.retrieveCompletedItems(associatedId);
        TodoItemsViewModel.myCompletedItems.observe(getViewLifecycleOwner(), new Observer<List<TodoItem>>() {
            @Override
            public void onChanged(List<TodoItem> todoItems) {
                Log.i(TAG, "new completed item livedata");
                TodoItemsViewModel.retrieveItems(associatedId, true);
                completedItemsAdapter = new ToDoItemAdapter(todoItems);
                completedRecyclerView.setAdapter(completedItemsAdapter);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todo_items, container, false);
        recyclerView = v.findViewById(R.id.todo_items_page);
        completedRecyclerView = v.findViewById(R.id.completedRecyclerview);
        fab = v.findViewById(R.id.items_fab);
        ViewModelProvider provider = new ViewModelProvider(TodoListItemsFragment.this);
         todoItemsViewModel = provider.get(TodoItemsViewModel.class);
         TodoItemsViewModel.retrieveItems(associatedId, true);
         //TodoItemsViewModel.retrieveCompletedItems(associatedId);
         //if (TodoItemsViewModel.myCompletedItems == null) {
         //    Log.i(TAG, "myCompletedItems is null");
        // }
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        completedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
       // adapter = new ToDoItemAdapter(itemsList);
       // recyclerView.setAdapter(adapter);


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "items fab clicked");
                DialogFragment fragment = new NewListAlertDialogFragment();
                fragment.setTargetFragment(TodoListItemsFragment.this, 0);
                fragment.show(getFragmentManager(), NEWITEMTAG);
            }
        });
    }

    public static TodoListItemsFragment newInstance(String id) {
        Bundle b = new Bundle();
        b.putString(ASSOCIATEDID, id);
        TodoListItemsFragment fragment = new TodoListItemsFragment();
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onDialogPositiveClick(String inputtedTitle) {
        Log.i(TAG, "todoitemfragment received click");
        TodoItem item = new TodoItem(inputtedTitle, false, new Date());
        item.id = associatedId;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                TodoListFragment.database.toDoListDao().addItemToList(item);
            }
        });

    }

    @Override
    public void onFinishedEnteringNewName(String newName) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                TodoItem currentItem = TodoListFragment.database.toDoListDao().getToDoItemByID(currentItemIdentifier);
                currentItem.title = newName;
                TodoListFragment.database.toDoListDao().updateItemStatus(currentItem);
            }
        });

    }

    private class ToDoItemHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox = itemView.findViewById(R.id.item_checkbox);
        TextView title = itemView.findViewById(R.id.item_title);
        TextView date = itemView.findViewById(R.id.item_date);
        TodoItem item;


        public ToDoItemHolder(@NonNull View itemView) {
            super(itemView);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClick");
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            item.isCompleted = !item.isCompleted;
                            TodoListFragment.database.toDoListDao().updateItemStatus(item);
                        }
                    });
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    TodoListItemsFragment.currentItemIdentifier = item.identifier;
                    Log.i(TAG, "item clicked" + item.identifier);
                   // TodoListFragment.currentListId = list.listID;
                   // Log.i(TAG, "long click: " + list.listID);
                    DialogFragment fragment = new NewListAlertDialogFragment();
                    fragment.setTargetFragment(TodoListItemsFragment.this, 0);
                    fragment.show(getFragmentManager(), "updateitemname");
                    return true;
                }
            });



        }
        private void update(TodoItem item) {
            title.setText(item.title);
            Log.i(TAG, "item's primary key: " + item.identifier);
            if ((item.isCompleted)) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

        }




    }

    private class ToDoItemAdapter extends RecyclerView.Adapter<ToDoItemHolder> {
        private List<TodoItem> items;

        public ToDoItemAdapter(List<TodoItem> inputList) {
            items = inputList;
        }

        @NonNull
        @Override
        public ToDoItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.list_todo_item, parent, false);

            return new ToDoItemHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ToDoItemHolder holder, int position) {
            TodoItem currentItem = items.get(position);
            holder.item = currentItem;

            holder.update(currentItem);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

}
