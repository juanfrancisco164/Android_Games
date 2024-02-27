package com.example.games_overspace;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.editTextUsernameLogin);
        passwordEditText = findViewById(R.id.editTextPasswordLogin);

        TextView registerButton = findViewById(R.id.buttonRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegisterPage();
            }
        });

        TextView sendButton = findViewById(R.id.buttonLogin);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin();
            }
        });
    }

    public void openRegisterPage() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    private void handleLogin(){
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        String savedPassword = sharedPreferences.getString("password", "");
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.equals(savedUsername) && password.equals(savedPassword)){
            openMainPage();
        }else{
            showErrorMessage();
        }
    }

    private void showErrorMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Usuario o contraseña invalida")
                .setTitle("Error")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void openMainPage(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        // Deshabilitar el botón de retroceso
    }
}