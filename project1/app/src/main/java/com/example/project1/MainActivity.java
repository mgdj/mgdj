package com.example.project1;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.view.View;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final String TAG="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btn1=findViewById(R.id.btn_change);
        Button btn2=findViewById(R.id.btn2);
        TextView tv1=findViewById(R.id.tv_change);
        EditText et1=findViewById(R.id.edit_text);
        RadioButton rd1=findViewById(R.id.rdmale);
        RadioButton rd2=findViewById(R.id.rdfemale);


        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                tv1.setText("zju ByteDancer yyds");
                et1.setText("blank");
            }
        });
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Log.d(TAG,"you confirm a gender");
            }
        });

        rd1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                rd1.setChecked(true);
                rd2.setChecked(false);
            }
        });
        rd2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                rd2.setChecked(true);
                rd1.setChecked(false);
            }
        });
    }
}