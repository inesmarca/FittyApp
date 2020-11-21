package com.example.fitty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitty.databinding.LoginActivityBinding;
import com.example.fitty.models.Error;
import com.example.fitty.models.UserCredentials;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    LoginActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = LoginActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.progressBar2.setVisibility(View.GONE);

        binding.dividerLogin.txtDividerText.setText(getString(R.string.divider_login_text));

        binding.btnToSignup.setOnClickListener((view) -> {
            Intent signupIntent = new Intent(this, SignupActivity.class);
            startActivity(signupIntent);
        });

        binding.btnLogin.setOnClickListener(v->{
            TextInputLayout email = binding.txtLoginEmail;
            TextInputLayout pass = binding.txtLoginPassword;
            FittyApp app = (FittyApp) getApplication();
            app.getUserRepository().login(new UserCredentials(email.getEditText().getText().toString(),pass.getEditText().getText().toString())).observe(this, t->{
                if (t.getStatus() == Status.SUCCESS) {
                    binding.progressBar2.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), getString(R.string.welcome,email.getEditText().getText()), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("token", t.getData().getToken());
                    setResult(2, intent);
                    binding.progressBar2.setVisibility(View.GONE);

                    finish();
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
                binding.progressBar2.setVisibility(View.VISIBLE);
                break;
            case ERROR:
                Error error = resource.getError();
                String message = getString(R.string.error, error.getDescription(), error.getCode());
                Log.d("UI", message);
                //binding.result.setText(message);
                if(error.getCode()==4) {
                    if(!error.getDetails().get(0).equals("Password does not match"))
                        Toast.makeText(getApplicationContext(), R.string.username_error, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), R.string.password_error, Toast.LENGTH_SHORT).show();

                }
                else
                    Toast.makeText(getApplicationContext(), R.string.error_feo, Toast.LENGTH_SHORT).show();

                break;
        }
    }
}
