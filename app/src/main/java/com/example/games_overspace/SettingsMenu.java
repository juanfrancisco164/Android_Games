package com.example.games_overspace;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

public class SettingsMenu extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private TextView highScore2048;
    private TextView highScoreSenku;

    private static final int PICK_IMAGE_REQUEST = 1;

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_menu);

        sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);

        editTextUsername = findViewById(R.id.usernameEditText);
        editTextPassword = findViewById(R.id.passwordEditText);

        highScore2048 = findViewById(R.id.highScore2048);
        highScoreSenku = findViewById(R.id.highScoreSenku);

        highScore2048.setText("" + sharedPreferences.getInt("bestScore2048", 0));
        long bestTimeSenku = sharedPreferences.getLong("bestTimeSenku", 0); // Utiliza getLong aquí
        if (bestTimeSenku > 0) {
            highScoreSenku.setText(bestTimeSenku + " seconds");
        } else {
            highScoreSenku.setText("N/A");
        }
        findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMainMenu();
            }
        });

        findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveSettings(view);
            }
        });
    }

    public void selectImage(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                ImageButton imageButton = findViewById(R.id.imageUserCustom);
                imageButton.setImageBitmap(bitmap);
                imageButton.setScaleType(ImageButton.ScaleType.FIT_CENTER);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void backToMainMenu(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    public void saveSettings(View view){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", editTextUsername.getText().toString());
        editor.putString("password", editTextPassword.getText().toString());
        editor.apply();
        showAppliedSettingsDialog(view);
    }

    public void showAppliedSettingsDialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Usuario y contraseña guardados")
                .setTitle("Exito")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        highScore2048.setText("" + sharedPreferences.getInt("bestScore2048", 0));
        long bestTimeSenku = sharedPreferences.getLong("bestTimeSenku", 0); // De nuevo, utiliza getLong
        if (bestTimeSenku > 0) {
            highScoreSenku.setText(bestTimeSenku + " seconds");
        } else {
            highScoreSenku.setText("N/A");
        }    }

}
