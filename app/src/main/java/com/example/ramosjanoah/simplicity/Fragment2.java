package com.example.ramosjanoah.simplicity;

import android.content.Context;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import static android.content.Context.SENSOR_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * create an instance of this fragment.
 */
public class Fragment2 extends Fragment implements SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView HealthTextView;
    private TextView MuscleTextView;
    public static final String USER_PREFERENCE = "User_Reference";



    private OnFragmentInteractionListener mListener;

    public Fragment2() {
        // Required empty public constructor
    }

    @Override
    public void onAccuracyChanged (Sensor s, int n){

    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment Fragment2.
     */
    /*
    // TODO: Rename and change types and number of parameters
    public static Fragment2 newInstance(String param1, String param2) {
        Fragment2 fragment = new Fragment2();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
*/
    private Sensor accelerometer;
    private SensorManager sm;

    private long curTime, lastUpdate;
    private float x, y, z, lastX, lastY, lastZ;
    private final static long UPDATE_PERIOD = 300;
    private final static int SHAKE_THRESHOLD = 800;
    private SharedPreferences sp;
    TextView healthPoints;
    //EditText healthInput;

    Intent mServiceIntent;

    private void initialize() {
        sm = (SensorManager) getActivity().getSystemService(SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this,accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        curTime = lastUpdate = (long)0.0;
        x = y = z = lastX = lastY = lastZ = (float)0.0;
        //System.out.println("initialized run()");
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long curTime = System.currentTimeMillis();
        SharedPreferences.Editor spEditor = sp.edit();
        if ((curTime - lastUpdate) > UPDATE_PERIOD) {
            long diffTime = (curTime - lastUpdate);
            lastUpdate = curTime;
            //System.out.println("onSensorChanged if 1 run()");
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            float speed = Math.abs(x + y + z - lastX - lastY - lastZ) / diffTime * 10000;

            if (speed > SHAKE_THRESHOLD) {
                //Toast.makeText(this, "Shake detected w/ speed: " +speed,  Toast.LENGTH_SHORT).show();
                int points = Integer.parseInt(healthPoints.getText().toString());
                points = points+3;
                healthPoints.setText(Integer.toString(points));
                //System.out.println("onSensorChanged if 2 run()");
                spEditor.putInt("USER_HEALTH", points);
                spEditor.commit();
            }
            lastX = x;
            lastY = y;
            lastZ = z;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_fragment2, container, false);
        HealthTextView = (TextView) view.findViewById(R.id.HealthTextView);
        MuscleTextView = (TextView) view.findViewById(R.id.MuscleTextView);

/*
        healthPoints = (TextView) view.findViewById(R.id.HealthTextView);
        this.initialize();
        */

        //healthInput = (EditText) findViewById(R.id.sementara);

        //Button switchX = (Button) findViewById(R.id.switchX);

        sp = getActivity().getSharedPreferences(USER_PREFERENCE, Context.MODE_PRIVATE);
        if (sp.getInt("USER_HEALTH", -1) == -1) {
            HealthTextView.setText("Health Not Found");
            MuscleTextView.setText("Muscle Not Found");
        } else {
            HealthTextView.setText(String.valueOf(sp.getInt("USER_HEALTH", -1)));
            MuscleTextView.setText(String.valueOf(sp.getInt("USER_MUSCLE", -1)));

            //Service Intent Start
            mServiceIntent = new Intent(getActivity(), bgService.class);
            mServiceIntent.putExtra("Health",sp.getInt("USER_HEALTH",-1));
            getActivity().startService(mServiceIntent);
        }


        healthPoints = (TextView) view.findViewById(R.id.HealthTextView);
        this.initialize();



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    /*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
