package com.example.ecocar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class Register extends AppCompatActivity implements View.OnClickListener{

    private Button createButton;
    private EditText usernameET;
    private EditText passwordET;
    private EditText confirmPasET;
    private EditText emailET;
    private TextView errU;
    private TextView errP;
    private TextView errC;
    private TextView errE;

    private boolean validatePass (String p1, String p2) {
        errP.setVisibility(View.GONE);
        errC.setVisibility(View.GONE);
        if (p1.isEmpty()) {
            errP.setText("Aquest camp és obligatori!");
            errP.setVisibility(View.VISIBLE);
        }
        if (p2.isEmpty()) {
            errC.setText("Aquest camp és obligatori!");
            errC.setVisibility(View.VISIBLE);
            return false;
        }
        if (!p1.equals(p2)) {
            errC.setText("Contrasenya incorrecta!");
            errC.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    private boolean validateEmail (String e) {
        errE.setVisibility(View.INVISIBLE);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (e.isEmpty()) {
            errE.setText("Aquest camp és obligatori!");
            errE.setVisibility(View.VISIBLE);
            return false;
        }
        if (!e.matches(emailPattern)) {
            errE.setText("Format incorrecte!");
            errE.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }


    private boolean validateUser (String u, String p1, String p2, String e) throws JSONException, ExecutionException, InterruptedException {
        errU.setVisibility(View.GONE);
        Boolean b1 = validatePass(p1, p2);
        Boolean b2 = validateEmail(e);
        if (u.isEmpty()) {
            errU.setText("Aquest camp és obligatori!");
            errU.setVisibility(View.VISIBLE);
            return false;
        }
        if (b1 && b2){
            ConnectionBackend createUser = new ConnectionBackend(this);
            String aux = createUser.execute("register", u, p1, e).get();
            JSONObject result = new JSONObject(aux);
            if (!result.getBoolean("result")) {
                errU.setText("Usuari ja existent");
                errU.setVisibility(View.VISIBLE);
                return false;
            }
            return true;
        }
        return false;
    }


    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.RegButton:
                String username = usernameET.getText().toString().trim();
                String password = passwordET.getText().toString();
                String confirmPas = confirmPasET.getText().toString();
                String email = emailET.getText().toString().trim();
                try {
                    if (validateUser(username, password, confirmPas, email)) finish();;
                } catch (JSONException | ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
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
        usernameET = findViewById(R.id.RegUsername);
        passwordET = findViewById(R.id.RegPassword);
        confirmPasET = findViewById(R.id.RegConfirmPas);
        emailET = findViewById(R.id.RegEmail);
        errU = findViewById(R.id.ErrUsername);
        errP = findViewById(R.id.ErrPassword);
        errC = findViewById(R.id.ErrConfirmPas);
        errE = findViewById(R.id.ErrEmail);

        createButton.setOnClickListener(this);
    }
}