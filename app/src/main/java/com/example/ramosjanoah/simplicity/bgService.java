package com.example.ramosjanoah.simplicity;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;

/**
 * Created by Personal on 2/24/2017.
 */

public class bgService extends IntentService{

    public bgService(){
        super("bgService");
    }
    @Override
    protected void onHandleIntent(Intent workIntent){
        int health_data = workIntent.getIntExtra("Health",0);
        System.out.print("Health = ");
        System.out.println(health_data);
        SharedPreferences sp = this.getSharedPreferences("User_Reference", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();

        while(true) {
            health_data--;
            System.out.println("Health deprecated");
            spEditor.putInt("USER_HEALTH", health_data);
            spEditor.commit();
            System.out.println(sp.getInt("USER_HEALTH", -1));
        }
    }
}

