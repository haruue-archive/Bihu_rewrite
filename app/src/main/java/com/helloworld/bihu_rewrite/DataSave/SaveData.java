package com.helloworld.bihu_rewrite.DataSave;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/2/25.
 */
public class SaveData {
    private SharedPreferences sharedPreferences = null;
    private String token = null;

    public String getToken() {
        return token;
    }

    public SaveData(Context context) {
        sharedPreferences = context.getSharedPreferences("account",Context.MODE_PRIVATE);
    }
    public void saveData(String key,String value){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        if (key.equals("token")){
            this.token = value;
        }
        editor.apply();
    }
    public void saveAll(String name,String token,String face){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", name);
        editor.putString("token", token);
//        editor.putString("face", face);
        editor.apply();
        this.token = token;
    }
    public Map<String,String > getAll(){
        Map<String,String> map = new HashMap<String,String>();
        map.put("name",sharedPreferences.getString("name", null));
        map.put("token", sharedPreferences.getString("token", null));
        map.put("face", sharedPreferences.getString("face", null));
        return map;
    }


}
