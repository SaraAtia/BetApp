package com.example.betapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.betapp.Services.Group;
import com.example.betapp.Services.GroupPresentation;
import com.example.betapp.Services.HttpService.HttpService;
import com.example.betapp.Services.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import static com.example.betapp.Consts.GROUPS_DATABASE;
import static com.example.betapp.Consts.Error_FLAG;

public class JoinGroup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);
    }
    public void enterCode(View view) {
        EditText text_code = (EditText) findViewById(R.id.code);
        String code = text_code.getText().toString(); // code = groupID
        Error_FLAG flag = Group.addUser(code, AuthActivity.mUser.user_ID);
        if (flag == Error_FLAG.NO_ERROR){
            User user = AuthActivity.mUser;
            try {
                String group_name = (String) ((JSONObject) HttpService.getInstance().
                        getJSON(GROUPS_DATABASE).get(code)).get("groupName");
                user.addGroup(group_name, code);
                Intent intent = new Intent(this, GroupPresentation.class);
                intent.putExtra("groupID", code);
                startActivity(intent);
            } catch (InterruptedException | ExecutionException| JSONException e) {
                e.printStackTrace();
            }

        } else {
            if(flag == Error_FLAG.WRONG_CODE){
                Toast.makeText(this, "Wrong Code", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Already in Group", Toast.LENGTH_SHORT).show();
            }
        }


    }
}
