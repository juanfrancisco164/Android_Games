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

import org.w3c.dom.Text;

public class Register extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        usernameEditText = findViewById(R.id.editTextRegister);
        passwordEditText = findViewById(R.id.editTextPasswordRegister);

        TextView loginButton = findViewById(R.id.backToLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLoginPage();
            }
        });

        TextView sendButton = findViewById(R.id.buttonSubmitRegister);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRegister();
            }
        });
    }

    public void openLoginPage() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

    public void handleRegister() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        if (!username.isEmpty() && !password.isEmpty() && !checkIfUserExists(username)) {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putString("username", username);
            editor.putString("password", password);

            editor.putInt("bestScore2048", 0);
            editor.putLong("bestTimeSenku", 0);

            editor.apply();

            showOkMessage();
        } else {
            showErrorMessage();
        }
    }


    private boolean checkIfUserExists(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
        String savedUsername = sharedPreferences.getString("username", "");
        if (savedUsername.equals(username)){
            return true;
        }
        return false;
    }

    private void showOkMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Usuario registrado con éxito.")
                .setTitle("Éxito")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        openLoginPage();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showErrorMessage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("El usuario ya existe o el nombre de usuario está vacío.")
                .setTitle("Error")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}