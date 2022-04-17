package com.fitgroup.fitpower.main.routine;

import static com.fitgroup.fitpower.utils.StaticVar.activeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.NavActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoutineActivity extends NavActivity {

    private final Map<Integer, String> UserRoutines = new HashMap<>();
    private final Map<Integer, Integer> routinesGyms = new HashMap<>();
    private final Map<Integer, String> userGyms = new HashMap<>();
    private Spinner gymSpin;
    private EditText searchField;

    private LinearLayout rtnListLayout;


    public RoutineActivity() {
        super(R.id.routine_drawer, R.id.routine_nav,R.layout.activity_routine);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button = findViewById(R.id.rtn_new_w_btn);

        rtnListLayout = findViewById(R.id.rtn_list_lyout);

        searchField = findViewById(R.id.rtn_search_field);
        gymSpin = findViewById(R.id.rtn_gym_spin);

        button.setOnClickListener(view-> openNewRoutineActivity());

        getObjectFromPost("https://fitrickapi.azurewebsites.net/select/userroutines",getBasicSIDJson(),1);

    }

    @Override
    protected void responsePostHandler(JSONObject obj, int handler) {

        if (obj != null) {
            this.runOnUiThread(() -> {
                JSONArray array;
                try {
                    if (handler == 1) {
                        array = obj.getJSONArray("UserRoutines");

                        List<String> gymList = new ArrayList<>();

                        gymList.add(getString(R.string.rtn_bygym_label));

                        for(int i = 0; array.length() > i ; ++i){

                            JSONObject json = array.getJSONObject(i);

                            int routineSID = json.getInt("RoutineSID");
                            int gymSID = json.getInt("RoutineGymSID");
                            UserRoutines.put(routineSID,json.getString("RoutineLabel"));
                            routinesGyms.put(routineSID, gymSID);
                            if(!userGyms.containsKey(gymSID)) {
                                String gymName = json.getString("GymLabel");
                                userGyms.put(gymSID, gymName);
                                gymList.add(gymName);
                            }
                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, gymList);

                        gymSpin.setAdapter(arrayAdapter);

                        setRoutineList();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }


    }

    private void openNewRoutineActivity() {
        activeActivity = R.layout.activity_routine_new;
        startActivity(new Intent(this, NewRoutineActivity.class));
    }

    private void setRoutineList(){
        String searchText = searchField.getText().toString();
        String gymSelection = gymSpin.getSelectedItem().toString();

        for(Map.Entry<Integer,String> entry: UserRoutines.entrySet()){
            if(searchText.equals("") || entry.getValue().contains(searchText)){
                 if(gymSelection.equals(getString(R.string.rtn_bygym_label)) || gymSelection.equals(userGyms.get(routinesGyms.get(entry.getKey())))){
                     rtnListLayout.addView(setRoutineCard(entry.getValue(),entry.getKey()));
                 }
            }
        }
    }

    private CardView setRoutineCard(String label,int routineSID){

        CardView cv = new CardView(this);
        LinearLayout main_ll = getLinearLayout(this,LinearLayout.HORIZONTAL);
        LinearLayout center_ll = getLinearLayout(this,LinearLayout.VERTICAL);
        LinearLayout main_label_ll = getLinearLayout(this,LinearLayout.HORIZONTAL);
        LinearLayout gym_ll = getLinearLayout(this,LinearLayout.HORIZONTAL);
        LinearLayout wo_ll = getLinearLayout(this,LinearLayout.HORIZONTAL);
        LinearLayout musc_ll = getLinearLayout(this,LinearLayout.HORIZONTAL);
        LinearLayout btn_ll = getLinearLayout(this,LinearLayout.VERTICAL);

        ImageView logo = getImageView(this,R.drawable.ic_routine,DPtoPX(30),DPtoPX(30));
        main_ll.addView(logo);


        TextView textLabel = getTextView(this,label,24,30,160,10,10,0,0, Gravity.NO_GRAVITY);
        main_label_ll.addView(textLabel);

        ImageButton workoutListButton = new ImageButton(this);
        workoutListButton.setImageResource(R.drawable.ic_description);
        main_label_ll.addView(workoutListButton);

        TextView gymLabel = getTextView(this,getString(R.string.rtn_gym_label),16,24,80,10,5,0,0, Gravity.NO_GRAVITY);
        TextView gymName = getTextView(this,userGyms.get(routinesGyms.get(routineSID)),16,24,120,10,5,0,0, Gravity.NO_GRAVITY);
        gym_ll.addView(gymLabel);
        gym_ll.addView(gymName);
        int totalW = 0;

        TextView workoutsLabel = getTextView(this,getString(R.string.rtn_wo_label),16,30,80,10,5,0,0, Gravity.NO_GRAVITY);
        TextView totalWorkouts = getTextView(this,String.valueOf(totalW),16,30,120,10,5,0,0, Gravity.NO_GRAVITY);
        wo_ll.addView(workoutsLabel);
        wo_ll.addView(totalWorkouts);
        String muscles = "";

        TextView musclesLabel = getTextView(this,getString(R.string.rtn_tm_label),16,30,140,10,5,0,0, Gravity.NO_GRAVITY);
        TextView mainMuscles = getTextView(this,muscles,16,30,120,10,5,0,0, Gravity.NO_GRAVITY);

        musc_ll.addView(musclesLabel);
        musc_ll.addView(mainMuscles);

        ImageButton editButton = new ImageButton(this);
        editButton.setImageResource(R.drawable.ic_edit);
        ImageButton deleteButton = new ImageButton(this);
        deleteButton.setImageResource(R.drawable.ic_remove_circle);
        ImageButton favButton = new ImageButton(this);

        boolean isFav = false;
        if(isFav) {
            favButton.setImageResource(R.drawable.ic_star_full);
        }else{
            favButton.setImageResource(R.drawable.ic_star_empty);
        }
        btn_ll.addView(favButton);
        btn_ll.addView(editButton);
        btn_ll.addView(deleteButton);

        center_ll.addView(main_label_ll);
        center_ll.addView(gym_ll);
        center_ll.addView(wo_ll);
        center_ll.addView(musc_ll);

        main_ll.addView(center_ll);
        main_ll.addView(btn_ll);
        cv.addView(main_ll);
        return cv;
    }









}
