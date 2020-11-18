package com.example.fitty.ui;

import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitty.api.models.Routine;

public class ExecuteActivity extends AppCompatActivity {

    com.example.fitty.databinding.ExecuteActivityBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        binding = com.example.fitty.databinding.ExecuteActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Routine routine = (Routine) getIntent().getExtras().getSerializable("routine");
    }
}
