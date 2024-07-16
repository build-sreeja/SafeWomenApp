package com.example.women_safetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {
    EditText e_name, e_no;
    Button breg;
    String u_name,u_no;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    User_info user_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        e_name = findViewById(R.id.editTextTextPersonName2);
        e_no = findViewById(R.id.editTextPhone2);
        breg = findViewById(R.id.button4);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        user_info=new User_info();
        breg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                u_name = e_name.getText().toString();
                u_no = e_no.getText().toString();
                if(TextUtils.isEmpty(u_name) && TextUtils.isEmpty(u_no)) {
                    Toast.makeText(Signup.this,"Please add some data",Toast.LENGTH_LONG).show();
                }
                else {
                    addDataFirebase();
                }
            }
        });
    }

    private void addDataFirebase() {
        user_info.setUser_name(u_name);
        user_info.setUser_mob_no(u_no);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.child(u_name).setValue(user_info);
                Toast.makeText(Signup.this,"Registration Successful",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Signup.this,signup_signin.class);
                startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Signup.this,"Registration Unsuccesfull",Toast.LENGTH_LONG).show();
            }
        });
    }
}