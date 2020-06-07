package com.example.betapp.Services;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.betapp.AllGamesPresentation;
import com.example.betapp.GeneralRankingTable;
import com.example.betapp.R;

public class GroupPresentation extends AppCompatActivity {
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_presentation);
        TextView groups_code_obj = (TextView)findViewById(R.id.groups_code);
        this.code = getIntent().getStringExtra("groupID");
        groups_code_obj.setText("Code: "+ code);
    }

    /**
     * on click.
     * @param view
     */
    public void openRankTable(View view){
        Intent intent = new Intent(this, GeneralRankingTable.class);
        intent.putExtra("groupID", this.code);
        startActivity(intent);
    }

    /**
     * on click.
     * @param view
     */
    public void openAllGames(View view){
        Intent intent = new Intent(this, AllGamesPresentation.class);
        intent.putExtra("groupID", this.code);
        startActivity(intent);
    }
}
