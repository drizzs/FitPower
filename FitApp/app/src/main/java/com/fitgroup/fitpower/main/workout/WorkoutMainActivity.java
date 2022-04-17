package com.fitgroup.fitpower.main.workout;

import static com.fitgroup.fitpower.utils.StaticVar.activeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class WorkoutMainActivity extends NavActivity {

    //WorkoutSID/WorkoutLabel/WorkoutDescription
    private Map<Integer, Pair<String,String>> workouts = new HashMap<>();
    private Map<Integer,String> equipment = new HashMap<>();
    private Map<Integer,String> muscleGroups = new HashMap<>();
    private Map<Integer,Pair<String,Pair<String,String>>> muscles = new HashMap<>();
    //WorkoutSID/MuscleSID
    private Map<Integer,Integer> workoutMuscles = new HashMap<>();
    //WorkoutSID/EquipmentSID
    private Map<Integer,Integer> workoutEquipment = new HashMap<>();

    private Spinner equipSpin;
    private Spinner muscleSpin;
    private Spinner typeSpin;

    private LinearLayout workoutList;

    public WorkoutMainActivity() {
        super(R.id.workout_drawer, R.id.workout_nav,R.layout.activity_workout);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        equipSpin = findViewById(R.id.workout_new_equip_spin);
        muscleSpin = findViewById(R.id.workout_new_muscle_spin);
        typeSpin = findViewById(R.id.workout_new_types_spin);
        workoutList = findViewById(R.id.wrk_main_svl);

        getObjectFromPost("https://fitrickapi.azurewebsites.net/select/workouts",getBasicSIDJson(),1);

        Button addWrkout = findViewById(R.id.wrk_new_add);

        addWrkout.setOnClickListener((view)->openWorkoutCreate());

    }

    protected void openWorkoutCreate(){
        activeActivity = R.layout.activity_workout_new;
        startActivity(new Intent(this, WorkoutNewActivity.class));
    }

    @Override
    protected void responsePostHandler(JSONObject obj, int handler) {
        if (obj != null) {
            this.runOnUiThread(() -> {
                JSONArray array;
                try {
                    if (handler == 1) {
                        array = obj.getJSONArray("Workouts");
                        for (int i = 0; array.length() > i; ++i) {
                            JSONObject arrayObj = array.getJSONObject(i);

                            int workoutSID = arrayObj.getInt("WorkoutSID");
                            String workoutLabel = arrayObj.getString("WorkoutLabel");
                            String workoutDesc = arrayObj.getString("WorkoutDescription");

                            workouts.put(workoutSID, new Pair<>(workoutLabel,workoutDesc));

                            workoutList.addView(createWorkoutCard(workoutLabel));
                        }
                        array = obj.getJSONArray("Equipment");

                        List<String> standList = new ArrayList<>();
                        standList.add("Equipment");
                        for (int i = 0; array.length() > i; ++i) {
                            JSONObject arrayObj = array.getJSONObject(i);

                            int EquipmentSID = arrayObj.getInt("EquipmentSID");
                            String EquipmentLabel = arrayObj.getString("EquipmentLabel");

                            equipment.put(EquipmentSID, EquipmentLabel);

                            standList.add(EquipmentLabel);

                        }

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, standList);

                        equipSpin.setAdapter(arrayAdapter);

                        standList = new ArrayList<>();

                        array = obj.getJSONArray("Muscles");
                        for (int i = 0; array.length() > i; ++i) {
                            JSONObject arrayObj = array.getJSONObject(i);

                            int MuscleSID = arrayObj.getInt("MuscleSID");
                            String MuscleLabel = arrayObj.getString("MuscleLabel");
                            String MuscleGroup0 = arrayObj.getString("MuscleGroup0");
                            String MuscleGroup1 = arrayObj.getString("MuscleGroup1");

                            muscles.put(MuscleSID, new Pair<>(MuscleLabel, new Pair<>(MuscleGroup0, MuscleGroup1)));
                        }

                        array = obj.getJSONArray("MuscleGroups");
                        standList.add("Muscle Groups");
                        for (int i = 0; array.length() > i; ++i) {
                            JSONObject arrayObj = array.getJSONObject(i);

                            int MuscleGroupSID = arrayObj.getInt("MuscleGroupSID");
                            String MuscleGroupLabel = arrayObj.getString("MuscleGroupLabel");

                            muscleGroups.put(MuscleGroupSID,MuscleGroupLabel);
                            standList.add(MuscleGroupLabel);
                        }

                        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, standList);

                        muscleSpin.setAdapter(arrayAdapter);

                        array = obj.getJSONArray("WorkoutMuscles");
                        for (int i = 0; array.length() > i; ++i) {
                            JSONObject arrayObj = array.getJSONObject(i);

                            int workoutSID = arrayObj.getInt("WorkoutSID");
                            int muscleSID = arrayObj.getInt("MuscleSID");

                            workoutMuscles.put(workoutSID, muscleSID);
                        }
                        array = obj.getJSONArray("WorkoutEquipment");
                        for (int i = 0; array.length() > i; ++i) {
                            JSONObject arrayObj = array.getJSONObject(i);

                            int workoutSID = arrayObj.getInt("WorkoutSID");
                            int equipmentSID = arrayObj.getInt("EquipmentSID");

                            workoutEquipment.put(workoutSID, equipmentSID);
                        }



                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private CardView createWorkoutCard(String Label){

        CardView view = new CardView(this);

        LinearLayout mainLayout = getLinearLayout(this,LinearLayout.HORIZONTAL);

        ImageView mainLogo = getImageView(this,R.drawable.ic_home,90,90);
        mainLayout.addView(mainLogo);

        LinearLayout centerLayout = getLinearLayout(this,LinearLayout.VERTICAL);

        TextView mainLabel = getTextView(this,Label,18,30,230,5,8,0,5, Gravity.CENTER);
        centerLayout.addView(mainLabel);

        mainLayout.addView(centerLayout);
        view.addView(mainLayout);
        return view;
    }



}
