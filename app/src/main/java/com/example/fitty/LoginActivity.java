package com.example.fitty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitty.api.ApiClient;
import com.example.fitty.api.UserApiService;
import com.example.fitty.databinding.LoginActivityBinding;
import com.example.fitty.models.Error;
import com.example.fitty.models.UserCredentials;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    LoginActivityBinding binding;
    AppPreferences preferences ;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.dividerLogin.txtDividerText.setText(getString(R.string.divider_login_text));

        binding.btnToSignup.setOnClickListener((view) -> {
            Intent signupIntent = new Intent(this, SignupActivity.class);
            startActivity(signupIntent);
        });

        binding.btnHack.setOnClickListener((view) -> {
            Intent homeIntent = new Intent(this, MainActivity.class);
            startActivity(homeIntent);
        });

        binding.btnHack2.setOnClickListener((view) -> {
            Intent verificationCodeIntent = new Intent(this, VerificationCodeActivity.class);
            startActivity(verificationCodeIntent);
        });
        binding.btnLogin.setOnClickListener(v->{
            TextInputLayout email = binding.txtLoginEmail;
            TextInputLayout pass = binding.txtLoginPassword;
            FittyApp app = (FittyApp) getApplication();
            app.getUserRepository().login(new UserCredentials(email.getEditText().getText().toString(),pass.getEditText().getText().toString())).observe(this, t->{
                if (t.getStatus() == Status.SUCCESS) {
                    AppPreferences preferences = new AppPreferences(app);
                    preferences.setAuthToken(t.getData().getToken());
                    Toast.makeText(getApplicationContext(), R.string.welcome, Toast.LENGTH_SHORT).show();
                    Intent homeIntent = new Intent(this, MainActivity.class);
                    startActivity(homeIntent);
                } else {
                    defaultResourceHandler(t);
                }

            });
        });
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
                //binding.result.setText(message);
                break;
        }
    }
}
