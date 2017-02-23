package com.example.ramosjanoah.simplicity;

/**
 * Created by Alvin Lan on 2/22/2017.
 */

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class FragmentPotrait extends Fragment {
    /*
         LayoutInflator takes the provided xml layout and puts it in a view
         container is the view the fragment should be attached to
         savedInstanceState is passed if the fragment is being re-constructed from a saved state
         inflate() is passed the layout xml to place, the optional view to attach to and
         true or false on whether to attach to the optional view named container
          */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.potrait_fragment, container, false);
    }
}