package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;

public class ForgetPassword extends AppCompatActivity implements View.OnClickListener{

    private TextInputLayout emailForgetL;
    private EditText emailForget;
    private Button sendButton;
    private ProgressBar prgSendEmail;

    private FirebaseAuth mAuth;

    private void sendEmail (String email) {

        emailForgetL.setErrorEnabled(false);
        if (email.isEmpty()) {
            emailForgetL.setErrorEnabled(true);
            emailForgetL.setError("¡Este campo es obligatorio!");
        }
        else {
            prgSendEmail.setVisibility(View.VISIBLE);
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                prgSendEmail.setVisibility(View.INVISIBLE);
                if (task.isSuccessful()){
                    finish();
                    Toast.makeText(ForgetPassword.this, "La contraseña ha sido enviada a tu correo", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(e -> {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    emailForgetL.setErrorEnabled(true);
                    emailForgetL.setError("¡Formato incorrecto!");
                }
                else if (e instanceof FirebaseAuthInvalidUserException) {
                    emailForgetL.setErrorEnabled(true);
                    emailForgetL.setError("¡Correo no encontrado!");
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.sendPasButton:
                String email = emailForget.getText().toString();
                sendEmail(email);
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        setTitle("Recuperar contraseña");

        emailForgetL = findViewById(R.id.forgetEmailLayout);
        emailForget = findViewById(R.id.forgetEmail);
        sendButton = findViewById(R.id.sendPasButton);
        prgSendEmail = findViewById(R.id.prgSendEmail);

        sendButton.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
    }
}