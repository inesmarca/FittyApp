package com.example.fitty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitty.databinding.SignupActivityBinding;
import com.google.android.material.datepicker.MaterialDatePicker;

public class SignupActivity extends AppCompatActivity {

    SignupActivityBinding binding;

    String[] genders;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignupActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.dividerSignup.txtDividerText.setText(getString(R.string.divider_signup_text));

        binding.btnToLogin.setOnClickListener((view) -> {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        });

        genders = new String[]{getString(R.string.hint_gender), getString(R.string.female), getString(R.string.male), getString(R.string.other)};

        binding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Si i == 0 entonces no selecciono un genero aun
                /*if(i != 0) {
                    //A completar
                    // en genders[i] esta el sexo seleccionado
                }*/

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //A completar
            }
        });

        ArrayAdapter gendersAdapter = new ArrayAdapter(this, R.layout.gender_spinner, genders); //android.R.layout.simple_spinner_item
        gendersAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGender.setAdapter(gendersAdapter);



    }
}
