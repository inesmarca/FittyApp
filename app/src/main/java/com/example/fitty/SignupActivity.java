package com.example.fitty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.UserApiService;
import com.example.fitty.databinding.SignupActivityBinding;
import com.example.fitty.models.Error;
import com.example.fitty.models.User;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Date;

public class SignupActivity extends AppCompatActivity {

    User userToAdd; //ViewModelear esto
    SignupActivityBinding binding;

    String[] genders;
    String gender=null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignupActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // binding.btnSignup.setEnabled(false);

        binding.dividerSignup.txtDividerText.setText(getString(R.string.divider_signup_text));

        binding.btnToLogin.setOnClickListener((view) -> {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        });
        userToAdd= new User();


        binding.btnSignupNext.setOnClickListener((view) -> {
            binding.linearLayoutSignupEtapa1.setVisibility(View.GONE);
            binding.linearLayoutSignupEtapa2.setVisibility(View.VISIBLE);
        });

        binding.btnSignupBack.setOnClickListener((view) -> {
            binding.linearLayoutSignupEtapa2.setVisibility(View.GONE);
            binding.linearLayoutSignupEtapa1.setVisibility(View.VISIBLE);
        });

        genders = new String[]{getString(R.string.hint_gender), getString(R.string.female), getString(R.string.male), getString(R.string.other)};

        binding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Si i == 0 entonces no selecciono un genero aun
               /* if(i != 0) {
                    //A completar
                    // en genders[i] esta el sexo seleccionado


                }*/
                switch (i) {
                    case (0):
                        gender=null;
                        //Toast.makeText(getApplicationContext(),R.string.genderError,Toast.LENGTH_SHORT).show();
                        break;
                    case(1):
                        gender = "female";
                        break;
                    case(2):
                        gender = "male";
                        break;
                    case(3):
                        gender = "other";
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(getApplicationContext(),R.string.genderError,Toast.LENGTH_SHORT).show();

            }
        });
        binding.btnSignup.setOnClickListener(v->{
            FittyApp app = (FittyApp)getApplication();

            userToAdd.setBirthdate(new Date(Long.parseLong(getText(binding.txtBirthdate))));
            userToAdd.setEmail(getText(binding.txtSignupEmail));
            userToAdd.setFullName(getText(binding.txtSignupFullName));
            userToAdd.setPassword(getText(binding.txtSignupPassword));
            userToAdd.setGender(gender);
            userToAdd.setUsername(getText(binding.txtSignupUsername));
            app.getUserRepository().addUser(userToAdd).observe(this,r-> {
                if (r.getStatus() == Status.SUCCESS) {
                    AppPreferences preferences = new AppPreferences(app);
                    preferences.setEmail(r.getData().getEmail());
                    preferences.setUsername(r.getData().getUsername());
                    userToAdd = r.getData();
                    Intent goToVerifyCode = new Intent(this, VerificationCodeActivity.class);
                    startActivity(goToVerifyCode);
                } else {
                    defaultResourceHandler(r);
                    //HCIear ACAAAAAAAa
                }
            });
        });
        ArrayAdapter gendersAdapter = new ArrayAdapter(this, R.layout.gender_spinner, genders); //android.R.layout.simple_spinner_item
        gendersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGender.setAdapter(gendersAdapter);



    }
    private String getText (TextInputLayout t){
        return t.getEditText().getText().toString();
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
                if(gender==null)
                    Toast.makeText(getApplicationContext(),R.string.genderError,Toast.LENGTH_SHORT).show();

                //binding.result.setText(message);
                break;
        }
    }
}
