package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.betapp.Services.Group;
import com.example.betapp.Services.User;

/**
 * Name Group activity set groups name and update it in DB.
 */
public class NameGroup extends AppCompatActivity {
    private String m_groupID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_group);
    }

    /**
     * On click OK save group's name and update group's state to active.
     * @param view
     */
    public void nameGroup(View view) {
        EditText text_name = (EditText) findViewById(R.id.group_name);
        String group_name = text_name.getText().toString();
        Group group = CreateGroup.getGroup();
        group.setGroupState(true);
        group.setGroupName(group_name);
        User curr_user = AuthActivity.mUser;
        group.addUser(curr_user.getUserID());
        curr_user.addGroup(group);
        Intent intent = new Intent(this, Gamble.class);
        intent.putExtra("group_name", group_name);
        startActivity(intent);
    }
}
