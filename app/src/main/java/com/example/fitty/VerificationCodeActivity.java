package com.example.fitty;

import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitty.databinding.VerificationCodeActivityBinding;
import com.google.android.material.textfield.TextInputEditText;

public class VerificationCodeActivity extends AppCompatActivity {

    VerificationCodeActivityBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = VerificationCodeActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.txtVerificationCode1.addTextChangedListener(new CustomTextWatcher(binding.txtVerificationCode2));
        binding.txtVerificationCode2.addTextChangedListener(new CustomTextWatcher(binding.txtVerificationCode3));
        binding.txtVerificationCode3.addTextChangedListener(new CustomTextWatcher(binding.txtVerificationCode4));
        binding.txtVerificationCode4.addTextChangedListener(new CustomTextWatcher(binding.txtVerificationCode5));
        binding.txtVerificationCode5.addTextChangedListener(new CustomTextWatcher(binding.txtVerificationCode6));
        binding.txtVerificationCode6.addTextChangedListener(new CustomTextWatcher(null));
    }

    public boolean codeEmpty() {
        return binding.txtVerificationCode1.getText().length() == 0 || binding.txtVerificationCode2.getText().length() == 0 || binding.txtVerificationCode3.getText().length() == 0 || binding.txtVerificationCode4.getText().length() == 0 || binding.txtVerificationCode5.getText().length() == 0 || binding.txtVerificationCode6.getText().length() == 0;
    }



    private class CustomTextWatcher implements TextWatcher {
        TextInputEditText nextTextInputEditText;

        CustomTextWatcher(TextInputEditText nextTextInputEditText) {
            this.nextTextInputEditText = nextTextInputEditText;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() == 1) {
                if(nextTextInputEditText != null)
                    nextTextInputEditText.requestFocus();
            }
        }
    }
}
