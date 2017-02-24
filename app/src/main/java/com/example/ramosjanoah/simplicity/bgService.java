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
        SharedPreferences sp = this.getSharedPreferences("User_Reference", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();
        int health_data = 999;
        while(health_data>0) {
            health_data=sp.getInt("USER_HEALTH",-1);
            health_data--;
            System.out.println("Health deprecated");
            spEditor.putInt("USER_HEALTH", health_data);
            spEditor.commit();
            System.out.println(sp.getInt("USER_HEALTH", -1));
            if (health_data>120){
                try {
                    Thread.sleep(800);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

