package com.example.fitty;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitty.databinding.SignupActivityBinding;

public class SignupActivity extends AppCompatActivity {

    SignupActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SignupActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnToLogin.setOnClickListener((view) -> {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        });
    }
}
