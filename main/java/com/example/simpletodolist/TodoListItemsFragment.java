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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoListItemsFragment extends Fragment {
    public static String ITEMNAMES = "itemnames";
    public static String STATUS = "status";
    public static String CREATIONDATE = "creationdate";
    private String TAG = "todolistitemsfragment";
    private RecyclerView recyclerView;
    private ToDoItemAdapter adapter;
    private List<TodoItem> itemsList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<String> titles = getArguments().getStringArrayList(ITEMNAMES);
        Object[] status  = (Object[]) getArguments().getSerializable(STATUS);
        Object[] creationDate  = (Object[]) getArguments().getSerializable(CREATIONDATE);
        Log.i(TAG, "Length " + status.length);
        Log.i(TAG, "Length " + creationDate.length);
        Log.i(TAG, "Length " + titles.size());
        createItems(titles, status, creationDate);


        // Object <Date> creationDate = (List<Date>) getArguments().getSerializable(CREATIONDATE);
        for (String item: titles) {
            Log.i(TAG, item.toString());
        }
        Log.i(TAG, "created todolistitemsfragment");
    }

    private void createItems(List<String> titles, Object[] status, Object[] creationDates) {
        for (int i = 0; i < 100; i++) {
            itemsList.add(new TodoItem(titles.get(i), (Boolean)status[i], (Date)creationDates[i]));

        }

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todo_items, container, false);
        recyclerView = v.findViewById(R.id.todo_items_page);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ToDoItemAdapter(itemsList);
        recyclerView.setAdapter(adapter);


        return v;
    }

    public static TodoListItemsFragment newInstance(ArrayList<String> itemNames, List<Boolean> status, List<Date> creationDates) {
        Bundle b = new Bundle();
        b.putStringArrayList(ITEMNAMES, itemNames);
        b.putSerializable(STATUS, status.toArray());
        b.putSerializable(CREATIONDATE, creationDates.toArray());
        TodoListItemsFragment fragment = new TodoListItemsFragment();
        fragment.setArguments(b);
        return fragment;
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
