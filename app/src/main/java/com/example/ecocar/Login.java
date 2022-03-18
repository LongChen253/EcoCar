package com.example.ecocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private Button loginButton;
    private Button registerButton;
    private ImageView showHidePassword;
    private EditText password;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.LoginButton:
                Toast.makeText(this, "Function Not Implemented Yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.RegisterButton:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.SHPassword:
                if (password.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance())) {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showHidePassword.setImageResource(R.drawable.ic_show_password);
                }
                else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showHidePassword.setImageResource(R.drawable.ic_hide_password);
                }
            default:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.LoginButton);
        registerButton = findViewById(R.id.RegisterButton);
        showHidePassword = findViewById(R.id.SHPassword);
        password = findViewById(R.id.LogPassword);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        showHidePassword.setOnClickListener(this);
    }
}