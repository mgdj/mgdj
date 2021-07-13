package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class activity_jump extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jump);
        Log.i("activity_jump","OnCreate");
        Button btn=findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(activity_jump.this, "button clicked",Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        Log.i("activity_jump","OnStart");
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.i("activity_jump","OnResume");
    }
    @Override
    protected  void onPause(){
        super.onPause();
        Log.i("activity_jump","OnPause");
    }
    @Override
    protected  void onStop(){
        super.onStop();
        Log.i("activity_jump","OnStop");
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i("activity_jump","OnDestroy");
    }
}