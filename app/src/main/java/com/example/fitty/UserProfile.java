package com.example.fitty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.example.fitty.models.Error;
import com.example.fitty.models.User;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends SecondaryFragment {

    View rootView;

    public UserProfile() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_user_profile, container, false);


        FittyApp fitty = (FittyApp) getActivity().getApplication();
        fitty.getUserRepository().getCurrentUser().observe(getActivity(),r->{
            if (r.getStatus() == Status.SUCCESS) {
                User user = r.getData();
                TextView username = rootView.findViewById(R.id.username);
                TextInputEditText fullname = rootView.findViewById(R.id.fullname);
                TextInputEditText email = rootView.findViewById(R.id.email);
                TextInputEditText gender = rootView.findViewById(R.id.gender);
                TextInputEditText birthdate = rootView.findViewById(R.id.birthday);
                CircleImageView icon = rootView.findViewById(R.id.profile_image);

                username.setText(user.getUsername());
                fullname.setText(user.getFullName());
                email.setText(user.getUsername());
                gender.setText(user.getGender());
                String pattern = "yyyy-MM-dd";
                @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
                String date = simpleDateFormat.format(user.getBirthdate());
                birthdate.setText(date);
                SvgLoader.pluck()
                        .with(getActivity())
                        .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                        .load(user.getAvatarUrl(), icon);
                rootView.findViewById(R.id.progressBar).setVisibility(View.INVISIBLE);
            } else {
                defaultResourceHandler(r);
            }
        });

        rootView.findViewById(R.id.signOut).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AppPreferences preferences = new AppPreferences(getActivity().getApplication());
                preferences.setAuthToken(null);
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        setTitle(getContext().getString(R.string.profile));
        setTopBar();

        return rootView;
    }

    private void defaultResourceHandler(Resource<?> resource) {
        switch (resource.getStatus()) {
            case LOADING:
                Log.d("UI", getString(R.string.loading));

                break;
            case ERROR:
                Error error = resource.getError();
                String message = getString(R.string.error, error.getDescription(), error.getCode());
                Log.d("UI", message);
                break;
        }
    }
}