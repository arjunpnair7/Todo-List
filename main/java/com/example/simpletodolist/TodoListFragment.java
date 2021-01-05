package com.example.simpletodolist;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoListFragment extends Fragment {
    private String TAG = "TodoListFragment";
    private TodoListsViewModel todoListsViewModel;
    private RecyclerView recyclerView;
    private TodoListAdapter adapter;
    private callbacks callbacks;

    public interface callbacks {
         void onListClicked(ArrayList<String> itemNames, List<Boolean> status, List<Date> creationDates);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "TodoList Fragment created");

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        callbacks = (callbacks) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todo_list, container, false);
        ViewModelProvider provider = new ViewModelProvider(TodoListFragment.this);
        todoListsViewModel = provider.get(TodoListsViewModel.class);
        recyclerView = v.findViewById(R.id.todo_items);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TodoListAdapter(todoListsViewModel.myList);
        Log.i(TAG, todoListsViewModel.myList.toString());
        recyclerView.setAdapter(adapter);
        return v;
    }

    public static TodoListFragment newInstance() {
        //Static method to create a new instance of the TodoList Fragment
        TodoListFragment fragment = new TodoListFragment();
        return fragment;
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
            prepareData();
            callbacks.onListClicked(itemNames, status, creationDates);
            Log.i(TAG, "itemview clicked");
        }

        private void prepareData() {
            List<TodoItem> items = list.todoItems;
            for (TodoItem item: items) {
                itemNames.add(item.title);
                status.add(item.isCompleted);
                creationDates.add(item.creationDate);
            }

        }



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
