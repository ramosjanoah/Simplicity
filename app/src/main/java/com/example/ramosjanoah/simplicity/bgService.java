package com.example.ramosjanoah.simplicity;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;

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


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.logo3);
        mBuilder.setContentTitle("Your health has emptied!!");
        mBuilder.setContentText("Your health has depleted! Quickly recharge it!");

        SharedPreferences sp = this.getSharedPreferences("User_Reference", Context.MODE_PRIVATE);
        SharedPreferences.Editor spEditor = sp.edit();

        userToUpdate = new SUser();
        lastFullname = sp.getString("USER_FULLNAME",SUser.DEFAULT_FULLNAME);
        lastNationality = sp.getString("USER_NATIONALITY",SUser.DEFAULT_NATIONALITY);
        lastEmail = sp.getString("USER_EMAIL",SUser.DEFAULT_EMAIL);

        userToUpdate.setEmail(lastEmail);
        userToUpdate.setFullname(lastFullname);
        userToUpdate.setNationality(lastNationality);

        int health_data = 999;
        while(health_data>0) {
            health_data=sp.getInt("USER_HEALTH",-1);
            health_data--;
            System.out.print("Health : ");
            spEditor.putInt("USER_HEALTH", health_data);
            spEditor.commit();
            System.out.println(sp.getInt("USER_HEALTH", -1));

            //Update Database
            lastHealth = sp.getInt("USER_HEALTH",SUser.DEFAULT_HEALTH);
            lastMuscle = sp.getInt("USER_MUSCLE",SUser.DEFAULT_MUSCLE);

            userToUpdate.setHealth(lastHealth);
            userToUpdate.setMuscle(lastMuscle);
            Updater u = new Updater("GENERAL",userToUpdate);
            u.execute();
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

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }
    }
}

