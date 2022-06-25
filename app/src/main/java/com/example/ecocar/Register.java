package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private Button createButton;
    private TextInputLayout emailL, passwordL, confirmPasL;
    private EditText emailET, passwordET, confirmPasET;

    private FirebaseAuth mAuth;

    private boolean validateEmail (String e) {
        emailL.setErrorEnabled(false);
        if (e.isEmpty()) {
            emailL.setErrorEnabled(true);
            emailL.setError("¡Este campo es obligatorio!");
            return false;
        }
        return true;
    }

    private boolean validatePass (String p1, String p2) {
        passwordL.setErrorEnabled(false);
        confirmPasL.setErrorEnabled(false);

        if (p1.isEmpty()) {
            passwordL.setErrorEnabled(true);
            passwordL.setError("¡Este campo es obligatorio!");
        }
        if (p2.isEmpty()) {
            confirmPasL.setErrorEnabled(true);
            confirmPasL.setError("¡Este campo es obligatorio!");
            return false;
        }
        else if (!p1.equals(p2)) {
            confirmPasL.setErrorEnabled(true);
            confirmPasL.setError("¡Contraseña incorrecta!");
            return false;
        }

        return true;
    }

    private void register (String e, String p1, String p2) {
        Boolean b1 = validatePass(p1, p2);
        Boolean b2 = validateEmail(e);

        if (b1 && b2){
            new AlertDialog.Builder(this)
                    .setMessage("Al usar esta aplicación, debe aceptar nuestros términos y condiciones y nuestra política de privacidad.")
                    .setPositiveButton("SÍ", (dialogInterface, i) -> mAuth.createUserWithEmailAndPassword(e, p1).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Register.this, UserProfileInfoForm.class);
                            intent.putExtra("userUID", task.getResult().getUser().getUid());
                            startActivity(intent);
                        }
                    }).addOnFailureListener(e1 -> {
                        if (e1 instanceof FirebaseAuthWeakPasswordException) {
                            passwordL.setErrorEnabled(true);
                            passwordL.setError("¡La contraseña debe tener 6 carácteres como mínimo!");
                        }
                        else if (e1 instanceof FirebaseAuthInvalidCredentialsException) {
                            emailL.setErrorEnabled(true);
                            emailL.setError("¡Formato incorrecto!");
                        }
                        else if (e1 instanceof FirebaseAuthUserCollisionException) {
                            emailL.setErrorEnabled(true);
                            emailL.setError("¡Usuario ya existe!");
                        }
                    }))
                    .setNegativeButton("CANCELAR", (dialogInterface, i) -> finish())
                    .show();
        }
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.RegButton:
                String email = emailET.getText().toString();
                String password = passwordET.getText().toString();
                String confirmPas = confirmPasET.getText().toString();
                register(email, password, confirmPas);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createButton = findViewById(R.id.RegButton);
        emailET = findViewById(R.id.RegEmail);
        passwordET = findViewById(R.id.RegPassword);
        confirmPasET = findViewById(R.id.RegConfirmPas);
        emailL = findViewById(R.id.RegEmailLayout);
        passwordL = findViewById(R.id.RegPasswordLayout);
        confirmPasL = findViewById(R.id.RegConfirmPasLayout);

        createButton.setOnClickListener(this);
        passwordET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwordL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        confirmPasET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                confirmPasL.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
    }

}