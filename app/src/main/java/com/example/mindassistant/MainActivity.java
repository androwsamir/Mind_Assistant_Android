package com.example.mindassistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mindassistant.Database.Lists;
import com.example.mindassistant.Database.MindAssistantDB;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements DialogCloseListener {

    ListView listView;
    SQLiteDatabase db;
    MindAssistantDB obj;
    ArrayList<Lists> arrayList;
    public static boolean isActionMode =false;
    public static ArrayList<Lists> UserSelection = new ArrayList<Lists>();
    public static ActionMode actionMode =null ;
    MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        obj = new MindAssistantDB(this);
        listView = (ListView) findViewById(R.id.listview);
        String [] texts =  {"MyDay","Important","Completed","Tasks", "MyTherapy","WorkTime"};
        int [] images = {R.drawable.sun,R.drawable.star,R.drawable.check,R.drawable.home,R.drawable.first_aid_kit,R.drawable.clockwise};
        arrayList = new ArrayList<Lists>();
        /*obj.deletelist("1");
        obj.deletelist("2");
        obj.deletelist("3");
        obj.deletelist("4");
        obj.deletelist("5");
        obj.deletelist("6");*/
        /*for(int i =0;i<texts.length;i++){
            obj.insert_list_names(texts[i], images[i], 0);
        }*/
        //sqLiteDatabase = obj.getReadableDatabase();
        //sqLiteDatabase.delete("lis", null, null);
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS list");

      //  Toast.makeText(this, obj.Get_lists_names().size() + "", Toast.LENGTH_SHORT).show();
        arrayList = obj.Get_lists_names();
        /*for(int i = 0;i<arrayList.size();i++){
            Toast.makeText(this, arrayList.get(i).getId() + " " + arrayList.get(i).getText(), Toast.LENGTH_SHORT).show();
        }*/
        Add a = new Add(arrayList);
        listView.setAdapter(a);
        findViewById(R.id.fabAdd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewList.newInstance().show(getSupportFragmentManager(), "ActionBottomDialog");
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if(i == 5){
                    Intent intent = new Intent(MainActivity.this, workTime.class);
                    //AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    //Intent intent = new Intent(MainActivity.this, AlertReceiver.class);
                    intent.putExtra("tablename", arrayList.get(i).getText());
                    //intent.putExtra("activityName", "workTime.class");
                    //PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
                    //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, pendingIntent);

                    startActivity(intent);
                }

                else {
                    Intent intent = new Intent(MainActivity.this, Tasks.class);
                    intent.putExtra("tablename", arrayList.get(i).getText());
                    //notificationHelper.showNotification(MainActivity.this,"InActivity", "Done", intent, 0);

                    startActivity(intent);
                }
            }
        });

        Switch mySwitch = findViewById(R.id.switch1) ;
        mySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mySwitch.isChecked())
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
                else
                {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                }
            }
        });



        AbsListView.MultiChoiceModeListener modeListener = new AbsListView.MultiChoiceModeListener() {
            ArrayList<Lists> arrayList ;

            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

            }

            @Override
            public boolean onCreateActionMode(ActionMode Mode, Menu menu) {
                MenuInflater inflater = Mode.getMenuInflater();
                inflater.inflate(R.menu.menu1,menu);
                //Toast.makeText(MainActivity.this,  Mode+" ", Toast.LENGTH_SHORT).show();
                isActionMode=true;
                actionMode = Mode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                arrayList = obj.Get_lists_names();
                switch (item.getItemId())
                {
                    case R.id.List_delete:
                        for(Lists arr : UserSelection)
                        {
                            //Toast.makeText(MainActivity.this,  arr.getId()+ "", Toast.LENGTH_SHORT).show();
                            obj.deleteList(arr.getId(), arr.getText());
                        }
                        arrayList = obj.Get_lists_names();
                        Add a = new Add(arrayList);
                        listView.setAdapter(a);
                        UserSelection.clear();
                        mode.finish();
                        return true;


                    case R.id.List_edit:
                        Bundle bundle = new Bundle();
                        for(Lists arr : UserSelection) {
                            Toast.makeText(MainActivity.this, arr.getText(), Toast.LENGTH_SHORT).show();
                            bundle.putInt("id",arr.getId());
                            bundle.putString("name",arr.getText());
                            AddNewList addNewList = new AddNewList();
                            addNewList.setArguments(bundle);
                            addNewList.show(getSupportFragmentManager(), "ActionBottomDialog");
                        }
                        UserSelection.clear();
                        MainActivity.actionMode.setTitle(MainActivity.UserSelection.size() + " Items selected..");

                    default:
                        return false;


                }

            }

            @Override
            public void onDestroyActionMode(ActionMode Mode) {

                UserSelection.clear();
                isActionMode=false;
                actionMode = null;

            }
        };

        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(modeListener);

    }

    @Override
    public void handleDialogClose(DialogInterface dialog){
        arrayList = obj.Get_lists_names();
        //Collections.reverse(arrayList);
        Add a = new Add(arrayList);
        listView.setAdapter(a);
    }

    public class Add extends BaseAdapter{

        ArrayList<Lists> arrayList = new ArrayList<Lists>();

        public Add(ArrayList<Lists> v){
            this.arrayList = v;
        }
        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object getItem(int i) {
            return arrayList.get(i).getText();
        }

        @Override
        public long getItemId(int i) {
            return i;
        }


        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater ly = getLayoutInflater();
            View view1 = ly.inflate(R.layout.item_list, null);
            TextView t1 = view1.findViewById(R.id.row);

            ImageView img = (ImageView) view1.findViewById(R.id.imageView);
            t1.setText(String.valueOf(arrayList.get(i).getText()));
            img.setImageResource(arrayList.get(i).getImg());

            CheckBox checkBox=view1.findViewById(R.id.checkBox);
            checkBox.setTag(i);
            if(MainActivity.isActionMode)
            {
                checkBox.setVisibility(View.VISIBLE);
            }
            else
            {
                checkBox.setVisibility(View.GONE);
            }

            if(i>=0 && i<=5) {
              checkBox.setEnabled(false);
                //  checkBox.setVisibility(View.GONE);
            }

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                    int position = (int)compoundButton.getTag();

                    //Toast.makeText(MainActivity.this, compoundButton.getTag() +" ", Toast.LENGTH_SHORT).show();
                    if(MainActivity.UserSelection.contains(arrayList.get(i)))
                    {
                        MainActivity.UserSelection.remove(arrayList.get(i));
                    }
                    else
                    {
                        MainActivity.UserSelection.add(arrayList.get(i));
                    }
                    MainActivity.actionMode.setTitle(MainActivity.UserSelection.size()+" Items selected..");
                }
            });

            return view1;
        }
    }
}