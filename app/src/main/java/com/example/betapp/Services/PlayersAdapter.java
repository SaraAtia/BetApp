package com.example.betapp.Services;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.betapp.CreateGroup;
import com.example.betapp.GamesList;
import com.example.betapp.R;

import java.util.ArrayList;
import java.util.HashMap;

import static android.view.View.inflate;

public class PlayersAdapter extends BaseAdapter {

    private final Context m_context;
    private final ArrayList<CheckBox> m_items_arr;
    private final HashMap<Integer, Boolean> mPlayers_selected; //position:isChecked
//    private final HashMap<Object, Object> checked_map;

    public PlayersAdapter(Context c, ArrayList<CheckBox> items, HashMap<Integer, Boolean> games_selected){
        this.m_context = c;
        this.m_items_arr = items;
        this.mPlayers_selected = games_selected;
       /* this.checked_map = new HashMap<>(checkbox_amount);
        for(int i = 0; i< checkbox_amount; i++){
            checked_map.put(i, ((CheckBox)items.get(i)).isChecked());
        }*/
    }
    @Override
    public int getCount() {
        return m_items_arr.size();
    }

    @Override
    public Button getItem(int position) {
        return m_items_arr.get(position);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Button b;
       /* if (position < getCount()-1){
            view = inflate(m_context, R.layout.checkbox_item, null);
            b = view.findViewById(R.id.checkbox_item);
            ((CheckBox) b).setChecked(this.checked_map.get(position));
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickCheckbox(v);
                }});
        } else {
            view = inflate(m_context, R.layout.button_item, null);
            b = view.findViewById(R.id.button_item);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OnClickSubmit(v);
                }});
        }
        b.setText(this.getItem(position).getText());
        b.setTag(position);
        this.m_items_arr.get(position).setTag(position);*/
        return null;
    }
}
