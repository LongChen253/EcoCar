package com.example.ecocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private Button loginButton;
    private Button registerButton;

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.LoginButton:
                Toast.makeText(this, "Function Not Implemented Yet", Toast.LENGTH_SHORT).show();
                break;
            case R.id.RegisterButton:
                startActivity(new Intent(this, Register.class));
                break;
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

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }
}