package com.example.ramosjanoah.simplicity;

import android.os.AsyncTask;
import android.provider.ContactsContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * Created by ramosjanoah on 2/22/2017.
 */

public class SUser {
    private String email;
    private String fullname;
    private String nationality;
    private int health;
    private int muscle;
    private static String photo;

    private static final String DEFAULT_FULLNAME = "Guest";
    private static final String DEFAULT_NATIONALITY = "Indonesia";
    private static final int DEFAULT_HEALTH = 50;
    private static final int MIN_HEALTH = 0;
    private static final int MAX_HEALTH = 100;
    private static final int DEFAULT_MUSCLE = 50;
    private static final int MIN_MUSCLE = 0;
    private static final int MAX_MUSCLE = 100;


    public SUser() {
        email = null;
        fullname = DEFAULT_FULLNAME;
        nationality = DEFAULT_NATIONALITY;
        health = DEFAULT_HEALTH;
        muscle = DEFAULT_MUSCLE;
    }

    public SUser(int type) {
        if (type == 0) {
            email = "guest@email.com";
            fullname = DEFAULT_FULLNAME;
            nationality = DEFAULT_NATIONALITY;
            health = DEFAULT_HEALTH;
            muscle = DEFAULT_MUSCLE;
        }
    }

    public SUser(String email, String UID) {
        this.email = email;
        fullname = DEFAULT_FULLNAME;
        nationality = DEFAULT_NATIONALITY;
        health = DEFAULT_HEALTH;
        muscle = DEFAULT_MUSCLE;
    }

    public SUser(String email, String fullname, String UID, String nationality, int health, int muscle) {
        this.email = email;
        this.fullname = fullname;
        this.nationality = nationality;
        this.health = health;
        this.muscle = muscle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMuscle() {
        return muscle;
    }

    public void setMuscle(int muscle) {
        this.muscle = muscle;
    }

    public static String getPhoto() {
        return photo;
    }

    public static void setPhoto(String photo) {
        SUser.photo = photo;
    }

    public void getUserProfile() throws IOException, JSONException {
        //uid = "gbkPzSkyg9XeQtlRktRtmIs4QfS2";
        String urllink = "https://ramosjanoah.herokuapp.com/readjson.php?email=" + email;
        System.out.println(urllink);
        URL url = new URL(urllink);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        conn.setRequestMethod("GET");

        // Pembaca echo dari PHP
        //BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        InputStreamReader isr;
        isr = new InputStreamReader(conn.getInputStream());
        BufferedReader in = new BufferedReader(isr);
        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = in.readLine()) != null) {
            sb.append(line).append("\n");
        }

        String jsonstring = sb.toString();
        JSONObject obj = new JSONObject(jsonstring);

        fullname = obj.getString("fullname");
        nationality = obj.getString("nationality");
        health = obj.getInt("health");
        muscle = obj.getInt("muscle");
        email = obj.getString("email");
    }

    public void writeUser() throws IOException, JSONException, URISyntaxException {
        String urllink = "https://ramosjanoah.herokuapp.com/insert.php";
        URL url = new URL(urllink);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        String postParameters = "&email="+email+"&fullname="+fullname+
                "&health="+health+"&muscle="+muscle+"&nationality="+nationality+"&photo="+photo;

        conn.setFixedLengthStreamingMode(postParameters.getBytes().length);
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(postParameters);
        out.close();
    }

    public void updateUser() throws IOException, JSONException, URISyntaxException {
        String urllink = "https://ramosjanoah.herokuapp.com/update.php";
        URL url = new URL(urllink);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
        String postParameters = "&email="+email+"&fullname="+fullname+
                "&health="+health+"&muscle="+muscle+"&nationality="+nationality+"&photo="+photo;

        conn.setFixedLengthStreamingMode(postParameters.getBytes().length);
        PrintWriter out = new PrintWriter(conn.getOutputStream());
        out.print(postParameters);
        out.close();
    }
}
