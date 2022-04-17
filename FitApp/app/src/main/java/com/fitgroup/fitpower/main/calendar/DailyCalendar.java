package com.fitgroup.fitpower.main.calendar;

import static com.fitgroup.fitpower.utils.StaticVar.selectedCalendarDate;
import static java.util.Calendar.*;

import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.NavActivity;
import com.fitgroup.fitpower.objects.DailyWorkout;
import com.fitgroup.fitpower.objects.Equipment;
import com.fitgroup.fitpower.objects.Muscle;
import com.fitgroup.fitpower.objects.MuscleGroup;
import com.fitgroup.fitpower.objects.Workout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DailyCalendar extends NavActivity {

    private ConstraintLayout addWorkoutLayout;

    private TextView dayLabel;

    private LinearLayout addWorkoutList;

    private final Map<Integer, DailyWorkout> dailyWorkouts = new HashMap<>();

    private final Map<Integer, Workout> allWorkouts = new HashMap<>();
    private final Map<Integer, Equipment> allEquipment = new HashMap<>();
    private final Map<Integer, Muscle> allMuscles = new HashMap<>();

    private int addWorkoutSid;
    private int addSets;
    private int addReps;

    private int addSuperSetWorkoutSid;
    private int addSuperSetSets;
    private int addSuperSetReps;

    private String superSetUUID;

    private Spinner eqSpin;
    private Spinner muscSpin;
    private Spinner extraSpin;

    public DailyCalendar() {
        super(R.id.dailycalendar_drawer, R.id.dailycalendar_nav,R.layout.activity_dailycalendar);
    }

    @Override
    protected void initiateFields() {
        addWorkoutLayout = findViewById(R.id.dcal_addwrk_cl);
        addWorkoutLayout.setVisibility(View.INVISIBLE);

        dayLabel = findViewById(R.id.dcal_date_lbl);
        addWorkoutList = findViewById(R.id.dcal_add_wrkout_list);

        eqSpin = findViewById(R.id.dcal_add_eq_spin);
        muscSpin = findViewById(R.id.dcal_add_musc_spin);
        extraSpin = findViewById(R.id.dcal_add_extra_spin);

        ImageButton nextDay = findViewById(R.id.cal_next_btn);
        nextDay.setOnClickListener(view -> nextDay());

        ImageButton prevDay = findViewById(R.id.cal_prev_btn);
        prevDay.setOnClickListener(view -> previousDay());

        setButtons();

        setForDay();
    }

    private void setButtons(){

        Button addWorkoutBtn = findViewById(R.id.dcal_addwrk_btn);
        Button openAddWorkoutBtn = findViewById(R.id.dcal_wrk_add);
        Button closeAddWorkoutBtn = findViewById(R.id.dcal_wrk_close);

        addWorkoutBtn.setOnClickListener(view -> openAddWorkout());
        closeAddWorkoutBtn.setOnClickListener(view -> closeAddWorkout());
        openAddWorkoutBtn.setOnClickListener(view -> addWorkoutToDay(addWorkoutSid,addSets,addReps));

    }

    @Override
    protected void responsePostHandler(JSONObject obj, int handler) {
        if(obj != null) {
            this.runOnUiThread(() -> {
                if (handler == 1) {

                } else if (handler == 2) {

                }else if (handler == 3) {
                    updateWorkouts(obj);
                }else if (handler == 4) {

                }

            });
        }
    }

    private void setForDay(){
        dayLabel.setText(dayMonthYearFromDateText());

        JSONObject json = getBasicSIDJson();

        try {
            json.put("TargetDate",dayMonthYearFromDateJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getObjectFromPost("https://fitrickapi.azurewebsites.net/calendar/routineworkouts",json,2);

    }

    private JSONObject AddWorkoutToDayJson(int workoutSID,int sets,int reps){
        JSONObject obj = getBasicSIDJson();
        try {
            obj.put("NewWorkout",workoutSID);
            obj.put("Sets",sets);
            obj.put("Reps",reps);
            obj.put("WorkoutDay",dayMonthYearFromDateJson());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    private String dayMonthYearFromDateJson(){
        return new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA).format(selectedCalendarDate.getTime());
    }

    private String dayMonthYearFromDateText(){
        return new SimpleDateFormat("MMMM dd yyyy", Locale.CANADA).format(selectedCalendarDate.getTime());
    }

    private void addWorkoutToDay(int workoutSID,int sets,int reps){

        getObjectFromPost("https://fitrickapi.azurewebsites.net/calendar/insert/workout",AddWorkoutToDayJson(workoutSID,sets,reps),1);
    }

    private void updateRoutineWorkouts(JSONObject obj){
        try {
            JSONArray array = obj.getJSONArray("");

            for(int i = 0; i < array.length(); i++){

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private CardView createWorkoutCard(String label, int workoutSID){

        CardView view = new CardView(this);

        LinearLayout mainLayout = getLinearLayout(this,LinearLayout.HORIZONTAL);

        ImageView mainLogo = getImageView(this,R.drawable.ic_home,90,90);
        mainLayout.addView(mainLogo);

        LinearLayout centerLayout = getLinearLayout(this,LinearLayout.VERTICAL);

        TextView mainLabel = getTextView(this,label,20,30,230,5,8,0,5, Gravity.CENTER);
        centerLayout.addView(mainLabel);

        LinearLayout hiddenLayout = getLinearLayout(this,LinearLayout.HORIZONTAL);

        LinearLayout setsLayout = getLinearLayout(this,LinearLayout.VERTICAL);
        LinearLayout repsLayout = getLinearLayout(this,LinearLayout.VERTICAL);
        LinearLayout weightLayout = getLinearLayout(this,LinearLayout.VERTICAL);
        LinearLayout weightLabelLayout = getLinearLayout(this,LinearLayout.VERTICAL);
        LinearLayout weightEditLayout = getLinearLayout(this,LinearLayout.HORIZONTAL);

        EditText setsIn = getEditText(this,"1",45,48,5,0,5,5,48, Gravity.CENTER);
        EditText repsIn = getEditText(this,"1",45,48,5,0,5,5,48, Gravity.CENTER);

        EditText weightIn = getEditText(this,"0",45,48,5,0,5,5,48, Gravity.CENTER);

        TextView xView = getTextView(this,"X",18,20,20,5,37,5,5, Gravity.CENTER);

        TextView xView2 = getTextView(this,"X",18,20,20,5,0,5,5, Gravity.CENTER);

        TextView setsLabel = getTextView(this,getResources().getString(R.string.dcal_label_sets),16,21,48,5,0,5,5, Gravity.CENTER);
        TextView repsLabel = getTextView(this,getResources().getString(R.string.dcal_label_reps),16,21,48,5,0,5,5, Gravity.CENTER);
        TextView weightLabel = getTextView(this,getResources().getString(R.string.dcal_label_weight),16,21,60,40,0,5,5, Gravity.CENTER);

        setsLayout.addView(setsLabel);
        setsLayout.addView(setsIn);
        repsLayout.addView(repsLabel);
        repsLayout.addView(repsIn);
        weightLabelLayout.addView(weightLabel);
        weightEditLayout.addView(xView2);
        weightEditLayout.addView(weightIn);

        weightLayout.addView(weightLabelLayout);
        weightLayout.addView(weightEditLayout);

        hiddenLayout.addView(setsLayout);
        hiddenLayout.addView(xView);
        hiddenLayout.addView(repsLayout);
        hiddenLayout.addView(weightLayout);

        setInvisible(hiddenLayout);

        centerLayout.addView(hiddenLayout);

        mainLayout.addView(centerLayout);
        view.addView(mainLayout);

        view.setOnClickListener(view1 -> {
            if(addWorkoutSid == 0){
                addWorkoutSid = workoutSID;
                addReps = 1;
                addSets = 1;
                setBackGroundColor(view,R.color.selected);
                setVisible(hiddenLayout);
            }else if(addWorkoutSid == workoutSID){
                addWorkoutSid = 0;
                setBackGroundColor(view,R.color.white);
                setInvisible(hiddenLayout);
            }else if(addSuperSetWorkoutSid == 0){
                addSuperSetWorkoutSid = workoutSID;
                addSuperSetReps = 1;
                addSuperSetSets = 2;
                setBackGroundColor(view,R.color.selected);
                setVisible(hiddenLayout);
            }else if(addSuperSetWorkoutSid == workoutSID){
                addSuperSetWorkoutSid = 0;
                setBackGroundColor(view,R.color.white);
                setInvisible(hiddenLayout);
            }
        });

        return view;
    }

    private void updateWorkouts(JSONObject obj){
        allWorkouts.clear();
        try {
            JSONArray array = obj.getJSONArray("Workouts");

            for(int i = 0; i < array.length(); i++){
                JSONObject arrayObj = array.getJSONObject(i);

                int workoutSID = arrayObj.getInt("WorkoutSID");
                String workoutLabel = arrayObj.getString("WorkoutLabel");
                String workoutDescription = arrayObj.getString("WorkoutDescription");

                Workout workout = new Workout(workoutSID,workoutLabel,workoutDescription);

                allWorkouts.put(workoutSID,workout);
                addWorkoutList.addView(createWorkoutCard(workoutLabel,workoutSID));
            }

            array = obj.getJSONArray("Equipment");

            List<String> spinList = new ArrayList<>();
            spinList.add("Equipment");

            for(int i = 0; i < array.length(); i++){
                JSONObject arrayObj = array.getJSONObject(i);

                int equipmentSID = arrayObj.getInt("EquipmentSID");
                String equipmentLabel = arrayObj.getString("EquipmentLabel");

                allEquipment.put(equipmentSID, new Equipment(equipmentSID,equipmentLabel,""));
                spinList.add(equipmentLabel);
            }

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinList);

            eqSpin.setAdapter(arrayAdapter);

            array = obj.getJSONArray("Muscles");

            spinList = new ArrayList<>();
            spinList.add("Muscles");

            for (int i = 0; array.length() > i; ++i) {
                JSONObject arrayObj = array.getJSONObject(i);

                int muscleSID = arrayObj.getInt("MuscleSID");
                String muscleLabel = arrayObj.getString("MuscleLabel");
                int muscleGroupSID0 = arrayObj.getInt("MuscleGroupSID0");
                String muscleGroup0 = arrayObj.getString("MuscleGroup0");
                int muscleGroupSID1 = arrayObj.getInt("MuscleGroupSID1");
                String muscleGroup1 = arrayObj.getString("MuscleGroup1");

                allMuscles.put(muscleSID, new Muscle(muscleSID,muscleLabel,"", new MuscleGroup(muscleGroupSID0,muscleGroup0),new MuscleGroup(muscleGroupSID1,muscleGroup1)));
                spinList.add(muscleLabel);
            }

            arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinList);

            muscSpin.setAdapter(arrayAdapter);

            array = obj.getJSONArray("WorkoutEquipment");
            for (int i = 0; array.length() > i; ++i) {
                JSONObject arrayObj = array.getJSONObject(i);

                int workoutSID = arrayObj.getInt("WorkoutSID");
                int equipmentSID = arrayObj.getInt("EquipmentSID");

                Workout workout = allWorkouts.get(workoutSID);
                if (workout != null) {
                    workout.addEquipment(equipmentSID);
                    allWorkouts.put(workoutSID,workout);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void nextDay(){
        if(addWorkoutLayout.getVisibility() == View.INVISIBLE) {
            selectedCalendarDate.add(DAY_OF_MONTH, 1);
            setForDay();
        }
    }

    private void previousDay(){
        if(addWorkoutLayout.getVisibility() == View.INVISIBLE) {
            selectedCalendarDate.add(DAY_OF_MONTH, -1);
            setForDay();
        }
    }

    private void openAddWorkout(){
        getObjectFromPost("https://fitrickapi.azurewebsites.net/select/workouts",getBasicSIDJson(),3);
        setVisible(addWorkoutLayout);
    }
    private void closeAddWorkout(){
        setInvisible(addWorkoutLayout);
    }



}
