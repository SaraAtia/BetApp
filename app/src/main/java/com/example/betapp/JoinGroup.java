package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.betapp.Services.Group;
import com.example.betapp.Services.User;

public class JoinGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
    }
    public void enterCode(View view) {
        EditText text_code = (EditText) findViewById(R.id.code);
        String code = text_code.getText().toString(); // code = groupID
        Group group = Group.getGroup(code);
        User user = AuthActivity.mUser;
        user.addGroup(group);
        group.addUser(user.getUserID());
        Intent intent = new Intent(this, Gamble.class);
        intent.putExtra("groupId", code);
        startActivity(intent);
    }
}
