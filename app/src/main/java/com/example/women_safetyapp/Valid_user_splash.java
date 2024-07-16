package com.example.women_safetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class Valid_user_splash extends AppCompatActivity {
    TextView T1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_valid_user_splash);
        T1=findViewById(R.id.textView4);

        Intent intent=getIntent();
        String name=intent.getStringExtra("N");
        T1.setText(name);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Valid_user_splash.this== null){
                    return;
                }
                Intent intent = new Intent(getApplicationContext(),MainActivity2.class);
                intent.putExtra("N1",name);
                startActivity(intent);
                finish();
            }
        },2000);
    }
}