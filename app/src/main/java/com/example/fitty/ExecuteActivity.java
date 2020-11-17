package com.example.fitty;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitty.databinding.ExecuteActivityBinding;

public class ExecuteActivity extends AppCompatActivity {

    ExecuteActivityBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        binding = ExecuteActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
