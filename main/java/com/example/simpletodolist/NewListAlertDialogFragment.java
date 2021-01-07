package com.example.simpletodolist;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

//I decided to reuse this class for the alert dialogue that appears when the user is
//is trying to create a new todo list item as the interface is essentially identical

public class NewListAlertDialogFragment extends DialogFragment {
    private String TAG = "newlistalertdialogfragment";
    NewListDialogListener listener;
    EditText editTitle;
    String inputtedTitle = "test";
    TextWatcher titleWatcher;
    //TextView title;

    public interface NewListDialogListener {
        public void onDialogPositiveClick(String inputtedTitle);
        public void onFinishedEnteringNewName(String newName);

    }




    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View v = inflater.inflate(R.layout.fragment_new_list_dialog, null);
        builder.setView(v);
        editTitle = v.findViewById(R.id.editText);

        //title = v.findViewById(R.id.listtitle_itemtitle);

        if (getTag() == TodoListItemsFragment.NEWITEMTAG) {
            editTitle.setHint("Enter item name");
        }
        if (getTag() == "updatelistname") {
            editTitle.setHint("New list name");
        }

        //editTitle = v.findViewById(R.id.editText);
        titleWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i(TAG, "text changed");
                inputtedTitle = s.toString();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        editTitle.addTextChangedListener(titleWatcher);
        builder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG, "User finished entering title: " + editTitle.getText());
                inputtedTitle = editTitle.getText().toString();
                if (getTag() == "updatelistname") {
                    listener.onFinishedEnteringNewName(editTitle.getText().toString());
                    Log.i(TAG, "onfinishedenteringnewname called");
                } else {
                    listener.onDialogPositiveClick(editTitle.getText().toString());

                }

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                NewListAlertDialogFragment.this.getDialog().cancel();
            }
        });

        return builder.create();
    }



    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStart() {
        super.onStart();




    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        listener =  (NewListDialogListener) getTargetFragment();






    }
}
