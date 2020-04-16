package com.example.betapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class popupPlayersList extends AppCompatActivity {
    LinearLayout linearLayout;
    static ArrayList<String> players = new ArrayList<>();
    static ArrayList<CheckBox> checkBoxes_arr = new ArrayList<>();
    static ArrayList<Integer> d = new ArrayList<>();
    CheckBox c1, c2, c3, c4, c5;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        linearLayout = (LinearLayout)findViewById(R.layout.activity_popup_players_list);

        setContentView(R.layout.activity_popup_players_list);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8),(int)(height*.75));
        if(!checkBoxes_arr.isEmpty()){
            for(CheckBox c: checkBoxes_arr){
                c.setChecked(true);
            }
        }
        else {
            c1 = (CheckBox)findViewById(R.id.checkbox1);
            c2 = (CheckBox)findViewById(R.id.checkbox2);
            c3 = (CheckBox)findViewById(R.id.checkbox3);
            c4 = (CheckBox)findViewById(R.id.checkbox4);
            c5 = (CheckBox)findViewById(R.id.checkbox5);
        }



    }


    public void addToArrays(CheckBox c){
        players.add(c.getText().toString());
        checkBoxes_arr.add(c);
        d.add(1);
    }

    public void removeFromArrays(CheckBox c){
        players.remove(c.getText().toString());
        checkBoxes_arr.remove(c);
    }

    public void checkFunc(View view){
        boolean checked = ((CheckBox) view).isChecked();

        switch(view.getId()) {
            case R.id.checkbox1:
                if (checked)
                    addToArrays(c1);
                else
                    removeFromArrays(c1);
                break;
            case R.id.checkbox2:
                if (checked)
                    addToArrays(c2);
                else
                    removeFromArrays(c2);
                break;
            case R.id.checkbox3:
                if (checked)
                    addToArrays(c3);
                else
                    removeFromArrays(c3);
                break;
            case R.id.checkbox4:
                if (checked)
                    addToArrays(c4);
                else
                    removeFromArrays(c4);
                break;
        }
    }
}
