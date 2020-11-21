package com.example.fitty;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.fitty.databinding.ActivityMainBinding;
import com.example.fitty.models.Routine;
import com.example.fitty.repository.Status;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    MaterialToolbar toolbar;
    private Boolean gotLink = false;
    private Integer idLink;
    private AppPreferences preferences;
    FittyApp app;

    private void initNavigation() {

        navController =  Navigation.findNavController(this, R.id.main_nav_host_fragment);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            toolbar.getMenu().clear();
            toolbar.setTitle(destination.getLabel());
            toolbar.setNavigationIcon(R.drawable.ic_account_circle);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (FittyApp) getApplication();
        preferences = new AppPreferences(app);

        Intent intent = getIntent();
        String action = intent.getAction();
        Uri data = intent.getData();

        if(data != null){
            gotLink = true;
            idLink = Integer.parseInt(data.getLastPathSegment());
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.topAppBar);
        initNavigation();

        if (preferences.getAuthToken() == null) {
            // No hay usuario
            Intent goToLogIn = new Intent(this, LoginActivity.class);
            startActivityForResult(goToLogIn, 2);
        } else {
            if (idLink != null) {
                app.getRoutineRepository().getRoutineById(idLink).observe(this, r -> {
                    if(r.getStatus() == Status.SUCCESS) {
                        assert r.getData() != null;
                        Routine routine = r.getData();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("routine", routine);
                        navController.navigate(R.id.action_home_to_routineView, bundle);
                    }
                });
            }
        }
    }

    @SuppressLint("ResourceType")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2) {
            preferences.setAuthToken(data.getStringExtra("token"));
            navController.navigate(R.id.action_home_self);
            if (idLink != null) {
                app.getRoutineRepository().getRoutineById(idLink).observe(this, r -> {
                    if(r.getStatus() == Status.SUCCESS) {
                        assert r.getData() != null;
                        Routine routine = r.getData();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("routine", routine);
                        navController.navigate(R.id.action_home_to_routineView, bundle);
                    }
                });
            }
        }
    }
}