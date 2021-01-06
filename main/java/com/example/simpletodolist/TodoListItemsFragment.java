package com.example.simpletodolist;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
    private String associatedId;
    private ToDoItemAdapter adapter;
    private LiveData<List<TodoItem>> itemsList;
    private FloatingActionButton fab;
    public static String ASSOCIATEDID = "associatedid";
    private TodoItemsViewModel todoItemsViewModel;
    public static String NEWITEMTAG = "newitem";
    private ExecutorService executor = Executors.newSingleThreadExecutor();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        associatedId = getArguments().getString(ASSOCIATEDID);
        itemsList = TodoListFragment.database.toDoListDao().getItemsForList(associatedId);
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
                Log.i(TAG, "new item livedata");
                adapter = new ToDoItemAdapter(todoItems);
                recyclerView.setAdapter(adapter);
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todo_items, container, false);
        recyclerView = v.findViewById(R.id.todo_items_page);
        fab = v.findViewById(R.id.items_fab);
        ViewModelProvider provider = new ViewModelProvider(TodoListItemsFragment.this);
         todoItemsViewModel = provider.get(TodoItemsViewModel.class);
         TodoItemsViewModel.retrieveItems(associatedId);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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

    private class ToDoItemHolder extends RecyclerView.ViewHolder {
        CheckBox checkBox = itemView.findViewById(R.id.item_checkbox);
        TextView title = itemView.findViewById(R.id.item_title);
        TextView date = itemView.findViewById(R.id.item_date);


        public ToDoItemHolder(@NonNull View itemView) {
            super(itemView);

        }
        private void update(TodoItem item) {
            checkBox.setChecked(item.isCompleted);
            title.setText(item.title);
            date.setText(item.creationDate.toString());
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
            holder.update(currentItem);

        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

}
