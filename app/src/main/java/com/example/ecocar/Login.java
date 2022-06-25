package com.example.ecocar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private Button loginButton, registerButton, recoverPas;
    private EditText usernameET, passwordET;
    private TextInputLayout usernameL, passwordL;
    private ProgressBar pg;

    private FirebaseAuth mAuth;

    private void login (String u, String p) {
        usernameL.setErrorEnabled(false);
        passwordL.setErrorEnabled(false);
        boolean sign = true;
        if (u.isEmpty()) {
            usernameL.setErrorEnabled(true);
            usernameL.setError("¡Este campo es obligatorio!");
            sign = false;
        }
        if (p.isEmpty()) {
            passwordL.setErrorEnabled(true);
            passwordL.setError("¡Este campo es obligatorio!");
            sign = false;
        }
        if (sign) {
            mAuth.signInWithEmailAndPassword(u, p).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    pg.setVisibility(View.VISIBLE);
                    CurrentUser.getInstance().setUid(mAuth.getCurrentUser().getUid());
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference UserRef = mDatabase.child("users").child(mAuth.getCurrentUser().getUid());
                    mAuth.signInWithEmailAndPassword("test@qq.com", "123456").addOnCompleteListener(task12 -> {
                        UserRef.get().addOnCompleteListener(task1 -> {
                            DataSnapshot snapshot = task1.getResult();
                            CurrentUser.getInstance().setUsername(snapshot.child("username").getValue(String.class));
                            CurrentUser.getInstance().setEmail(snapshot.child("email").getValue(String.class));
                            CurrentUser.getInstance().setBirthday(snapshot.child("birthday").getValue(String.class));
                            CurrentUser.getInstance().setAddress(snapshot.child("address").getValue(String.class));
                            CurrentUser.getInstance().setBiography(snapshot.child("biography").getValue(String.class));
                            CurrentUser.getInstance().setPhotoURL(snapshot.child("photoURL").getValue(String.class));
                            CurrentUser.getInstance().setGender(snapshot.child("gender").getValue(String.class));
                            CurrentUser.getInstance().setPreferences(snapshot.child("preferences").getValue(String.class));
                            CurrentUser.getInstance().setPhone(snapshot.child("phone").getValue(Integer.class));
                            CurrentUser.getInstance().setStar(snapshot.child("star").getValue(Float.class));
                            CurrentUser.getInstance().setNumComments(snapshot.child("numComments").getValue(Integer.class));
                            CurrentUser.getInstance().setCarSelected(snapshot.child("carSelected").getValue(String.class));
                            pg.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(Login.this, MainPage.class));
                        });
                    });
                }
            }).addOnFailureListener(e -> {
                if (e instanceof FirebaseAuthInvalidCredentialsException || e instanceof FirebaseAuthInvalidUserException) {
                    passwordL.setErrorEnabled(true);
                    passwordL.setError("¡Contraseña incorrecta o usuario no existe!");
                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.LoginButton:
                String username = usernameET.getText().toString().trim();
                String password = passwordET.getText().toString();
                login(username, password);
                break;
            case R.id.RegisterButton:
                startActivity(new Intent(this, Register.class));
                break;
            case R.id.RecoverPassword:
                startActivity(new Intent(this, ForgetPassword.class));
                break;
            default:
                break;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameL = findViewById(R.id.LogUsernameLayout);
        passwordL = findViewById(R.id.LogPasswordLayout);
        usernameET = findViewById(R.id.LogUsername);
        passwordET = findViewById(R.id.LogPassword);
        loginButton = findViewById(R.id.LoginButton);
        registerButton = findViewById(R.id.RegisterButton);
        recoverPas = findViewById(R.id.RecoverPassword);
        pg = findViewById(R.id.Login_pb);

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        recoverPas.setOnClickListener(this);
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

        mAuth = FirebaseAuth.getInstance();
    }
}