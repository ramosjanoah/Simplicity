package com.example.ramosjanoah.simplicity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONException;

import java.io.IOException;
import java.net.URISyntaxException;

/**
 * Created by Personal on 2/25/2017.
 */

public class Updater extends AsyncTask<String, String, String>{
    SUser userToUpdate;

    public Updater(SUser input){
        userToUpdate = input;
    }
    @Override
    protected String doInBackground(String... strings) {
        try {
            userToUpdate.updateUser();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onPostExecute(String s) {
    }
}
