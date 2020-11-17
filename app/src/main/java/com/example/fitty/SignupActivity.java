package com.example.fitty;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fitty.databinding.SignupActivityBinding;
import com.example.fitty.models.Error;
import com.example.fitty.models.User;
import com.example.fitty.repository.Resource;
import com.example.fitty.repository.Status;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import java.util.Locale;

public class SignupActivity extends AppCompatActivity {

    User userToAdd; //ViewModelear esto
    SignupActivityBinding binding;

    String[] genders;
    String gender = null;


    private TextInputLayout fullname, username, password, email;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FittyApp app = (FittyApp) getApplication();
        AppPreferences preferences = new AppPreferences(app);

        if(preferences.getAuthToken().length() > 0) {
            //Logeado
            Intent goToMain = new Intent(this, MainActivity.class);
            startActivity(goToMain);
        }


        binding = SignupActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fullname = binding.txtSignupFullName;
        username = binding.txtSignupUsername;
        password = binding.txtSignupPassword;
        email = binding.txtSignupEmail;

        username.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String val = username.getEditText().getText().toString();
                int len = val.length();
                binding.count.setText(String.format("%d", len));
                if (val.length() > 30)
                    username.setError(getString(R.string.too_long));
                else {
                    username.setError(null);
                    username.setErrorEnabled(false);
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        password.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = getText(password).length();
                if(len<8){
                    password.setError(getString(R.string.too_short_pass));
                }
                else{
                    password.setError(null);
                    password.setErrorEnabled(false);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // binding.btnSignup.setEnabled(false);

        binding.dividerSignup.txtDividerText.setText(getString(R.string.divider_signup_text));

        binding.btnToLogin.setOnClickListener((view) -> {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
        });
        userToAdd = new User();


        binding.btnSignupNext.setOnClickListener((view) -> {
            if (!validateFullname()  | !validateDate() | !validateGender())
                return;
            binding.signupPageCounter.setText("2/2");
            binding.linearLayoutSignupEtapa1.setVisibility(View.GONE);
            binding.linearLayoutSignupEtapa2.setVisibility(View.VISIBLE);
        });

        binding.btnSignupBack.setOnClickListener((view) -> {
            binding.signupPageCounter.setText("1/2");
            binding.linearLayoutSignupEtapa2.setVisibility(View.GONE);
            binding.linearLayoutSignupEtapa1.setVisibility(View.VISIBLE);
        });

        binding.txtBirthdate.getEditText().setOnClickListener((view) -> {
            showDatePickerDialog();
        });


        genders = new String[]{getString(R.string.female), getString(R.string.male), getString(R.string.other)};
        binding.txtGender.getEditText().setOnClickListener(v -> {
            selectGender();
        });

        binding.btnSignup.setOnClickListener(v -> {
            try {

                if (!validateEmail() | !validatePassword() | !validateUsername() )
                    return;
                userToAdd.setBirthdate(new SimpleDateFormat("dd/MM/yyyy").parse(getText(binding.txtBirthdate)));
                userToAdd.setEmail(getText(binding.txtSignupEmail));
                userToAdd.setFullName(getText(binding.txtSignupFullName));
                userToAdd.setPassword(getText(binding.txtSignupPassword));
                userToAdd.setGender(gender);
                userToAdd.setUsername(getText(binding.txtSignupUsername));
                app.getUserRepository().addUser(userToAdd).observe(this, r -> {
                    if (r.getStatus() == Status.SUCCESS) {
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
            } catch (ParseException ex) {
                Toast.makeText(getApplicationContext(), getString(R.string.error_feo), Toast.LENGTH_LONG).show(); ////Poner un error mas adecuado
            }

        });

    }

    private void selectGender() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.hint_gender));
        builder.setItems(genders, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case (0):
                        gender = "female";
                        break;
                    case (1):
                        gender = "male";
                        break;
                    case (2):
                        gender = "other";
                        break;
                }
                binding.txtGender.getEditText().setText(genders[which]);
            }
        });
        builder.show();
    }


    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String selectedDate = day + "/" + (month + 1) + "/" + year; // +1 because January is zero
                binding.txtBirthdate.getEditText().setText(selectedDate);
                validateDate();

            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");

    }

    private String getText(TextInputLayout t) {
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
                validateGender();
                if (error.getCode() == 2) {
                    if (error.getDetails().get(0).equals("UNIQUE constraint failed: Users.email"))
                        Toast.makeText(getApplicationContext(), R.string.error_email_taken, Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getApplicationContext(), R.string.error_username_taken, Toast.LENGTH_SHORT).show();

                }
                //binding.result.setText(message);
                break;
        }

    }

    private boolean validateFullname() {
        String val = fullname.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            fullname.setError(getString(R.string.field_empty_errror));
            return false;
        } else {
            fullname.setError(null);
            fullname.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername() {
        String val = username.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            username.setError(getString(R.string.field_empty_errror));
            return false;
        } else if (val.length() > 30) {
            username.setError(getString(R.string.too_long));
            return false;
        } else if (!val.matches("\\A\\w{1,30}\\z")) {
            username.setError(getString(R.string.no_whitespaces));
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString().trim();
        if (val.isEmpty()) {
            email.setError(getString(R.string.field_empty_errror));
            return false;
        } else if (!val.matches("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
            email.setError(getString(R.string.wrong_email_format));
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {

        String val = password.getEditText().getText().toString().trim();
        if (val.length()<8) {
            password.setError(getString(R.string.too_short_pass));

            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validateGender() {
        if(gender==null){
            binding.txtGender.setError(getString(R.string.field_empty_errror));
            return false;
        }
        else{
            binding.txtGender.setError(null);
            binding.txtGender.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validateDate(){
        String val = getText(binding.txtBirthdate).trim();
        if(val.isEmpty()){
            binding.txtBirthdate.setError(getString(R.string.field_empty_errror));
            return false;
        }
        else{
            binding.txtBirthdate.setError(null);
            binding.txtBirthdate.setErrorEnabled(false);
            return true;
        }

    }
}


