package com.example.ahmed.friender;

import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class UserDataBase extends AppCompatActivity {

    EditText deleteEditText ;
    ListView lv ;
    ArrayList<String> arrayList ;
    ArrayAdapter<String> arrayAdapter;
    UserDataBaseManager userDataBaseManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data_base);
        deleteEditText = (EditText)findViewById(R.id.deleteEditText);
        lv = (ListView) findViewById(R.id.ListViewId);
        userDataBaseManager = new UserDataBaseManager(this);
    }

    public void delete(View view) {
        try
        {
            final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Sure ?");
            alert.setMessage("Are U Sure Of This Operation");
            alert.setIcon(R.drawable.logo);
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(UserDataBase.this , userDataBaseManager.deleteByName(deleteEditText.getText().toString()),Toast.LENGTH_SHORT).show();

                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(UserDataBase.this , "Operation Cancled",Toast.LENGTH_SHORT).show();

                }
            });
            alert.show();
        }
        catch (Exception e)
        {
            Toast.makeText(UserDataBase.this , "Exception "+e.getMessage().toString(),Toast.LENGTH_SHORT).show();
        }
    }

    public void show(View view) {
        arrayList = new ArrayList<>();
        Cursor c = userDataBaseManager.allData();
        while (c.moveToNext()) {
            arrayList.add("Name  = "+c.getString(0) + "\nSecret = " + c.getString(1));
        }
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,android.R.id.text1,arrayList);
        lv.setAdapter(arrayAdapter);
    }
}
