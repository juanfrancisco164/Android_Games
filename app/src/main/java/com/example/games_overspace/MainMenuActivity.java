package com.example.games_overspace;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);
    }

    public void start2048(View view) {
        Intent intent = new Intent(this, Main2048.class);
        startActivity(intent);
    }

    public void startSenku(View view) {
        Intent intent = new Intent(this, MainSenku.class);
        startActivity(intent);
    }
}
