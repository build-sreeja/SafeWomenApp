package com.example.women_safetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//*************Screen 3*************
public class register extends AppCompatActivity {

    EditText name,contact;
    Button insert,update,delete,view;
    DBHandler DB;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=findViewById(R.id.name);
        contact=findViewById(R.id.contact);
        insert=findViewById(R.id.btnInsert);
        update=findViewById(R.id.btnUpdate);
        delete=findViewById(R.id.btnDelete);
        view=findViewById(R.id.btnView);
        DB=new DBHandler(this);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTXT=name.getText().toString();
                String contactTXT=contact.getText().toString();
                Boolean checkinsertdata=DB.insertuserdata(nameTXT,contactTXT);
                if(checkinsertdata==true)
                {
                    Toast.makeText(register.this,"New data inserted",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(register.this,"New data not inserted",Toast.LENGTH_LONG).show();
                }
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTXT=name.getText().toString();
                String contactTXT=contact.getText().toString();

                Boolean checkupdatedata=DB.updateuserdata(nameTXT,contactTXT);
                if(checkupdatedata==true)
                {
                    Toast.makeText(register.this,"Data updated",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(register.this,"Data not updated",Toast.LENGTH_LONG).show();
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameTXT=name.getText().toString();
                Boolean checkdeletedata=DB.deletedata(nameTXT);
                if(checkdeletedata==true)
                {
                    Toast.makeText(register.this,"Data deleted",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(register.this,"Data not deleted",Toast.LENGTH_LONG).show();
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(register.this,MainActivity3.class);
                startActivity(intent);

            }
        });
    }
    }
