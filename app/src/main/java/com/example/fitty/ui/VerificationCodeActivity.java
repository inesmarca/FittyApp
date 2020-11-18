package com.example.fitty.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitty.AppPreferences;
import com.example.fitty.FittyApp;
import com.example.fitty.R;
import com.example.fitty.databinding.VerificationCodeActivityBinding;
import com.example.fitty.api.models.EmailVerification;
import com.example.fitty.api.models.Error;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.google.android.material.textfield.TextInputEditText;

public class VerificationCodeActivity extends AppCompatActivity {

    VerificationCodeActivityBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = VerificationCodeActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.txtVerificationCode1.addTextChangedListener(new CustomTextWatcher(binding.txtVerificationCode2));
        binding.txtVerificationCode1.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        binding.txtVerificationCode2.addTextChangedListener(new CustomTextWatcher(binding.txtVerificationCode3));
        binding.txtVerificationCode2.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        binding.txtVerificationCode3.addTextChangedListener(new CustomTextWatcher(binding.txtVerificationCode4));
        binding.txtVerificationCode3.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        binding.txtVerificationCode4.addTextChangedListener(new CustomTextWatcher(binding.txtVerificationCode5));
        binding.txtVerificationCode4.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        binding.txtVerificationCode5.addTextChangedListener(new CustomTextWatcher(binding.txtVerificationCode6));
        binding.txtVerificationCode5.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        binding.txtVerificationCode6.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        binding.btnResendCode.setOnClickListener(v->{
            FittyApp fitty = (FittyApp) getApplication();
            fitty.getUserRepository().resendVerification();
            Toast.makeText(getApplicationContext(), R.string.resend_code_toast,Toast.LENGTH_SHORT).show();
        });

        binding.btnValidateCode.setOnClickListener(v->{
            FittyApp fitty = (FittyApp) getApplication();
            AppPreferences preferences = new AppPreferences(fitty);
            System.out.println(preferences.getEmail());
            fitty.getUserRepository().verify(new EmailVerification(preferences.getEmail(),generateCode())).observe(this,r->{
                if(r.getStatus()== Status.SUCCESS){
                    Toast.makeText(getApplicationContext(),R.string.welcome,Toast.LENGTH_LONG).show();

                }
                else
                    defaultResourceHandler(r);

            });
        });
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

    private String generateCode(){
        StringBuilder stringBuilder = new StringBuilder(binding.txtVerificationCode1.getText().toString());
        stringBuilder.append(binding.txtVerificationCode2.getText().toString());
        stringBuilder.append(binding.txtVerificationCode3.getText().toString());
        stringBuilder.append(binding.txtVerificationCode4.getText().toString());
        stringBuilder.append(binding.txtVerificationCode5.getText().toString());
        stringBuilder.append(binding.txtVerificationCode6.getText().toString());
        return stringBuilder.toString();


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
                if(error.getCode()==8)
                    Toast.makeText(getApplicationContext(),R.string.bad_code,Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(),R.string.error_feo,Toast.LENGTH_SHORT).show();

                break;
        }
    }

}
