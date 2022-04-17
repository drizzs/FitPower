package com.fitgroup.fitpower.main.gym;

import static com.fitgroup.fitpower.utils.StaticVar.activeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
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
import com.fitgroup.fitpower.utils.StaticVar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;


public class GymMainActivity extends NavActivity {

    private final Map<Integer, String> myGymNames = new HashMap<>();
    private final Map<Integer, List<Integer>> gymEquipment = new HashMap<>();
    private final Map<Integer, String> equipmentNames = new HashMap<>();

    private final Map<Integer, String> allPublicGyms = new HashMap<>();

    private final Map<Integer, Integer> gymCities = new HashMap<>();

    private final Map<Integer, String> cityNames = new HashMap<>();
    private final Map<Integer, String> provincesNames = new HashMap<>();
    private final Map<Integer, String> countryNames = new HashMap<>();

    private int favGymID;

    private LinearLayout sv_layout;

    private LinearLayout mainLayout;
    private LinearLayout addLayout;

    private Spinner provSpin;
    private Spinner countrySpin;
    private Spinner citySpin;

    private int selectedCountrySID;
    private int selectedProvinceSID;
    private int selectedCitySID;

    public GymMainActivity() {
        super(R.id.gym_drawer, R.id.gym_nav,R.layout.activity_gym);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sv_layout = findViewById(R.id.gym_main_sv);
        mainLayout = findViewById(R.id.my_gym_layout);
        mainLayout.setVisibility(View.VISIBLE);
        addLayout = findViewById(R.id.add_gym_layout);
        setInvisible(addLayout);

        Button addGym = findViewById(R.id.add_gym_btn);
        Button newGym = findViewById(R.id.new_gym_btn);
        Button closeAdd = findViewById(R.id.close_add_gym_btn);

        addGym.setOnClickListener(view -> openGymSearch());
        newGym.setOnClickListener(view -> openGymCreate());
        closeAdd.setOnClickListener(view -> closeGymSearch());

        countrySpin = findViewById(R.id.add_gym_cntry_spin);
        provSpin = findViewById(R.id.add_gym_prov_spin);
        citySpin = findViewById(R.id.add_gym_city_spin);

        EditText searchText = findViewById(R.id.gym_search_field);

        JSONObject json = new JSONObject();

        try {
            json.put("AccountInfoSID", StaticVar.accountInfoSID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        getObjectFromPost("https://fitrickapi.azurewebsites.net/select/usergyms",json,1);

        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                sv_layout.removeAllViews();

                for(Map.Entry<Integer,String> entry: myGymNames.entrySet()) {

                    String toCheck = charSequence.toString().toLowerCase(Locale.ROOT);

                    if (charSequence.toString().equals("") || entry.getValue().toLowerCase(Locale.ROOT).contains(toCheck)) {
                        sv_layout.addView(createGymCard(entry.getValue(),entry.getKey()));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });


    }

    protected void openGymSearch(){
        getObjectFromPost("https://fitrickapi.azurewebsites.net/location/countries",getBasicSIDJson(),4);
        getObjectFromPost("https://fitrickapi.azurewebsites.net/select/publicgyms",getBasicSIDJson(),5);
        provSpin.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,new ArrayList<>()));
        citySpin.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,new ArrayList<>()));
        setInvisible(mainLayout);
        setVisible(addLayout);
    }

    protected void closeGymSearch(){
        setInvisible(addLayout);
        setVisible(mainLayout);
    }

    protected void openGymCreate(){
        activeActivity = R.layout.activity_gym_new;
        startActivity(new Intent(this, NewGymActivity.class));
    }

    @Override
    protected void responseHandler(JSONObject obj, int handler) {
        if(obj != null) {
            GymMainActivity.this.runOnUiThread(() -> {
                if(handler == 1) {
                    addCountries(obj);
                }
            });
        }
    }

    @Override
    protected void responsePostHandler(JSONObject obj, int handler) {
        if(obj != null) {
            this.runOnUiThread(() -> {
                if(handler == 1) {
                    addGymsToList(obj);
                }else if(handler == 2){
                    addProvinces(obj);
                }else if(handler == 3){
                    addCities(obj);
                }else if(handler == 4) {
                    addCountries(obj);
                }else if(handler == 5) {
                    AllGyms(obj);
                }
            });
        }
    }

    private void addGymsToList(JSONObject obj){
        try {
            JSONArray array;
            array = obj.getJSONArray("GymEquipment");
            for(int i = 0; array.length() > i ; ++i){
                JSONObject json = array.getJSONObject(i);

                int GymSID = json.getInt("GymSID");
                int eqSID = json.getInt("EquipmentSID");

                if(!gymEquipment.containsKey(GymSID)){
                    gymEquipment.put(GymSID,new ArrayList<>());
                }

                List<Integer> gymEquip = gymEquipment.get(GymSID);

                Objects.requireNonNull(gymEquip).add(eqSID);

                gymEquipment.put(GymSID,gymEquip);
            }

            array = obj.getJSONArray("UserGyms");
            for(int i = 0; array.length() > i ; ++i){
                JSONObject json = array.getJSONObject(i);

                int GymSID = json.getInt("GymSID");
                String gymLabel = json.getString("GymLabel");

                myGymNames.put(GymSID,json.getString("GymLabel"));

                if(json.getBoolean("IsFavourite")) {
                    favGymID = json.getInt("GymSID");
                }
                sv_layout.addView(createGymCard(gymLabel,GymSID));
            }

            array = obj.getJSONArray("Equipment");
            for(int i = 0; array.length() > i ; ++i){
                JSONObject json = array.getJSONObject(i);

                int eqSID = json.getInt("EquipmentSID");
                String eqLabel = json.getString("EquipmentLabel");

                equipmentNames.put(eqSID,eqLabel);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private CardView createGymCard(String label,int SID){
        CardView cv = new CardView(this);

        LinearLayout cv_ll = getLinearLayout(this,LinearLayout.HORIZONTAL);
        LinearLayout center_ll = getLinearLayout(this,LinearLayout.VERTICAL);
        LinearLayout center_u_ll = getLinearLayout(this,LinearLayout.HORIZONTAL);
        LinearLayout end_ll = getLinearLayout(this,LinearLayout.VERTICAL);

        ImageView logo_image = getImageView(this,R.drawable.ic_settings,120,120);


        TextView nameLabel = getTextView(this,label,22,30,190,10,16,0,0,Gravity.CENTER);
        TextView eqLabel = getTextView(this,getString(R.string.gymcard_equip),16,24,87,12,8,2,0,Gravity.CENTER);
        TextView eqTotal = getTextView(this,String.valueOf(getEquipSize(SID)),16,24,87,10,8,2,0,Gravity.CENTER);
        TextView eqButton = getTextView(this,getString(R.string.gymcard_equiplist),16,48,190,10,8,2,0,Gravity.CENTER);

        cv_ll.addView(logo_image);
        center_u_ll.addView(eqLabel);
        center_u_ll.addView(eqTotal);

        center_ll.addView(nameLabel);
        center_ll.addView(center_u_ll);
        center_ll.addView(eqButton);

        ImageButton favBtn = new ImageButton(this);

        if(favGymID == SID){
            favBtn.setImageResource(R.drawable.ic_star_full);
        }else{
            favBtn.setImageResource(R.drawable.ic_star_empty);
        }

        ImageButton editBtn = new ImageButton(this);
        editBtn.setImageResource(R.drawable.ic_settings);

        end_ll.addView(favBtn);
        end_ll.addView(editBtn);
        cv_ll.addView(center_ll);
        cv_ll.addView(end_ll);
        cv.addView(cv_ll);

        return cv;
    }

    private void addCountries(JSONObject obj){
        countryNames.clear();
        provincesNames.clear();
        cityNames.clear();
       try {
            JSONArray countriesObj = obj.getJSONArray("Countries");

            List<String> countryList = new ArrayList<>();

            countryList.add("Country");

            for(int i = 0; i< countriesObj.length(); i++){

                JSONObject country = countriesObj.getJSONObject(i);

                int SID = country.getInt("CountrySID");
                String label = country.getString("CountryLabel");

                countryNames.put(SID,label);
                countryList.add(label);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,countryList);

            countrySpin.setAdapter(arrayAdapter);

            countrySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    JSONObject json = getBasicSIDJson();
                    String string = adapterView.getItemAtPosition(i).toString();
                    if(!string.equals("Country")) {
                        try {
                            selectedCountrySID = getCountrySIDFromString(string);
                            json.put("CountrySID", selectedCountrySID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getObjectFromPost("https://fitrickapi.azurewebsites.net/location/provinces", json, 2);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void addProvinces(JSONObject obj){
        JSONArray provObj;
        provincesNames.clear();
        try {
            provObj = obj.getJSONArray("Provinces");

            List<String> provList = new ArrayList<>();

            provList.add("Provinces");

            for (int i = 0; i < provObj.length(); i++) {

                JSONObject province = provObj.getJSONObject(i);

                int SID = province.getInt("ProvinceSID");
                String label = province.getString("ProvinceLabel");

                provincesNames.put(SID,label);
                provList.add(label);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, provList);

            provSpin.setAdapter(arrayAdapter);

            provSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    JSONObject json = getBasicSIDJson();
                    String string = adapterView.getItemAtPosition(i).toString();
                    if(!string.equals("Provinces")) {
                        try {
                            selectedProvinceSID = getProvinceSIDFromString(string);
                            json.put("ProvinceSID", selectedProvinceSID);
                            json.put("CountrySID", selectedCountrySID);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        getObjectFromPost("https://fitrickapi.azurewebsites.net/location/cities", json, 3);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void addCities(JSONObject obj){
        JSONArray cityObj;
        cityNames.clear();
        try {
            cityObj = obj.getJSONArray("Cities");

            List<String> cityList = new ArrayList<>();

            cityList.add("Cities");

            for (int i = 0; i < cityObj.length(); i++) {

                JSONObject province = cityObj.getJSONObject(i);

                int SID = province.getInt("CitySID");
                String label = province.getString("CityLabel");

                cityNames.put(SID,label);
                cityList.add(label);
            }
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, cityList);

            citySpin.setAdapter(arrayAdapter);

            citySpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String string = adapterView.getItemAtPosition(i).toString();
                    if(!string.equals("Provinces")) {
                       selectedCitySID = getCitySIDFromString(string);
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void AllGyms(JSONObject obj){
        JSONArray publicGymObj;
        try {
            publicGymObj = obj.getJSONArray("PublicGyms");




        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private int getCountrySIDFromString(String label){
        int answer = 0;

        for(Map.Entry<Integer,String> entry: countryNames.entrySet()){
            if(entry.getValue().equals(label)){
                answer = entry.getKey();
                break;
            }
        }

        return answer;
    }

    private int getProvinceSIDFromString(String label){
        int answer = 0;
        for(Map.Entry<Integer,String> entry: provincesNames.entrySet()){
            if(entry.getValue().equals(label)){
                answer = entry.getKey();
                break;
            }
        }
        return answer;
    }

    private int getCitySIDFromString(String label){
        int answer = 0;
        for(Map.Entry<Integer,String> entry: cityNames.entrySet()){
            if(entry.getValue().equals(label)){
                answer = entry.getKey();
                break;
            }
        }
        return answer;
    }

    private int getEquipSize(int SID){
        return Objects.requireNonNull(gymEquipment.get(SID)).size();
    }

}
