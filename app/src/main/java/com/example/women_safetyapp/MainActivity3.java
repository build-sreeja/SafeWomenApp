package com.example.women_safetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
//***************Screen 4******************
public class MainActivity3 extends AppCompatActivity {

    DBHandler DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        DB=new DBHandler(this);
        Cursor cursor=DB.getdata();
        StringBuffer buffer[]=new StringBuffer[cursor.getCount()];
        for(int i=0;i<cursor.getCount();i++)
        {
            buffer[i]=new StringBuffer();
        }
        int i=0;
        while (cursor.moveToNext())
        {
            buffer[i].append(cursor.getString(0));
            buffer[i].append("\n");
            buffer[i].append(cursor.getString(1));
            i++;
        }
        ArrayAdapter adapter=new ArrayAdapter<StringBuffer>(this,R.layout.text_color_layout,buffer);
        ListView listView=findViewById(R.id.lv);
        listView.setAdapter(adapter);
    }
}