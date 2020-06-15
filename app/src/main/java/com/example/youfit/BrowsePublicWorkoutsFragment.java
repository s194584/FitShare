package com.example.youfit;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BrowsePublicWorkoutsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrowsePublicWorkoutsFragment extends Fragment {

    private static final String TAG = "BrowsePublicFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_browse_public_workouts, container, false);
    }
}