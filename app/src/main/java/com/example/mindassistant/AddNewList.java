package com.example.mindassistant;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.mindassistant.Database.Lists;
import com.example.mindassistant.Database.MindAssistantDB;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewList extends BottomSheetDialogFragment {

    private EditText newListName;
    private Button addListButton;
    private MindAssistantDB db;
    private SQLiteDatabase s;

    public static AddNewList newInstance(){
        return new AddNewList();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.new_list, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        newListName = getView().findViewById(R.id.newListName);
        addListButton = getView().findViewById(R.id.addList);
        addListButton.setEnabled(false);
        db = new MindAssistantDB(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        //Toast.makeText(getActivity(), bundle.getString("name"), Toast.LENGTH_SHORT).show();
        if(bundle != null){
            isUpdate =true;
            String listName = bundle.getString("name");
            newListName.setText(listName);
            if(listName.length()>0){
                addListButton.setTextColor(ContextCompat.getColor(getContext(), R.color.purple_700));
            }
        }

        newListName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("") || charSequence.toString().equals(" ")) {
                    addListButton.setEnabled(false);
                    addListButton.setTextColor(Color.GRAY);
                }
                else{
                    addListButton.setEnabled(true);
                    addListButton.setTextColor(Color.BLUE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boolean finalIsUpdate = isUpdate;

        addListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String listname = newListName.getText().toString();
                if(finalIsUpdate == true){
                    db.updateListName(bundle.getInt("id"), listname,bundle.getString("name"));
                }
                else{
                    String d = db.insert_list_names(listname, R.drawable.list, 0);
                    s = db.getWritableDatabase();
                    s.execSQL("create table "+listname +" (id INTEGER primary key AUTOINCREMENT,task Text, status INTEGER)");
                    Toast.makeText(getActivity(), d, Toast.LENGTH_SHORT).show();

                }
                dismiss();
            }
        });

    }
    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener){
            ((DialogCloseListener)activity).handleDialogClose(dialog);
        }
    }
}
