package com.fitgroup.fitpower.base;


import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fitgroup.fitpower.R;
import com.fitgroup.fitpower.utils.StaticVar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectiveActivity extends AppCompatActivity {

    protected final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    protected void getObjectFromRequest(String url, int handler){

       OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    JSONObject obj = null;
                    try {
                        final String resp = response.body().string();
                        obj = new JSONObject(resp);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                    responseHandler(obj,handler);
                }
            }
        });
    }

    protected void getObjectFromPost(String url, JSONObject json,int handler){

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON,json.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                if(response.isSuccessful()){
                    JSONObject obj = null;
                    try {
                        final String resp = response.body().string();
                        obj = new JSONObject(resp);
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                        obj = new JSONObject();
                    }

                    responsePostHandler(obj,handler);
                }
            }
        });
    }

    protected LinearLayout getLinearLayout(Context cntxt, int orientation){
        LinearLayout layout = new LinearLayout(cntxt);
        layout.setOrientation(orientation);
        return layout;
    }

    protected TextView getTextView(Context cntxt,String text,float textSize,int dpHeight,int dpWidth,int leftM,int topM,int rightM, int bottomM, int gravity){
        TextView view = new TextView(cntxt);
        view.setText(text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DPtoPX(dpWidth),DPtoPX(dpHeight));
        params.gravity = gravity;
        params.setMargins(leftM,topM,rightM,bottomM);
        view.setLayoutParams(params);
        view.setTextSize(textSize);

        return view;
    }

    protected EditText getEditText(Context cntxt, @Nullable String text, int dpHeight, int dpWidth, int leftM, int topM, int rightM, int bottomM,int minHeight, int gravity){
        EditText view = new EditText(cntxt);
        if(text != null) {
            view.setText(text);
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DPtoPX(dpWidth),DPtoPX(dpHeight));
        params.setMargins(leftM,topM,rightM,bottomM);
        params.gravity = gravity;
        view.setLayoutParams(params);
        view.setMinHeight(DPtoPX(minHeight));

        return view;
    }

    protected ImageView getImageView(Context cntxt,int drawable,int dpHeight,int dpWidth){
        ImageView view = new ImageView(cntxt);
        view.setImageResource(drawable);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(DPtoPX(dpWidth),DPtoPX(dpHeight));
        view.setLayoutParams(params);
        return view;
    }


    protected void responsePostHandler(JSONObject obj, int handler){

    }


    protected void responseHandler(JSONObject obj, int handler){


    }

    protected int DPtoPX(int DP){
        Resources r = this.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                DP,
                r.getDisplayMetrics()
        );
    }

    protected JSONObject SendImage(File file,String mediaType,String fileName,String link){

        MediaType MEDIA_TYPE = MediaType.parse(mediaType);

        RequestBody req = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(link,fileName,RequestBody.create(MEDIA_TYPE, file)).build();


        return new JSONObject();
    }

    protected JSONObject getBasicSIDJson(){

        JSONObject json = new JSONObject();

        try {
            json.put("AccountInfoSID", StaticVar.accountInfoSID);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;

    }

}
