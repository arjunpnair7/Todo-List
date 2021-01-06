package com.example.simpletodolist;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.simpletodolist.database.TodolistsDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TodoListFragment extends Fragment implements NewListAlertDialogFragment.NewListDialogListener {
    private String TAG = "TodoListFragment";
    private TodoListsViewModel todoListsViewModel;
    private RecyclerView recyclerView;
    private TodoListAdapter adapter;
    private callbacks callbacks;
    private FloatingActionButton fab;
    public String inputtedTitle;
    public static TodolistsDatabase database;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    public void onDialogPositiveClick(String inputtedTitle) {
        this.inputtedTitle = inputtedTitle;
        ToDoList list = new ToDoList(inputtedTitle);

        executor.execute(new Runnable() {
            @Override
            public void run() {
                database.toDoListDao().addToDoList(list);
            }
        });
       // database.toDoListDao().addToDoList(list);
        //adapter.notifyDataSetChanged();
        //adapter.notifyItemInserted(todoListsViewModel.myList.size()-1);
        //adapter = new TodoListAdapter(todoListsViewModel.myList);
        //recyclerView.setAdapter(adapter);

        Log.i(TAG, "TodoList fragment received title: " + inputtedTitle);
    }

    public interface callbacks {
         void onListClicked(ArrayList<String> itemNames, List<Boolean> status, List<Date> creationDates);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = Room.databaseBuilder(getContext(), TodolistsDatabase.class, "todolistsdatabase").fallbackToDestructiveMigration().build();
        Log.i(TAG, "TodoList Fragment created");

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callbacks = (callbacks) context;
    }

    @Override
    public void onStart() {
        super.onStart();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "FAB clicked");
                DialogFragment fragment = new NewListAlertDialogFragment();
                fragment.setTargetFragment(TodoListFragment.this, 0);
                fragment.show(getFragmentManager(), "newlist");
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todo_list, container, false);
        ViewModelProvider provider = new ViewModelProvider(TodoListFragment.this);
        todoListsViewModel = provider.get(TodoListsViewModel.class);
        recyclerView = v.findViewById(R.id.todo_items);
        fab = v.findViewById(R.id.fab);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        //adapter = new TodoListAdapter(todoListsViewModel.myList);

        Log.i(TAG, todoListsViewModel.myList.toString());
        //recyclerView.setAdapter(adapter);
        return v;
    }



    public static TodoListFragment newInstance() {
        //Static method to create a new instance of the TodoList Fragment
        TodoListFragment fragment = new TodoListFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        todoListsViewModel.myList.observe(getViewLifecycleOwner(), new Observer<List<ToDoList>>() {
            @Override
            public void onChanged(List<ToDoList> toDoLists) {
                Log.i(TAG, "data observed");
                adapter = new TodoListAdapter(toDoLists);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private class TodoListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView listTitle = itemView.findViewById(R.id.list_title);
        private ImageView listIcon = itemView.findViewById(R.id.list_icon);
        private ToDoList list;
        private ArrayList<String> itemNames = new ArrayList<>();
        private List<Boolean> status = new ArrayList<>();
        private List<Date> creationDates = new ArrayList<>();

        public TodoListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            //To be added
            //Will trigger a transition to itemList fragment
            //prepareData();
            //callbacks.onListClicked(itemNames, status, creationDates);
           // Log.i(TAG, "itemview clicked");
        }

       /* private void prepareData() {
            List<TodoItem> items = list.todoItems;
            for (TodoItem item: items) {
                itemNames.add(item.title);
                status.add(item.isCompleted);
                creationDates.add(item.creationDate);
            }

        } */



        public void Bind(ToDoList list) {
            listTitle.setText(list.title);
        }
    }

    private class TodoListAdapter extends RecyclerView.Adapter<TodoListViewHolder> {

        private List<ToDoList> adapterLists;

        public TodoListAdapter(List<ToDoList> input) {
            adapterLists = input;
        }

        @NonNull
        @Override
        public TodoListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = getLayoutInflater().inflate(R.layout.list_todo, parent, false);
            return new TodoListViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull TodoListViewHolder holder, int position) {
            ToDoList list = adapterLists.get(position);
            holder.Bind(list);
            holder.list = list;

        }

        @Override
        public int getItemCount() {
            return adapterLists.size();
        }
    }
}
