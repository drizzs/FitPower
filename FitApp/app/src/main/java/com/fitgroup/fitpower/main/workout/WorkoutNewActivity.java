package com.fitgroup.fitpower.main.workout;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.NavActivity;
import com.fitgroup.fitpower.utils.StaticVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class WorkoutNewActivity extends NavActivity {

    private Boolean isWeighted = false;
    private Boolean isTimed = false;

    private LinearLayout muscleSVList;
    private LinearLayout muscleSVAdd;

    private LinearLayout equipmentSVList;
    private LinearLayout equipmentSVAdd;

    private final Map<Integer, Pair<String,Pair<String,String>>> musclesListed = new HashMap<>();
    private final Map<Integer, Pair<String,Pair<String,String>>> musclesAdded = new HashMap<>();

    private final Map<Integer, String> equipmentList = new HashMap<>();
    private final Map<Integer, String> equipmentAdded = new HashMap<>();

    public WorkoutNewActivity() {
        super(R.id.workout_new_drawer, R.id.workout_new_nav,R.layout.activity_workout_new);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        muscleSVList = findViewById(R.id.wrk_new_sv_musc_list);
        muscleSVAdd = findViewById(R.id.wrk_new_sv_musc_act);
        equipmentSVList = findViewById(R.id.wrk_new_sv_eq_list);
        equipmentSVAdd = findViewById(R.id.wrk_new_sv_eq_act);


        getObjectFromPost("https://fitrickapi.azurewebsites.net/select/muscles",getBasicSIDJson(),1);
        getObjectFromPost("https://fitrickapi.azurewebsites.net/select/equipment",getBasicSIDJson(),2);

        Button isWeightedBtn = findViewById(R.id.wrk_new_weight_btn);
        Button isTimedBtn = findViewById(R.id.wrk_new_timed_btn);

        isWeightedBtn.setBackgroundColor(getResources().getColor(R.color.teal_200));
        isTimedBtn.setBackgroundColor(getResources().getColor(R.color.teal_200));

        isWeightedBtn.setOnClickListener(view -> {
            if(isWeighted){
                isWeighted = false;
                view.setBackgroundColor(getResources().getColor(R.color.teal_200));
            }else {
                isWeighted = true;
                view.setBackgroundColor(getResources().getColor(R.color.purple_700));
            }
        });
        isTimedBtn.setOnClickListener(view -> {
            if(isTimed){
                isTimed = false;
                view.setBackgroundColor(getResources().getColor(R.color.teal_200));
            }else {
                isTimed = true;
                view.setBackgroundColor(getResources().getColor(R.color.purple_700));
            }
        });

        EditText equipSearch = findViewById(R.id.wrk_new_equip_srch);

        equipSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                equipmentSVList.removeAllViews();

                for(Map.Entry<Integer,String> entry: equipmentList.entrySet()){

                    String toCheck = charSequence.toString().toLowerCase(Locale.ROOT);

                    if(toCheck.equals("") || entry.getValue().toLowerCase(Locale.ROOT).contains(toCheck)) {
                        equipmentSVList.addView(createEquipmentCard(entry.getValue()));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        EditText muscleSearch = findViewById(R.id.wrk_new_srch);

        muscleSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                muscleSVList.removeAllViews();
                for(Map.Entry<Integer,Pair<String,Pair<String,String>>> entry: musclesListed.entrySet()){
                    Pair<String,Pair<String,String>> value = entry.getValue();
                    Pair<String,String> groups = value.second;

                    String toCheck = charSequence.toString().toLowerCase(Locale.ROOT);

                    if(charSequence.toString().equals("") || value.first.toLowerCase(Locale.ROOT).contains(toCheck) || groups.first.toLowerCase(Locale.ROOT).contains(toCheck) || groups.second.toLowerCase(Locale.ROOT).contains(toCheck)){
                        muscleSVList.addView(createMuscleCard(value.first, groups.first, groups.second));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        Button createBtn = findViewById(R.id.wrk_new_create_btn);

        createBtn.setOnClickListener((view) -> getObjectFromPost("https://fitrickapi.azurewebsites.net/workout/newworkout",createPostWorkoutJson(),3));


    }

    @Override
    protected void responsePostHandler(JSONObject obj, int handler) {
        if (obj != null) {
            this.runOnUiThread(() -> {
                JSONArray array;
                try {
                    if (handler == 1) {
                        array = obj.getJSONArray("Muscles");
                        for (int i = 0; array.length() > i; ++i) {
                            JSONObject arrayObj = array.getJSONObject(i);

                            int muscleSID = arrayObj.getInt("MuscleSID");
                            String muscleLabel = arrayObj.getString("MuscleLabel");
                            String muscleGroup0 = arrayObj.getString("MuscleGroup0");
                            String muscleGroup1 = arrayObj.getString("MuscleGroup1");

                            addToMuscleList(muscleSID,muscleLabel,muscleGroup0,muscleGroup1);

                            muscleSVList.addView(createMuscleCard(muscleLabel,muscleGroup0,muscleGroup1));
                        }
                    }else if(handler == 2){
                        array = obj.getJSONArray("Equipment");
                        for (int i = 0; array.length() > i; ++i) {
                            JSONObject arrayObj = array.getJSONObject(i);

                            int equipmentSID = arrayObj.getInt("EquipmentSID");
                            String equipmentLabel = arrayObj.getString("EquipmentLabel");

                            equipmentList.put(equipmentSID,equipmentLabel);

                            equipmentSVList.addView(createEquipmentCard(equipmentLabel));
                        }
                    }else if(handler == 3){

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private CardView createEquipmentCard(String label){

        CardView cv = new CardView(this);
        LinearLayout layoutMain = getLinearLayout(this,LinearLayout.HORIZONTAL);

        ImageView equipmentIcon = getImageView(this,R.drawable.ic_settings,85,85);

        LinearLayout layoutCenter = getLinearLayout(this,LinearLayout.VERTICAL);
        TextView equipmentName = getTextView(this,label,20,30,230,5,15,5,5, Gravity.CENTER);
        layoutCenter.addView(equipmentName);

        layoutMain.addView(equipmentIcon);
        layoutMain.addView(layoutCenter);
        cv.addView(layoutMain);

        cv.setOnClickListener((view) -> {
            if (view.getParent().equals(equipmentSVList)) {
                equipmentSVList.removeView(view);
                equipmentSVAdd.addView(view);
                Map.Entry<Integer,String> entry = getFromEquipList(label);
                equipmentAdded.put(Objects.requireNonNull(entry).getKey(),entry.getValue());
                equipmentList.remove(entry.getKey());
            }else if (view.getParent().equals(equipmentSVAdd)) {
                equipmentSVAdd.removeView(view);
                equipmentSVList.addView(view);
                Map.Entry<Integer,String> entry = getFromEquipAdd(label);
                equipmentList.put(Objects.requireNonNull(entry).getKey(),entry.getValue());
                equipmentAdded.remove(entry.getKey());
            }
        });
        return cv;
    }


    private CardView createMuscleCard(String label,String group0,String group1){
        CardView cv = new CardView(this);
        LinearLayout layoutMain = getLinearLayout(this,LinearLayout.HORIZONTAL);

        ImageView muscleIcon = getImageView(this,R.drawable.ic_settings,85,85);

        LinearLayout layoutCenter = getLinearLayout(this,LinearLayout.VERTICAL);
        TextView muscleName = getTextView(this,label,20,30,230,5,15,5,5, Gravity.CENTER);

        LinearLayout layoutCBottom = getLinearLayout(this,LinearLayout.HORIZONTAL);

        TextView muscleGroup0 = getTextView(this,group0,15,30,115,5,10,5,5, Gravity.CENTER);
        TextView muscleGroup1 = getTextView(this,group1,15,30,115,5,10,5,5, Gravity.CENTER);
        layoutCBottom.addView(muscleGroup0);
        layoutCBottom.addView(muscleGroup1);

        layoutCenter.addView(muscleName);
        layoutCenter.addView(layoutCBottom);

        layoutMain.addView(muscleIcon);
        layoutMain.addView(layoutCenter);

        cv.addView(layoutMain);

        cv.setOnClickListener((view -> {
            if (view.getParent().equals(muscleSVList)) {
                muscleSVList.removeView(view);
                muscleSVAdd.addView(view);

                Map.Entry<Integer,Pair<String,Pair<String,String>>> entry = getFromMuscleList(label);

                addToMuscleAdd(Objects.requireNonNull(entry).getKey(),entry.getValue().first,entry.getValue().second.first,entry.getValue().second.second);
                musclesListed.remove(entry.getKey());
            } else if (view.getParent().equals(muscleSVAdd)) {
                muscleSVAdd.removeView(view);
                muscleSVList.addView(view);
                Map.Entry<Integer,Pair<String,Pair<String,String>>> entry = getFromMuscleAdd(label);

                addToMuscleList(Objects.requireNonNull(entry).getKey(),entry.getValue().first,entry.getValue().second.first,entry.getValue().second.second);
                musclesAdded.remove(entry.getKey());
            }
        }));
        return cv;
    }

    private void addToMuscleList(int sid,String label,String group0,String group1){
        musclesListed.put(sid,new Pair<>(label,new Pair<>(group0,group1)));
    }

    private void addToMuscleAdd(int sid,String label,String group0,String group1){
        musclesAdded.put(sid,new Pair<>(label,new Pair<>(group0,group1)));
    }

    private Map.Entry<Integer,Pair<String,Pair<String,String>>> getFromMuscleList(String label){
        for(Map.Entry<Integer,Pair<String,Pair<String,String>>> entry: musclesListed.entrySet()) {
            if(entry.getValue().first.equals(label)) {
                return entry;
            }
        }
        return null;
    }

    private Map.Entry<Integer,Pair<String,Pair<String,String>>> getFromMuscleAdd(String label){
        for(Map.Entry<Integer,Pair<String,Pair<String,String>>> entry: musclesAdded.entrySet()) {
            if(entry.getValue().first.equals(label)) {
                return entry;
            }
        }
        return null;
    }

    private Map.Entry<Integer,String> getFromEquipList(String label){
        for(Map.Entry<Integer,String> entry: equipmentList.entrySet()) {
            if(entry.getValue().equals(label)) {
                return entry;
            }
        }
        return null;
    }

    private Map.Entry<Integer,String> getFromEquipAdd(String label){
        for(Map.Entry<Integer,String> entry: equipmentAdded.entrySet()) {
            if(entry.getValue().equals(label)) {
                return entry;
            }
        }
        return null;
    }

    private JSONObject createPostWorkoutJson(){
        JSONObject json = new JSONObject();

        JSONArray muscleArray = new JSONArray();
        for(Map.Entry<Integer,Pair<String,Pair<String,String>>> entry: musclesAdded.entrySet()) {
            muscleArray.put(entry.getKey());
        }
        JSONArray equipmentArray = new JSONArray();
        for(Map.Entry<Integer,String> entry: equipmentAdded.entrySet()) {
            equipmentArray.put(entry.getKey());
        }

        EditText workoutName = findViewById(R.id.wrk_new_name_field);
        EditText workoutDesc = findViewById(R.id.wrk_new_desc_field);

        try {
            json.put("AccountInfoSID", StaticVar.accountInfoSID);
            json.put("WorkoutLabel",workoutName.getText().toString());
            json.put("WorkoutDescription",workoutDesc.getText().toString());
            json.put("IsWeighted",isWeighted);
            json.put("IsTimed",isTimed);
            json.put("ActivatedMuscles",muscleArray);
            json.put("AddedEquipment",equipmentArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }


}
