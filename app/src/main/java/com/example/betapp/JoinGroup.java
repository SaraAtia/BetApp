package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class JoinGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
    }
    public void enterCode(View view) {
        EditText text_code = (EditText) findViewById(R.id.code);
        String code = text_code.getText().toString();
        System.out.println(code);
        Intent intent = new Intent(this, GambleWin.class);
        intent.putExtra("groupId", code);
        startActivity(intent);
        //Todo: use code to sign into group
    }
}
