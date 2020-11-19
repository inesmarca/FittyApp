package com.example.fitty;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.appbar.MaterialToolbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Progress#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Progress extends MainFragment {

    View rootView;

    public Progress() {}

    // TODO: Rename and change types and number of parameters
    public static Progress newInstance(String param1, String param2) {
        Progress fragment = new Progress();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_progress, container, false);
        setTitle(getContext().getString(R.string.progreso));
        setTopBar();

        MaterialToolbar toolbar = requireActivity().findViewById(R.id.topAppBar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(rootView).navigate(R.id.action_progress_to_userProfile);
            }
        });

        return rootView;
    }
}