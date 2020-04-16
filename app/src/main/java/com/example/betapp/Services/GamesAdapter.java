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

import com.example.betapp.GambleWin;
import com.example.betapp.GamesList;
import com.example.betapp.JoinGroup;
import com.example.betapp.R;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import static android.view.View.inflate;


public class GamesAdapter extends BaseAdapter{
    private Context m_context;
    private ArrayList<Button> m_itemsArr;
    private HashMap<Integer, Boolean> checkedMap;
    private JSONArray m_events;
    private int m_counter = 0;
    private final GamesList m_gamesListJSON;
    public GamesAdapter(Context c, ArrayList<Button> items, JSONArray events, GamesList gamesList ){
        this.m_context = c;
        this.m_itemsArr = items;
        this.m_events = events;
        this.m_gamesListJSON = gamesList;
        int checkbox_amount = m_itemsArr.size()-1;
        this.checkedMap = new HashMap<>(checkbox_amount);
        for(int i = 0; i< checkbox_amount; i++){
            checkedMap.put(/*m_game.get(i).getId()*/i, false);
        }
    }
    @Override
    public int getCount() {
        return m_itemsArr.size();
    }

    @Override
    public Button getItem(int position) {
        return m_itemsArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        int id = m_itemsArr.get(position).getId();
        if ( position == id){ //todo:delete
            System.out.println("Position is ID");
        }
        return id;
//        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        Button b;
        if (position < getCount()-1){
            view = inflate(m_context, R.layout.checkbox_item, null);
            b = view.findViewById(R.id.checkbox_item);
            ((CheckBox) b).setChecked(this.checkedMap.get(position));
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickGame(v);
                }});
        } else {
            view = inflate(m_context, R.layout.button_item, null);
            b = view.findViewById(R.id.button_item);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showGamesChosen(v);
                }});
        }
        b.setText(this.getItem(position).getText());
        b.setTag(position);
        this.m_itemsArr.get(position).setTag(position);
        return view;
    }
    public HashMap<Integer, Boolean> getCheckedMap() {
        return checkedMap;
    }
    private void onClickGame(View box){
        CheckBox event = (CheckBox) box;
        Toast toast;
        boolean checked = event.isChecked();
        if(checked){
            this.checkedMap.put((Integer) event.getTag(), true);
           /* LinkedHashMap<String, String> details = new LinkedHashMap<>(8);
            try{
                details.put("idEvent",
                        m_events.getJSONObject(event.getId()).getString("idEvent"));
                details.put("strEvent",
                        m_events.getJSONObject(event.getId()).getString("strEvent"));
                details.put("idLeague",
                        m_events.getJSONObject(event.getId()).getString("idLeague"));
                details.put("strLeague",
                        m_events.getJSONObject(event.getId()).getString("strLeague"));
                details.put("strHomeTeam",
                        m_events.getJSONObject(event.getId()).getString("strHomeTeam"));
                details.put("strAwayTeam",
                        m_events.getJSONObject(event.getId()).getString("strAwayTeam"));
                details.put("dateEvent",
                        m_events.getJSONObject(event.getId()).getString("dateEvent"));
                details.put("strTimeLocal",
                        m_events.getJSONObject(event.getId()).getString("strTimeLocal"));
                boolean added = m_gamesList.addGame(String.valueOf(event.getId()), details);
                if (!added){
                    System.out.println("didn't add games chosen");
                    //Toast.makeText(this, "added succesfuly"); //TODO: notification
                }
            } catch (Exception e){
                System.out.println(this.toString()+" line 111");
            }*/
            if(m_counter >= 2){
                event.setChecked(false);
                this.checkedMap.put((Integer) event.getTag(), false);
                toast = Toast.makeText(m_context, "Can't more than 10", Toast.LENGTH_SHORT);
            } else {
                m_counter++;
                toast = Toast.makeText(m_context, event.getText()+" selected", Toast.LENGTH_SHORT);
            }
        } else {
            this.checkedMap.put(event.getId(), false);
            m_counter--;
//            event.setChecked(false);
            toast = Toast.makeText(m_context, event.getText()+" removed", Toast.LENGTH_SHORT);
        }
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }
    public void showGamesChosen(View view){
        for (int j = 0; j<this.m_itemsArr.size()-1; j++){
            CheckBox game =(CheckBox) this.m_itemsArr.get(j);
            if(this.checkedMap.get(j)){
                int i = (Integer) game.getTag();
                LinkedHashMap<String, String> details = new LinkedHashMap<>(8);
                try{
                    details.put("idEvent",
                            m_events.getJSONObject(i).getString("idEvent"));
                    details.put("strEvent",
                            m_events.getJSONObject(i).getString("strEvent"));
                    details.put("idLeague",
                            m_events.getJSONObject(i).getString("idLeague"));
                    details.put("strLeague",
                            m_events.getJSONObject(i).getString("strLeague"));
                    details.put("strHomeTeam",
                            m_events.getJSONObject(i).getString("strHomeTeam"));
                    details.put("strAwayTeam",
                            m_events.getJSONObject(i).getString("strAwayTeam"));
                    details.put("dateEvent",
                            m_events.getJSONObject(i).getString("dateEvent"));
                    details.put("strTimeLocal",
                            m_events.getJSONObject(i).getString("strTimeLocal"));
                    boolean added = m_gamesListJSON.addGame(String.valueOf(i), details);
                    if (!added){
                        System.out.println("didn't add games chosen");
                        //Toast.makeText(this, "added succesfuly"); //TODO: notification
                    }
                } catch (Exception e){
                    System.out.println(this.toString()+" line 80");
                }
            }
        }
        this.m_gamesListJSON.uploadToDB();
        Intent intent = new Intent(m_context, GambleWin.class);
        m_context.startActivity(intent);
    }
}
