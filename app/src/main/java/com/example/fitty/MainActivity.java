package com.example.fitty;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.fitty.databinding.ActivityMainBinding;
import com.google.android.material.appbar.MaterialToolbar;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private NavController navController;
    MaterialToolbar toolbar;

    private void initNavigation() {
        navController =  Navigation.findNavController(this, R.id.main_nav_host_fragment);
        NavigationUI.setupWithNavController(binding.bottomNav, navController);
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            toolbar.setTitle(destination.getLabel());
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppPreferences preferences = new AppPreferences(getApplication());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
      /*  binding.loginButton.setOnClickListener(v->{
            UserApiService userApiService = ApiClient.create(this,UserApiService.class);
            userApiService.login(new UserCredentials("johndoe","1234567890")).observe(this, t->{
                if(t.getError()==null){
                    preferences.setAuthToken(t.getData().getToken());
                    Toast.makeText(getApplicationContext(),preferences.getAuthToken(),Toast.LENGTH_SHORT).show();

                }
            });


        });
        binding.tocame.setOnClickListener(v->{
            if(preferences.getAuthToken()==null)
                Toast.makeText(getApplicationContext(),"No estas logeado rey",Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(getApplicationContext(),"Tas logeado kinga",Toast.LENGTH_SHORT).show();

        });*/
        setContentView(binding.getRoot());
        toolbar = findViewById(R.id.topAppBar);

        initNavigation();
    }

    public void OnCategoryClickListener(int id) {
        Fragment fragment = new CategoryRoutines();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_tag, fragment).commit();
    }
}