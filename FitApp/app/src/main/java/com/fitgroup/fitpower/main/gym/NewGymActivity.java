package com.fitgroup.fitpower.main.gym;

import static com.fitgroup.fitpower.utils.StaticVar.activeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import java.util.Map;
import java.util.Objects;


public class NewGymActivity extends NavActivity {

    private final Map<Integer,String> equipmentList = new HashMap<>();
    private final Map<Integer,String> equipmentAdd = new HashMap<>();

    private final Map<Integer,String> provinceNames = new HashMap<>();
    private final Map<Integer,String> countryNames = new HashMap<>();

    private LinearLayout scrollViewLayout;
    private LinearLayout horSV;

    private Spinner provSpin;
    private Spinner countrySpin;

    public NewGymActivity() {
        super(R.id.new_gym_drawer, R.id.new_gym_nav,R.layout.activity_gym_new);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scrollViewLayout = findViewById(R.id.new_gym_eq_lo);
        horSV = findViewById(R.id.new_gym_add_lo);
        Button button = findViewById(R.id.gym_add_btn);
        provSpin = findViewById(R.id.new_gym_cntry_sp);
        countrySpin = findViewById(R.id.new_gym_prov_sp);
        getObjectFromPost("https://fitrickapi.azurewebsites.net/location/countries",getBasicSIDJson(),4);
        getObjectFromPost("https://fitrickapi.azurewebsites.net/select/equipment",getBasicSIDJson(),3);

        button.setOnClickListener((btn) -> postAddGymJson());

    }

    private void postAddGymJson(){

        JSONObject json = getBasicSIDJson();

        if(equipmentAdd.size() > 0){
            JSONArray array = new JSONArray();

            for(Map.Entry<Integer,String> entry: equipmentAdd.entrySet()){
                array.put(entry.getKey());
            }

            EditText gymName = findViewById(R.id.new_gym_name_field);
            EditText gymAdr1 = findViewById(R.id.new_gym_adr_field);
            EditText gymAdr2 = findViewById(R.id.new_gym_apt_field);
            EditText gymCity = findViewById(R.id.new_gym_city_field);
            EditText gymPostal = findViewById(R.id.new_gym_pc_field);
            Spinner gymCntry = findViewById(R.id.new_gym_prov_sp);
            Spinner gymProv = findViewById(R.id.new_gym_cntry_sp);
            CheckBox gymPublic = findViewById(R.id.new_gym_priv_cb);
            CheckBox gymFav = findViewById(R.id.new_gym_fav_cb);

            int gymFavInt = 0;
            int gymPublicInt = 0;

            String cntry = gymCntry.getSelectedItem().toString();
            String prov = gymProv.getSelectedItem().toString();

            if(gymPublic.isChecked()){
                gymPublicInt = 1;
            }

            if(gymFav.isChecked()){
                gymFavInt = 1;
            }

            try {

                json.put("GymName",gymName.getText().toString());
                json.put("GymAddress1",gymAdr1.getText().toString());
                json.put("GymAddress2",gymAdr2.getText().toString());
                json.put("GymCity",gymCity.getText().toString());
                json.put("GymPostal",gymPostal.getText().toString());
                json.put("GymCountry",getCountrySIDFromString(cntry));
                json.put("GymProvince",getProvinceSIDFromString(prov));
                json.put("GymPublic",gymPublicInt);
                json.put("GymFavourite",gymFavInt);
                json.put("Equipment",array);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        getObjectFromPost("https://fitrickapi.azurewebsites.net/gym/newgym",json,2);

    }

    protected CardView EquipmentCardHandler(String label){

            CardView newCard = new CardView(this);
            TextView tv = new TextView(this);
            ImageView imv = new ImageView(this);
            LinearLayout layout = new LinearLayout(this);
            imv.setImageResource(R.drawable.ic_settings);
            imv.setLayoutParams(new LinearLayout.LayoutParams(220,220));
            tv.setText(label);
            tv.setTextSize(18);
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(700,100);
            param.gravity = Gravity.CENTER;
            tv.setLayoutParams(param);
            layout.addView(imv);
            layout.addView(tv);
            newCard.addView(layout);

            newCard.setOnClickListener(view1 -> {
                if (view1.getParent().equals(scrollViewLayout)) {
                    scrollViewLayout.removeView(view1);
                    horSV.addView(view1);

                    Map.Entry<Integer,String> entry = getFromEquipList(label);

                    equipmentAdd.put(Objects.requireNonNull(entry).getKey(),entry.getValue());
                    equipmentList.remove(entry.getKey());

                } else if (view1.getParent().equals(horSV)) {
                    horSV.removeView(view1);
                    scrollViewLayout.addView(view1);

                    Map.Entry<Integer,String> entry = getFromEquipAdd(label);

                    equipmentList.put(Objects.requireNonNull(entry).getKey(),entry.getValue());
                    equipmentAdd.remove(entry.getKey());
                }
            });
            return newCard;
    }

    @Override
    protected void responsePostHandler(JSONObject obj, int handler) {
        if(obj != null) {
            NewGymActivity.this.runOnUiThread(() -> {
                if (handler == 1) {
                    JSONArray countriesObj;
                    try {
                        countriesObj = obj.getJSONArray("Provinces");

                        List<String> provList = new ArrayList<>();

                        provList.add("Provinces");

                        for (int i = 0; i < countriesObj.length(); i++) {

                            JSONObject countryObj = countriesObj.getJSONObject(i);

                            int provinceSID = countryObj.getInt("ProvinceSID");
                            String province = countryObj.getString("ProvinceLabel");

                            provinceNames.put(provinceSID,province);

                            provList.add(province);
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, provList);

                        provSpin.setAdapter(arrayAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (handler == 2){
                    try {
                        String answer = obj.getString("InsertResponse");

                        if(answer.equals("Success")){
                            activeActivity = R.layout.activity_gym;
                            startActivity(new Intent(this, GymMainActivity.class));
                        }else if(answer.equals("Failed")){

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if (handler == 3) {
                    try {
                        JSONArray arrayObj = obj.getJSONArray("Equipment");
                        for(int i = 0; i< arrayObj.length(); i++){
                            JSONObject json = arrayObj.getJSONObject(i);

                            scrollViewLayout.addView(EquipmentCardHandler(json.getString("EquipmentLabel")));
                            equipmentList.put(json.getInt("EquipmentSID"),json.getString("EquipmentLabel"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else if(handler == 4){
                    try {
                        JSONArray countriesObj = obj.getJSONArray("Countries");

                        List<String> countryList = new ArrayList<>();

                        countryList.add("Country");

                        for(int i = 0; i< countriesObj.length(); i++){

                            JSONObject countryObj = countriesObj.getJSONObject(i);

                            int countrySID = countryObj.getInt("CountrySID");
                            String country = countryObj.getString("CountryLabel");

                            countryNames.put(countrySID,country);
                            countryList.add(country);
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
                                        json.put("CountrySID", getCountrySIDFromString(string));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    getObjectFromPost("https://fitrickapi.azurewebsites.net/location/provinces", json, 1);
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
            });
        }
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
        for(Map.Entry<Integer,String> entry: equipmentAdd.entrySet()) {
            if(entry.getValue().equals(label)) {
                return entry;
            }
        }
        return null;
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
        for(Map.Entry<Integer,String> entry: provinceNames.entrySet()){
            if(entry.getValue().equals(label)){
                answer = entry.getKey();
                break;
            }
        }
        return answer;
    }

}
