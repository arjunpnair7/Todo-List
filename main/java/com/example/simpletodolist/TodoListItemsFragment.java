package com.example.simpletodolist;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
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
    public static int REQUEST_IMAGE_CAPTURE = 1;
    private static ImageView itemImage;
    private static Bitmap itemImageBitmap;
    private static TodoItem currentItem;




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
                ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    private Drawable icon;
                    private ColorDrawable background;
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        Log.i(TAG, "Current item id: " + currentItemIdentifier);
                        executor.execute(new Runnable() {
                            @Override
                            public void run() {
                                TodoListFragment.database.toDoListDao().deleteItemByID(currentItemIdentifier);

                            }
                        });
                    }

                    @Override
                    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                        super.onChildDraw(c, recyclerView, viewHolder, dX,
                                dY, actionState, isCurrentlyActive);
                        icon = ContextCompat.getDrawable(getContext(),
                                R.drawable.ic_list_trash);
                        background = new ColorDrawable(Color.RED);
                        View itemView = viewHolder.itemView;
                        int backgroundCornerOffset = 20;
                        int iconMargin = (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                        int iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                        int iconBottom = iconTop + icon.getIntrinsicHeight();

                        if (dX > 0) { // Swiping to the right
                            int iconLeft = itemView.getLeft() + iconMargin + icon.getIntrinsicWidth();
                            int iconRight = itemView.getLeft() + iconMargin + 50;
                            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                            background.setBounds(itemView.getLeft(), itemView.getTop(),
                                    itemView.getLeft() + ((int) dX) + backgroundCornerOffset,
                                    itemView.getBottom());
                        } else if (dX < 0) { // Swiping to the left
                            int iconLeft = itemView.getRight() - iconMargin - icon.getIntrinsicWidth();
                            int iconRight = itemView.getRight() - iconMargin;
                            icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                            background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                                    itemView.getTop(), itemView.getRight(), itemView.getBottom());
                        } else { // view is unSwiped
                            background.setBounds(0, 0, 0, 0);
                        }


                        background.draw(c);
                        icon.draw(c);
                    }
                };
                new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);


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



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            currentItem.mapper = imageBitmap;
            currentItem.firstAppearance++;
            //itemImage.setImageBitmap(imageBitmap);
            //itemImageBitmap = imageBitmap;
            //currentItem.mapper = itemImageBitmap;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    TodoListFragment.database.toDoListDao().updateItemStatus(currentItem);
                    //adapter.notifyDataSetChanged();
                }
            });
            adapter.notifyDataSetChanged();

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_todo_items, container, false);
        recyclerView = v.findViewById(R.id.todo_items_page);
        completedRecyclerView = v.findViewById(R.id.completedRecyclerview);
        itemImage = new ImageView(getContext());
        itemImage.setImageResource(R.drawable.ic_list_camera);
        Log.i(TAG, "set imageResource to camera");

        fab = v.findViewById(R.id.items_fab);
        ViewModelProvider provider = new ViewModelProvider(TodoListItemsFragment.this);
         todoItemsViewModel = provider.get(TodoItemsViewModel.class);
         TodoItemsViewModel.retrieveItems(associatedId, true);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                manager.getOrientation());
        mDividerItemDecoration.setDrawable(getContext().getDrawable(R.drawable.item_divider));
        recyclerView.addItemDecoration(mDividerItemDecoration);

        LinearLayoutManager manager2 = new LinearLayoutManager(getContext());
        completedRecyclerView.setLayoutManager(manager2);
        DividerItemDecoration mDividerItemDecoration2 = new DividerItemDecoration(completedRecyclerView.getContext(),
                manager2.getOrientation());
        mDividerItemDecoration2.setDrawable(getContext().getDrawable(R.drawable.item_divider));
        completedRecyclerView.addItemDecoration(mDividerItemDecoration2);

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
       // TodoListItemsFragment.

         ImageButton imageButton = itemView.findViewById(R.id.item_imagebutton);

        //TextView date = itemView.findViewById(R.id.item_date);
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

            itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    currentItemIdentifier = item.identifier;
                    return false;
                }
            });

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "Imagebutton clicked");
                    currentItem = item;
                    takePicture();
                }
            });

        }

        private void takePicture() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            try {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            } catch (ActivityNotFoundException e) {
                // display error state to the user
            }
        }





        private void update(TodoItem item) {
            title.setText(item.title);
            Log.i(TAG, "item's primary key: " + item.identifier);
            if ((item.isCompleted)) {
                checkBox.setChecked(true);
                title.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else {
                checkBox.setChecked(false);
            }
            itemImage.setImageBitmap(item.mapper);
            imageButton.setImageDrawable(itemImage.getDrawable());
           // if (itemImage != null) {
                //itemImage.setImageBitmap();
               // imageButton.setImageDrawable(itemImage.getDrawable());
                //imageButton.setImageBitmap(currentItem.mapper);
               // Log.i(TAG, "item mapper is not null");

           // } else {
           //     imageButton.setImageResource(R.drawable.ic_list_camera);
           // }
            Log.i(TAG, "updated");


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
            ToDoItemHolder holder = new ToDoItemHolder(v);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull ToDoItemHolder holder, int position) {
            TodoItem currentItem = items.get(position);
            holder.item = currentItem;
          // holder.imageButton.setImageBitmap(currentItem.mapper);
            Log.i(TAG, "binded");
            holder.update(currentItem);
            if (currentItem.firstAppearance == 0) {
                holder.imageButton.setImageResource(R.drawable.ic_list_camera);
            }
            //holder.imageButton.setImageResource(R.drawable.ic_list_camera);

        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

}
