package com.example.ramosjanoah.simplicity;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.provider.ContactsContract;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.SQLOutput;
import java.util.Map;
//import org.apache.http;

import javax.net.ssl.HttpsURLConnection;

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

    public static final String DEFAULT_FULLNAME = "Guest";
    public static final String DEFAULT_NATIONALITY = "Indonesia";
    public static final int DEFAULT_HEALTH = 50;
    public static final int MIN_HEALTH = 0;
    public static final int MAX_HEALTH = 100;
    public static final int DEFAULT_MUSCLE = 50;
    public static final int MIN_MUSCLE = 0;
    public static final int MAX_MUSCLE = 100;
    public static final String DEFAULT_PHOTO = "TBD";
    public static final String DEFAULT_EMAIL = "guest@guest.com";


    public SUser() {
        email = null;
        fullname = DEFAULT_FULLNAME;
        nationality = DEFAULT_NATIONALITY;
        health = DEFAULT_HEALTH;
        muscle = DEFAULT_MUSCLE;
        photo = DEFAULT_PHOTO;
    }

    public SUser(int type) {
        if (type == 0) {
            email = "guest@email.com";
            fullname = DEFAULT_FULLNAME;
            nationality = DEFAULT_NATIONALITY;
            health = DEFAULT_HEALTH;
            muscle = DEFAULT_MUSCLE;
            photo = DEFAULT_PHOTO;
        }
    }

    public SUser(String email) {
        this.email = email;
        fullname = DEFAULT_FULLNAME;
        nationality = DEFAULT_NATIONALITY;
        health = DEFAULT_HEALTH;
        muscle = DEFAULT_MUSCLE;
        photo = DEFAULT_PHOTO;
    }

    public SUser(String email, String nationality) {
        this.email = email;
        fullname = DEFAULT_FULLNAME;
        this.nationality = nationality;
        health = DEFAULT_HEALTH;
        muscle = DEFAULT_MUSCLE;
        photo = DEFAULT_PHOTO;
    }

    public SUser(String email, String fullname, String nationality, int health, int muscle) {
        this.email = email;
        this.fullname = fullname;
        this.nationality = nationality;
        this.health = health;
        this.muscle = muscle;
        photo = DEFAULT_PHOTO;
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
        System.out.println(jsonstring);
        JSONObject obj = new JSONObject(jsonstring);

        fullname = obj.getString("fullname");
        nationality = obj.getString("nationality");
        health = obj.getInt("health");
        muscle = obj.getInt("muscle");
        email = obj.getString("email");
    }

    public void writeUser(String password) throws IOException, JSONException, URISyntaxException {
        String urllink = "https://ramosjanoah.herokuapp.com/insert.php";
        //photo = "TBD";
        URL url = new URL(urllink);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);

        //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        //List<NameValuePair> params = new ArrayList<NameValuePair>();
        ContentValues values = new ContentValues();
        values.put("email",this.email);
        values.put("fullname",fullname);
        values.put("health",muscle);
        values.put("muscle",muscle);
        values.put("nationality",nationality);
        values.put("photo",photo);
        values.put("password", password);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(values));
        writer.flush();
        writer.close();
        int response = conn.getResponseCode();
        System.out.println("COLOR SEKOP : " + response);
        os.close();
        //response = conn.getResponseCode();
        conn.connect();

        //String postParameters = "&email=" + email + "&fullname=" + fullname +
        //        "&health=" + health + "&muscle=" + muscle + "&nationality=" + nationality + "&photo=" + photo;
        //System.out.println(postParameters);


        //conn.setFixedLengthStreamingMode(postParameters.getBytes().length);
        //PrintWriter out = new PrintWriter(conn.getOutputStream());
        //out.print(postParameters);

        //out.close();
    }

    public void updateUser() throws IOException, JSONException, URISyntaxException {
        String urllink = "https://ramosjanoah.herokuapp.com/update.php";
        URL url = new URL(urllink);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);

        //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        //List<NameValuePair> params = new ArrayList<NameValuePair>();
        ContentValues values = new ContentValues();
        values.put("email",this.email);
        values.put("fullname",fullname);
        values.put("health",health);
        values.put("muscle",muscle);
        values.put("nationality",nationality);
        values.put("photo",photo);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(values));
        writer.flush();
        writer.close();
        int response = conn.getResponseCode();
        System.out.println("2 PADUNG : " + response);
        os.close();
        //response = conn.getResponseCode();
        conn.connect();

        //String postParameters = "&email=" + email + "&fullname=" + fullname +
        //        "&health=" + health + "&muscle=" + muscle + "&nationality=" + nationality + "&photo=" + photo;
        //System.out.println(postParameters);

        //conn.setFixedLengthStreamingMode(postParameters.getBytes().length);
        //PrintWriter out = new PrintWriter(conn.getOutputStream());
        //out.print(postParameters);

        //out.close();
    }

    public void updatePhoto() throws IOException, JSONException, URISyntaxException {
        String urllink = "https://ramosjanoah.herokuapp.com/update.php";
        URL url = new URL(urllink);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setReadTimeout(10000);
        conn.setConnectTimeout(15000);

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);

        //conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

        //List<NameValuePair> params = new ArrayList<NameValuePair>();
        ContentValues values = new ContentValues();
        values.put("email",this.email);
        values.put("photo",this.photo);

        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(getQuery(values));
        writer.flush();
        writer.close();
        int response = conn.getResponseCode();
        System.out.println("2 AZU : " + response);
        os.close();
        //response = conn.getResponseCode();
        conn.connect();

        //String postParameters = "&email=" + email + "&fullname=" + fullname +
        //        "&health=" + health + "&muscle=" + muscle + "&nationality=" + nationality + "&photo=" + photo;
        //System.out.println(postParameters);

        //conn.setFixedLengthStreamingMode(postParameters.getBytes().length);
        //PrintWriter out = new PrintWriter(conn.getOutputStream());
        //out.print(postParameters);

        //out.close();
    }


    private String getQuery(ContentValues values) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, Object> entry : values.valueSet()) {
            if (first)
                first = false;
            else
                result.append("&");
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(String.valueOf(entry.getValue()), "UTF-8"));
        }

        //Log.i("Result", result.toString() + " " + String.valueOf(response));
        System.out.println(result.toString());
        return result.toString();
    }
}
