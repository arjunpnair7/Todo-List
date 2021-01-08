package com.example.simpletodolist;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    private ToDoList list;
    public static String currentListId;
    ItemTouchHelper.SimpleCallback itemTouchHelperCallBack;

    @Override
    public void onDialogPositiveClick(String inputtedTitle) {
        //ToDoList current;


        this.inputtedTitle = inputtedTitle;
         list = new ToDoList(inputtedTitle);
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

    @Override
    public void onFinishedEnteringNewName(String newName) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ToDoList current = database.toDoListDao().getToDoListByID(currentListId);
                current.title = newName;
               database.toDoListDao().updateList(current);
                if (current.title.equals(inputtedTitle)) {
                    Log.i(TAG, "same list name");
                }
            }
        });

    }

    public interface callbacks {
         void onListClicked(String listId);
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
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        itemTouchHelperCallBack = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.i(TAG, "swiped: " + currentListId);
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        database.toDoListDao().deleteListById(currentListId);
                        database.toDoListDao().deleteAssociatedItemsForList(currentListId);
                    }
                });
            }
        };

       DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                manager.getOrientation());
       mDividerItemDecoration.setDrawable(getContext().getDrawable(R.drawable.item_divider));
        recyclerView.addItemDecoration(mDividerItemDecoration);




        new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(recyclerView);
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


        public TodoListViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    TodoListFragment.currentListId = list.listID;
                    Log.i(TAG, "long click: " + list.listID);
                    DialogFragment fragment = new NewListAlertDialogFragment();
                    fragment.setTargetFragment(TodoListFragment.this, 0);
                    fragment.show(getFragmentManager(), "updatelistname");
                    return true;
                }
            });
            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.i(TAG, "touched " + list.listID);
                    currentListId = list.listID;
                    return false;
                }
            });



        }
        @Override
        public void onClick(View v) {
            //To be added
            //Will trigger a transition to itemList fragment
            //prepareData();
            inputtedTitle = listTitle.getText().toString();
            callbacks.onListClicked(list.listID);
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
           // TodoListFragment.currentListId = list.listID;
            //inputtedTitle = adapterLists.get(position);

        }

        @Override
        public int getItemCount() {
            return adapterLists.size();
        }
    }
}
