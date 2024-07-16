package com.example.women_safetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Exist_user extends AppCompatActivity {
    Button B1;
    EditText E1,E2;
    String uname,uphone;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StringBuffer sb[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exist_user);

        B1=findViewById(R.id.button4);
        E1=findViewById(R.id.editTextTextPersonName2);
        E2=findViewById(R.id.editTextPhone2);
        firebaseDatabase=FirebaseDatabase.getInstance();
        databaseReference=firebaseDatabase.getReference().child("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                sb=new StringBuffer[(int)snapshot.getChildrenCount()];
                for(int i=0;i<sb.length;i++)
                {
                    sb[i]=new StringBuffer();
                }

                int j=0;
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    sb[j].append(dataSnapshot.child("user_name").getValue().toString());
                    sb[j].append(dataSnapshot.child("user_mob_no").getValue().toString());
                    j++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        B1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uname=E1.getText().toString();
                uphone=E2.getText().toString();
                StringBuffer buffer=new StringBuffer(uname);
                buffer.append(uphone);
                String Data=new String(buffer);
                //Toast.makeText(getApplicationContext(),buffer+"",Toast.LENGTH_LONG).show();
                int c=0;
                for(int i=0;i<sb.length;i++)
                {
                    String Data1=new String(sb[i]);
                    if(Data.equalsIgnoreCase(Data1))
                    {
                        Intent intent=new Intent(Exist_user.this,Valid_user_splash.class);
                        intent.putExtra("N",uname);
                        startActivity(intent);
                        c++;
                    }

                }
                if(c==0)
                {
                    Toast.makeText(getApplicationContext(),"Invalid User",Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}