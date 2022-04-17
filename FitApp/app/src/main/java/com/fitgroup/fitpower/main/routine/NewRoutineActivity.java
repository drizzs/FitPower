package com.fitgroup.fitpower.main.routine;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.base.NavActivity;
import com.fitgroup.fitpower.utils.StaticVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class NewRoutineActivity extends NavActivity {


    private final Map<Integer, String> workoutsList = new HashMap<>();
    private final Map<Integer, String> workoutDescription = new HashMap<>();

    private final List<Integer> workoutsAdded = new ArrayList<>();
    private final Map<Integer, Integer> addedWorkoutSets = new HashMap<>();
    private final Map<Integer, Integer> addedWorkoutReps = new HashMap<>();
    private final Map<Integer, Integer> addedWorkoutOrder = new HashMap<>();


    private final Map<Integer,String> equipment = new HashMap<>();
    private final Map<Integer,String> muscleGroups = new HashMap<>();

    private final Map<Integer,String> muscles = new HashMap<>();

    private final Map<Integer,List<Integer>> muscleMGroup = new HashMap<>();
    //WorkoutSID/MuscleSID
    private final Map<Integer,List<Integer>> workoutMuscles = new HashMap<>();
    //WorkoutSID/EquipmentSID
    private final Map<Integer,List<Integer>> workoutEquipment = new HashMap<>();

    private final Map<Integer,String> userGyms = new HashMap<>();
    private int gymsFav = 0;

    private Spinner equipSpin;
    private Spinner muscleSpin;
    private Spinner typeSpin;

    private final Map<Integer,List<Integer>> gymEquipment = new HashMap<>();

    private Spinner gymSpin;

    private int selectedGym;
    private int selectedEquipment;
    private int selectedMuscle;

    private LinearLayout workoutList;
    private LinearLayout workoutAdded;

    public NewRoutineActivity() {
        super(R.id.routine_drawer_new, R.id.routine_nav_new,R.layout.activity_routine_new);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        equipSpin = findViewById(R.id.rtn_new_eq);
        muscleSpin = findViewById(R.id.rtn_new_musc);
        typeSpin = findViewById(R.id.rtn_new_xt);

        gymSpin = findViewById(R.id.rtn_new_gym);

        workoutList = findViewById(R.id.rtn_new_lyt_list);
        workoutAdded = findViewById(R.id.rtn_new_lyt_select);

        Button createBtn = findViewById(R.id.rtn_new_create_btn);

        getObjectFromPost("https://fitrickapi.azurewebsites.net/select/usergyms",getBasicSIDJson(),1);

        equipSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String string = adapterView.getItemAtPosition(i).toString();
                selectedEquipment = getEquipmentSID(string);
                setWorkouts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        muscleSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String string = adapterView.getItemAtPosition(i).toString();
                selectedMuscle = getMuscleGroupSID(string);
                setWorkouts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        gymSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String string = adapterView.getItemAtPosition(i).toString();
                selectedGym = getGymSID(string);
                setWorkouts();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        createBtn.setOnClickListener(view -> createRoutine());

    }

    protected void createRoutine(){

        if(workoutsAdded.size() > 0) {

            JSONObject json = new JSONObject();

            EditText name = findViewById(R.id.routine_new_name_field);

            int gymSID = getGymSID(gymSpin.getSelectedItem().toString());

            JSONArray array = new JSONArray();
            try {

                for (int i = 0; workoutsAdded.size() > i ; ++i) {
                    JSONObject workoutObj = new JSONObject();

                    int SID = workoutsAdded.get(i);

                    workoutObj.put("WorkoutSID", SID);
                    workoutObj.put("WorkoutOrder","");
                    workoutObj.put("Sets", addedWorkoutSets.get(SID));
                    workoutObj.put("Reps", addedWorkoutReps.get(SID));
                    array.put(workoutObj);

                }

                json.put("AccountInfoSID", StaticVar.accountInfoSID);
                json.put("RoutineName", name.getText().toString());
                json.put("AssociatedGym",gymSID);
                json.put("Workouts",array);
            }catch (Exception e){
                e.printStackTrace();
            }
            getObjectFromPost("https://fitrickapi.azurewebsites.net/routine/newroutine",json,3);

        }

    }



    @Override
    protected void responsePostHandler(JSONObject obj, int handler) {
        if (obj != null) {
            this.runOnUiThread(() -> {
                try {
                    if (handler == 1) {
                        GymHandler(obj);
                    }else if (handler == 2) {
                        WorkoutHandler(obj);
                    }
                }catch( Exception e){
                    e.printStackTrace();
                }
            });
        }
    }

    private void GymHandler(JSONObject obj) throws JSONException {
        JSONArray array = obj.getJSONArray("UserGyms");

        List<String> standList = new ArrayList<>();

        standList.add("Your Gyms");

        for (int i = 0; array.length() > i; ++i) {
            JSONObject arrayObj = array.getJSONObject(i);

            if(arrayObj.getBoolean("IsFavourite")){
                selectedGym = arrayObj.getInt("GymSID");
                gymsFav = selectedGym;
            }

            userGyms.put(arrayObj.getInt("GymSID"),arrayObj.getString("GymLabel"));
            standList.add(arrayObj.getString("GymLabel"));
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, standList);

        gymSpin.setAdapter(arrayAdapter);

        if(userGyms.size() > 0){
            int pos = arrayAdapter.getPosition(Objects.requireNonNull(userGyms.get(selectedGym)));

            gymSpin.setSelection(pos);
        }

        array = obj.getJSONArray("GymEquipment");

        for (int i = 0; array.length() > i; ++i) {
            JSONObject arrayObj = array.getJSONObject(i);

            int gymSID = arrayObj.getInt("GymSID");
            int eqSID = arrayObj.getInt("EquipmentSID");

            if(!gymEquipment.containsKey(gymSID)){
                gymEquipment.put(gymSID,new ArrayList<>());
            }

            List<Integer> ge = gymEquipment.get(gymSID);

            Objects.requireNonNull(ge).add(eqSID);

            gymEquipment.put(gymSID,ge);
        }

        getObjectFromPost("https://fitrickapi.azurewebsites.net/select/workouts",getBasicSIDJson(),2);

    }

    private void WorkoutHandler(JSONObject obj) throws JSONException {
        JSONArray array = obj.getJSONArray("WorkoutMuscles");
        for (int i = 0; array.length() > i; ++i) {
            JSONObject arrayObj = array.getJSONObject(i);

            int workoutSID = arrayObj.getInt("WorkoutSID");
            int muscleSID = arrayObj.getInt("MuscleSID");

            if(!workoutMuscles.containsKey(workoutSID)){
                workoutMuscles.put(workoutSID,new ArrayList<>());
            }

            List<Integer> wm = workoutMuscles.get(workoutSID);

            Objects.requireNonNull(wm).add(muscleSID);

            workoutMuscles.put(workoutSID, wm);
        }

        array = obj.getJSONArray("WorkoutEquipment");
        for (int i = 0; array.length() > i; ++i) {
            JSONObject arrayObj = array.getJSONObject(i);

            int workoutSID = arrayObj.getInt("WorkoutSID");
            int equipmentSID = arrayObj.getInt("EquipmentSID");

            if(!workoutEquipment.containsKey(workoutSID)){
                workoutEquipment.put(workoutSID,new ArrayList<>());
            }

            List<Integer> we = workoutEquipment.get(workoutSID);

            Objects.requireNonNull(we).add(equipmentSID);

            workoutEquipment.put(workoutSID, we);
        }

        array = obj.getJSONArray("Equipment");

        List<String> standList = new ArrayList<>();

        standList.add("Equipment");

        selectedEquipment = 0;

        for (int i = 0; array.length() > i; ++i) {
            JSONObject arrayObj = array.getJSONObject(i);

            int EquipmentSID = arrayObj.getInt("EquipmentSID");
            String EquipmentLabel = arrayObj.getString("EquipmentLabel");

            equipment.put(EquipmentSID, EquipmentLabel);
            if(Objects.requireNonNull(gymEquipment.get(selectedGym)).contains(EquipmentSID)) {
                standList.add(EquipmentLabel);
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, standList);

        equipSpin.setAdapter(arrayAdapter);

        standList = new ArrayList<>();

        array = obj.getJSONArray("Muscles");
        for (int i = 0; array.length() > i; ++i) {
            JSONObject arrayObj = array.getJSONObject(i);

            int MuscleSID = arrayObj.getInt("MuscleSID");
            String MuscleLabel = arrayObj.getString("MuscleLabel");
            int MuscleGroupSID0 = arrayObj.getInt("MuscleGroupSID0");
            int MuscleGroupSID1 = arrayObj.getInt("MuscleGroupSID1");

            muscles.put(MuscleSID, MuscleLabel);

            List<Integer> mg = new ArrayList<>();
            if(MuscleGroupSID0 > 0) {
                mg.add(MuscleGroupSID0);
            }
            if (MuscleGroupSID1 > 0) {
                mg.add(MuscleGroupSID1);
            }

            muscleMGroup.put(MuscleSID,mg);
        }
        standList.add("Muscle Groups");
        array = obj.getJSONArray("MuscleGroups");

        selectedMuscle = 0;
        for (int i = 0; array.length() > i; ++i) {
            JSONObject arrayObj = array.getJSONObject(i);

            int MuscleGroupSID = arrayObj.getInt("MuscleGroupSID");
            String MuscleGroupLabel = arrayObj.getString("MuscleGroupLabel");

            muscleGroups.put(MuscleGroupSID,MuscleGroupLabel);

            standList.add(MuscleGroupLabel);
        }

        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, standList);

        muscleSpin.setAdapter(arrayAdapter);

        array = obj.getJSONArray("Workouts");
        for (int i = 0; array.length() > i; ++i) {
            JSONObject arrayObj = array.getJSONObject(i);

            int workoutSID = arrayObj.getInt("WorkoutSID");
            String workoutLabel = arrayObj.getString("WorkoutLabel");
            String workoutDesc = arrayObj.getString("WorkoutDescription");

            workoutsList.put(workoutSID, workoutLabel);
            workoutDescription.put(workoutSID, workoutDesc);
        }

        setWorkouts();
    }

    private CardView createWorkoutCard(String label,int workoutSID){

        CardView view = new CardView(this);

        LinearLayout mainLayout = getLinearLayout(this,LinearLayout.HORIZONTAL);

        ImageView mainLogo = getImageView(this,R.drawable.ic_home,90,90);
        mainLayout.addView(mainLogo);

        LinearLayout centerLayout = getLinearLayout(this,LinearLayout.VERTICAL);

        TextView mainLabel = getTextView(this,label,18,30,230,5,8,0,5, Gravity.CENTER);
        centerLayout.addView(mainLabel);

        mainLayout.addView(centerLayout);
        view.addView(mainLayout);

        view.setOnClickListener(view1 -> {
                workoutList.removeView(view1);

                workoutsAdded.add(workoutSID);
                addedWorkoutOrder.put(workoutSID,getMaxOrder() + 1);
                addedWorkoutSets.put(workoutSID,1);
                addedWorkoutReps.put(workoutSID,1);
                workoutAdded.addView(createAddedWorkoutCard(label,workoutSID));

        });

        return view;
    }

    private CardView createAddedWorkoutCard(String label,int workoutSID){
        CardView view = new CardView(this);

        LinearLayout mainLayout = getLinearLayout(this,LinearLayout.HORIZONTAL);
        LinearLayout firstLayout = getLinearLayout(this,LinearLayout.VERTICAL);
        LinearLayout iconLayout = getLinearLayout(this,LinearLayout.HORIZONTAL);
        LinearLayout buttonLayout = getLinearLayout(this,LinearLayout.VERTICAL);

        LinearLayout repsLayout = getLinearLayout(this,LinearLayout.HORIZONTAL);
        LinearLayout setLayout = getLinearLayout(this,LinearLayout.HORIZONTAL);

        LinearLayout centerLayout = getLinearLayout(this,LinearLayout.VERTICAL);
        LinearLayout textLayout = getLinearLayout(this,LinearLayout.VERTICAL);


        ImageView mainLogo = getImageView(this,R.drawable.ic_home,90,90);

        TextView mainLabel = getTextView(this,label,18,30,200,5,8,0,5, Gravity.CENTER);


        TextView setsLabel = getTextView(this,"Sets:",16,20,60,35,5,0,5,Gravity.NO_GRAVITY);
        TextView setsTotal = getTextView(this, Objects.requireNonNull(addedWorkoutSets.get(workoutSID)).toString(),16,20,30,5,5,0,5,Gravity.NO_GRAVITY);

        setLayout.addView(setsLabel);
        setLayout.addView(setsTotal);

        textLayout.addView(mainLabel);
        textLayout.addView(setLayout);

        ImageButton removeButton = new ImageButton(this);

        removeButton.setImageResource(R.drawable.ic_remove_circle);

        removeButton.setOnClickListener(view1 -> {
                workoutAdded.removeView(view);

                workoutsAdded.remove((Integer) workoutSID);
                addedWorkoutReps.remove(workoutSID);
                addedWorkoutSets.remove(workoutSID);
                addedWorkoutOrder.remove(workoutSID);
                workoutList.addView(createWorkoutCard(label,workoutSID));
        });

        int reps = Objects.requireNonNull(addedWorkoutReps.get(workoutSID));

        String repsText = "";

        if(reps == 26){
            repsText = "AMRAP";
        }else{
            repsText = String.valueOf(reps);
        }

        TextView repsLabel = getTextView(this,"Reps:",16,20,60,35,5,0,5,Gravity.NO_GRAVITY);
        TextView repsTotal = getTextView(this, repsText,16,20,60,5,5,0,5,Gravity.NO_GRAVITY);
        repsLayout.addView(repsLabel);
        repsLayout.addView(repsTotal);

        ImageButton upButton = new ImageButton(this);
        ImageButton downButton = new ImageButton(this);

        int upID = 0;
        int downID = 0;

        if(canGoUp(workoutSID)) {
            upID = R.drawable.ic_arrow_up_available;
        }else{
            upID = R.drawable.ic_arrow_up_unavailable;
        }
        if(canGoDown(workoutSID)) {
            downID = R.drawable.ic_arrow_down_available;
        }else{
            downID = R.drawable.ic_arrow_down_unavailable;
        }

        upButton.setImageResource(upID);
        downButton.setImageResource(downID);

        upButton.setOnClickListener(upView -> {
            if(canGoUp(workoutSID)){
                int order = addedWorkoutOrder.get(workoutSID);
                addedWorkoutOrder.put(workoutSID,order - 1);
                addedWorkoutOrder.put(getSIDFromOrder(order - 1),order);

                setAddedWorkouts();
            }
        });

        downButton.setOnClickListener(downView -> {
            if(canGoDown(workoutSID)){
                int order = addedWorkoutOrder.get(workoutSID);
                addedWorkoutOrder.put(workoutSID,order + 1);
                addedWorkoutOrder.put(getSIDFromOrder(order + 1),order);

                setAddedWorkouts();
            }
        });

        SeekBar setsBar = new SeekBar(this);
        SeekBar repsBar = new SeekBar(this);

        int progressReps = (int)Math.round(Math.min(3.8*addedWorkoutReps.get(workoutSID),setsBar.getMax() - 1));
        int progressSets = (int)Math.round(Math.min(10*addedWorkoutSets.get(workoutSID),setsBar.getMax()));

        setsBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                addedWorkoutSets.put(workoutSID,Math.min(Math.max(seekBar.getProgress()/10,1),10));
                String string = addedWorkoutSets.get(workoutSID) + "";
                setsTotal.setText(string);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        repsBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(seekBar.getProgress() == 100) {
                    repsTotal.setText("AMRAP");
                }else{
                    addedWorkoutReps.put(workoutSID,(int)Math.min(Math.max(Math.round(seekBar.getProgress()/3.8),1),25));
                    String string = addedWorkoutReps.get(workoutSID) + "";
                    repsTotal.setText(string);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        repsBar.setProgress(progressReps);
        setsBar.setProgress(progressSets);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DPtoPX(320),DPtoPX(30));

        params.setMargins(DPtoPX(0),DPtoPX(0),DPtoPX(0),DPtoPX(5));

        repsBar.setLayoutParams(params);

        params = new LinearLayout.LayoutParams(DPtoPX(230),DPtoPX(30));

        params.setMargins(DPtoPX(0),DPtoPX(0),DPtoPX(0),DPtoPX(0));

        setsBar.setLayoutParams(params);


        centerLayout.addView(textLayout);
        centerLayout.addView(setsBar);
        iconLayout.addView(mainLogo);
        iconLayout.addView(centerLayout);

        buttonLayout.addView(removeButton);
        buttonLayout.addView(upButton);
        buttonLayout.addView(downButton);

        firstLayout.addView(iconLayout);
        firstLayout.addView(repsLayout);
        firstLayout.addView(repsBar);

        mainLayout.addView(firstLayout);
        mainLayout.addView(buttonLayout);

        view.addView(mainLayout);

        return view;
    }

    private void setAddedWorkouts(){
        workoutAdded.removeAllViews();
        if(!workoutsAdded.isEmpty()){
            int[] workoutArray = new int[workoutsAdded.size()];

            for(int i = 0; i < workoutsAdded.size();++i){

                int SID = workoutsAdded.get(i);

                workoutArray[addedWorkoutOrder.get(SID)] = SID;
            }

            for (int SID : workoutArray) {
                createAddedWorkoutCard(workoutsList.get(SID), SID);
            }
        }
    }

   private void setWorkouts(){
        workoutList.removeAllViews();
        if(selectedGym > 0) {

            List<Integer> ge = gymEquipment.get(selectedGym);

            if (ge != null) {
                for(Map.Entry<Integer, String> entry: workoutsList.entrySet()){

                    if(!workoutsAdded.contains(entry.getKey())) {
                        int workoutSID = entry.getKey();

                        List<Integer> workoutEquip = workoutEquipment.get(workoutSID);

                        boolean hasEquipment = true;

                        for (int i = 0; Objects.requireNonNull(workoutEquip).size() > i; ++i) {

                            int equipSID = workoutEquip.get(i);

                            if (!ge.contains(equipSID)) {
                                hasEquipment = false;
                                break;
                            }
                        }
                        if (hasEquipment) {

                            if (workoutEquip.contains(selectedEquipment) || selectedEquipment == 0) {

                                List<Integer> muscleList = workoutMuscles.get(workoutSID);

                                boolean containsMuscle = false;

                                for (int i = 0; i < Objects.requireNonNull(muscleList).size(); i++) {
                                    int MuscleSID = muscleList.get(i);
                                    if (Objects.requireNonNull(muscleMGroup.get(MuscleSID)).contains(selectedMuscle)) {
                                        containsMuscle = true;
                                        break;
                                    }
                                }

                                if (selectedMuscle == 0 || containsMuscle) {

                                    workoutList.addView(createWorkoutCard(entry.getValue(),entry.getKey()));

                                }
                            }
                        }
                    }
                }
            }
        }else{
            for(Map.Entry<Integer, String> entry: workoutsList.entrySet()) {
                int workoutSID = entry.getKey();
                List<Integer> muscleList = workoutMuscles.get(workoutSID);
                List<Integer> workoutEquip = workoutEquipment.get(workoutSID);

                if(Objects.requireNonNull(workoutEquip).contains(selectedEquipment) || selectedEquipment == 0) {
                    if (selectedMuscle == 0 || Objects.requireNonNull(muscleList).contains(selectedMuscle)) {
                        workoutList.addView(createWorkoutCard(entry.getValue(),entry.getKey()));
                    }
                }

            }
        }

    }


    private int getGymSID(String Label){
        int value = 0;

        for (Map.Entry<Integer, String> entry: userGyms.entrySet()){
            if(entry.getValue().equals(Label)){
                value = entry.getKey();
                break;
            }
        }
        return value;
    }

    private int getEquipmentSID(String Label){
        int value = 0;

        for (Map.Entry<Integer, String> entry: equipment.entrySet()){
            if(entry.getValue().equals(Label)){
                value = entry.getKey();
                break;
            }
        }
        return value;
    }

    private int getMuscleGroupSID(String Label){
        int value = 0;

        for (Map.Entry<Integer, String> entry: muscleGroups.entrySet()){
            if(entry.getValue().equals(Label)){
                value = entry.getKey();
                break;
            }
        }
        return value;
    }

    private boolean canGoUp(int workoutSID){
        boolean bool = false;

        if(addedWorkoutOrder != null && addedWorkoutOrder.get(workoutSID) != null) {
            if (addedWorkoutOrder.get(workoutSID) > 1) {
                bool = true;
            }
        }
        return bool;
    }

    private int getMaxOrder(){
        int answer = 0;
        for(Map.Entry<Integer,Integer> entry : addedWorkoutOrder.entrySet()){
            if(entry.getValue() > answer){
                answer = entry.getValue();
            }
        }
        return answer;
    }

    private int getSIDFromOrder(int order){
        int workoutSID = 0;

        for(Map.Entry<Integer,Integer> entry : addedWorkoutOrder.entrySet()) {
            if(entry.getValue() == order){
                workoutSID = entry.getKey();
            }
        }
            return workoutSID;
    }

    private boolean canGoDown(int workoutSID){
        boolean bool = false;

        if(addedWorkoutOrder != null && addedWorkoutOrder.get(workoutSID) != null) {
            if (addedWorkoutOrder.get(workoutSID) < getMaxOrder()) {
                bool = true;
            }
        }
        return bool;
    }



}
