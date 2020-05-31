package com.example.betapp.Services;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.betapp.Gamble;
import com.example.betapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.inflate;

public class PlayersAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList<CheckBox> mPlayers_view;
    private final HashMap<Integer, Boolean> mChecked_map; //position:isChecked - contain all players

    public PlayersAdapter(Context c, ArrayList<CheckBox> items, String teamID){
        this.mContext = c;
        this.mPlayers_view = items;
        int players_size = mPlayers_view.size();

        if(Gamble.teamPlayersChecked.get(teamID) == null) {
            this.mChecked_map = new HashMap<>();
            for (int i = 0; i < players_size; i++) {
                mChecked_map.put(i, false);
            }
            Gamble.teamPlayersChecked.put(teamID, mChecked_map);
        } else {
            this.mChecked_map = Gamble.teamPlayersChecked.get(teamID);
        }
    }
    @Override
    public int getCount() {
        return mPlayers_view.size();
    }

    @Override
    public Button getItem(int position) {
        return mPlayers_view.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get item info for presentation.
     * Pull data on checkboxes from map.
     * @param position in list
     * @param convertView
     * @param parent
     * @return item updated with its values and status
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        CheckBox b;
        View view = inflate(mContext, R.layout.checkbox_item, null);
        b = view.findViewById(R.id.checkbox_item);
        b.setChecked(this.mChecked_map.get(position));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    mChecked_map.put(position, true);
                } else {
                    mChecked_map.put(position, false);
                }
            }
        });
        b.setText(this.getItem(position).getText());
        b.setTag(this.mPlayers_view.get(position).getTag());
        return view;
    }
    public ArrayList<CheckBox> getPlayersView() {
        return mPlayers_view;
    }
}
