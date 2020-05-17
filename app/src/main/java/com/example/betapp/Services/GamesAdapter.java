package com.example.betapp.Services;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.example.betapp.CreateGroup;
import com.example.betapp.GamesList;
import com.example.betapp.NameGroup;
import com.example.betapp.R;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static android.view.View.inflate;

/**
 * responsible for representation of all objects in ListView and OnClick logic.
 */
public class GamesAdapter extends BaseAdapter{
    private Context m_context;
    private Group m_group;
    private ArrayList<Button> m_items_arr;
    private HashMap<Integer, Boolean> checked_map;
    private JSONArray m_events;
    private int MAX_GAMES_TO_CHOOSE = 3;
    private final GamesList m_games_listJSON;
    public GamesAdapter(Context c, ArrayList<Button> items, JSONArray events, GamesList gamesList ){
        this.m_context = c;
        this.m_items_arr = items;
        this.m_events = events;
        this.m_games_listJSON = gamesList;
        this.m_group = CreateGroup.getGroup();
        int checkbox_amount = m_items_arr.size()-1;
        this.checked_map = new HashMap<>(checkbox_amount);
        for(int i = 0; i< checkbox_amount; i++){
            checked_map.put(i, ((CheckBox)items.get(i)).isChecked());
        }
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
        /*int id = m_items_arr.get(position).getId();
        if ( position == id){ //todo:delete
            System.out.println("Position is ID");
        }
        return id;*/
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
        if (position < getCount()-1){
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
        this.m_items_arr.get(position).setTag(position);
        return view;
    }
    public HashMap<Integer, Boolean> getChecked_map() {
        return checked_map;
    }

    /**
     * When clicking a checkbox update its status in checked_map.
     * Present the user a correspondent message
     * @param box
     */
    private void onClickCheckbox(View box){
        CheckBox event = (CheckBox) box;
        Toast toast;
        boolean checked = event.isChecked();
        if(checked){
            this.checked_map.put((Integer) event.getTag(), true);
            if(CreateGroup.getCounter() > MAX_GAMES_TO_CHOOSE){
                event.setChecked(false);
                this.checked_map.put((Integer) event.getTag(), false);
                toast = Toast.makeText(m_context, "Can't select more than "+MAX_GAMES_TO_CHOOSE, Toast.LENGTH_SHORT);
            } else {
                CreateGroup.increaseCounter();
                toast = Toast.makeText(m_context, event.getText()+" selected", Toast.LENGTH_SHORT);
            }
        } else {
            this.checked_map.put((Integer) event.getTag(), false);
            CreateGroup.decreaseCounter();
            toast = Toast.makeText(m_context, event.getText()+" removed", Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    /**
     * When clicking submit btn - pull from API info of games selected and insert info to DB.
     * @param view
     */
    public void OnClickSubmit(View view){
        HashMap<String, String> games_selected = new HashMap<>(); //string = group ID
        String groupID = this.m_group.getGroupID();
        for (int j = 0; j<this.m_items_arr.size()-1; j++){
            CheckBox game =(CheckBox) this.m_items_arr.get(j);
            // if checkbox is selected - add to games map.
            if(this.checked_map.get(j)){
                int i = (Integer) game.getTag();
                LinkedHashMap<String, String> details = new LinkedHashMap<>(8);
                try{
                    String idEvent = m_events.getJSONObject(i).getString("idEvent");
                    String strEvent = m_events.getJSONObject(i).getString("strEvent");
                    String idLeague = m_events.getJSONObject(i).getString("idLeague");
                    String strLeague = m_events.getJSONObject(i).getString("strLeague");
                    String strHomeTeam = m_events.getJSONObject(i).getString("strHomeTeam");
                    String strAwayTeam = m_events.getJSONObject(i).getString("strAwayTeam");
                    String dateEvent = m_events.getJSONObject(i).getString("dateEvent");
                    String strTimeLocal = m_events.getJSONObject(i).getString("strTimeLocal");
                    Game g = new Game(groupID, dateEvent, idEvent, strEvent);
                    String game_entry = Game.uploadToDB(g);
                    games_selected.put(game_entry, g.mGame_name);
                    /*boolean added = m_games_listJSON.addGame(String.valueOf(i), details);
                    if (!added){
                        System.out.println("didn't add games chosen");
                        //Toast.makeText(this, "added succesfuly"); //TODO: notification
                    }*/
                } catch (Exception e){
                    System.out.println(this.toString()+" line 183");
                }
            }
        }
        this.m_group.setGroupGames(games_selected);
//        this.m_games_listJSON.uploadToDB();
        Intent intent = new Intent(m_context, NameGroup.class);
        m_context.startActivity(intent);
    }
}
