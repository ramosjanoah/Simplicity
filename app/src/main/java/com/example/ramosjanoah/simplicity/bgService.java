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
    private String lastFullname;
    private String lastNationality;
    private int lastHealth;
    private int lastMuscle;
    private String lastEmail;
    private String lastPhoto;
    private SUser userToUpdate;


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

            //Update Database
            lastFullname = sp.getString("USER_FULLNAME",SUser.DEFAULT_FULLNAME);
            lastNationality = sp.getString("USER_NATIONALITY",SUser.DEFAULT_NATIONALITY);
            lastEmail = sp.getString("USER_EMAIL",SUser.DEFAULT_EMAIL);
            lastHealth = sp.getInt("USER_HEALTH", SUser.DEFAULT_HEALTH);
            lastHealth = sp.getInt("USER_MUSCLE", SUser.DEFAULT_MUSCLE);
            userToUpdate = new SUser(lastEmail, lastFullname, lastNationality, lastHealth, lastMuscle);

            //Sleep
            if (health_data>120){
                try {
                    Thread.sleep(100);
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

